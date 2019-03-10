package io.vtom.vertx.pipeline;

import io.enoa.promise.Promise;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.eo.tip.EnoaTipKit;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.*;
import java.util.stream.Collectors;

class PipelineImpl implements Pipeline {

  private enum Stat {
    WAIT,
    RUN,
    STOP,
  }

  private Vertx vertx;
  private Pipecycle pipecycle;
  private Stat stat;
  private boolean alwayscalled = false;

  private List<Piperunnable> piperunnables;


  PipelineImpl(Vertx vertx, ScopeContext context) {
    this.vertx = vertx;
    this.pipecycle = new Pipecycle(vertx, context.scope());
    this.piperunnables = null;
    this.stat = Stat.WAIT;
  }

  @Override
  public Pipecycle cycle() {
    return this.pipecycle;
  }

  @Override
  public <I extends StepIN, O extends StepOUT> Pipeline next(Piperunnable<I, O> piperunnable) {
    if (this.piperunnables == null)
      this.piperunnables = new ArrayList<>();
    boolean exists = this.piperunnables.stream()
      .filter(runnable -> runnable.wrapper().ord() > 0)
      .map(runnable -> runnable.wrapper().ord())
      .collect(Collectors.toSet())
      .contains(piperunnable.wrapper().ord());
    if (exists)
      throw new IllegalArgumentException(EnoaTipKit.message("eo.tip.vtom.pipeline.duplicates_ord"));
    this.piperunnables.add(piperunnable);
    return this;
  }

  @Override
  public Pipepromise enqueue() {
    this.stat = Stat.RUN;

    EPDoneArgPromiseBuilder<Pipecycle> promise = Promise.builder().donearg();
    Pipepromise _ret = new Pipepromise(promise.build());
    if (CollectionKit.isEmpty(this.piperunnables)) {
      if (promise.always() != null)
        promise.always().execute();
      return _ret;
    }

    this.recheck();

    this.callv2(promise, 0, null);
    return _ret;
  }


  private void recheck() {
    Set<String> idsets = this.piperunnables.stream()
      .map(runnable -> runnable.wrapper().id())
      .collect(Collectors.toSet());
    if (idsets.size() != this.piperunnables.size())
      throw new RuntimeException(EnoaTipKit.message("eo.tip.vtom.pipeline.duplicates_id"));
    CollectionKit.clear(idsets);

    this.piperunnables.sort(Comparator.comparingInt(o -> o.wrapper().ord()));
    Set<String> moveds = new HashSet<>(this.piperunnables.size());
    int nix;
    while ((nix = this.clocix(runnable -> !moveds.contains(runnable.wrapper().id()) &&
      runnable.wrapper().ord() <= 0 &&
      runnable.wrapper().after() > 0)) != -1) {
      Piperunnable nrunnable = this.piperunnables.get(nix);
      moveds.add(nrunnable.wrapper().id());
      int tix = this.clocix(runnable -> runnable.wrapper().ord() == nrunnable.wrapper().after());
      if (tix == -1)
        continue;
//      Collections.swap(this.piperunnables, nix, tix);
      Collections.rotate(this.piperunnables.subList(nix, tix + 1), -1);
    }
    CollectionKit.clear(moveds);
  }

  private int clocix(CondIx cix) {
    for (int i = 0; i < this.piperunnables.size(); i++) {
      if (cix.cond(this.piperunnables.get(i)))
        return i;
    }
    return -1;
  }

  private interface CondIx {
    boolean cond(Piperunnable piperunnable);
  }


  private void callv2(EPDoneArgPromiseBuilder<Pipecycle> endpromise, int ix, Pipepromise steppromise) {

    if (this.stat == Stat.STOP) {
      if (!this.alwayscalled && endpromise.always() != null)
        endpromise.always().execute();
      this.alwayscalled = true;
      return;
    }

    if (ix == this.piperunnables.size()) {
      steppromise.done(cycle -> endpromise.dones().forEach(edone -> edone.execute(cycle)));
      steppromise.always(() -> {
        if (endpromise.always() == null)
          return;
        if (!this.alwayscalled)
          endpromise.always().execute();
        this.alwayscalled = true;
      });
      return;
    }

    Piperunnable piperunnable = this.piperunnables.get(ix);
    StepWrapper wrapper = piperunnable.wrapper();
//    int ord = piperunnable.wrapper().ord();

    if (steppromise == null) {
      StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
      Pipepromise itempromise = piperunnable.call(out);
      this.callv2(endpromise, ix + 1, itempromise);
      return;
    }

    if (wrapper.ord() <= 0) {
      StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
      Pipepromise parallpromise = piperunnable.call(out);
      parallpromise.capture(thr -> {
        endpromise.captures().forEach(capture -> capture.execute(thr));
        this.stat = Stat.STOP;
        if (!this.alwayscalled)
          endpromise.always().execute();
        this.alwayscalled = true;
      });
      this.callv2(endpromise, ix + 1, steppromise);
      return;
    }

    steppromise.capture(thr -> {
      this.stat = Stat.STOP;
      endpromise.captures().forEach(capture -> capture.execute(thr));
      if (endpromise.always() == null)
        return;
      if (!this.alwayscalled)
        endpromise.always().execute();
      this.alwayscalled = true;
    });

    steppromise.done(cycle -> {
      StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
      Pipepromise serialpromise = piperunnable.call(out);
      this.callv2(endpromise, ix + 1, serialpromise);
    });

  }

}
