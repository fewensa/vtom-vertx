package io.vtom.vertx.pipeline;

import io.enoa.promise.Promise;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.runnable.Piperunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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


  PipelineImpl(Vertx vertx, Scope scope) {
    this.vertx = vertx;
    this.pipecycle = new Pipecycle(vertx, scope);
    this.piperunnables = null;
    this.stat = Stat.WAIT;
  }

  @Override
  public Pipecycle cycle() {
    return this.pipecycle;
  }

  @Override
  public Pipeline next(Piperunnable piperunnable) {
    if (this.piperunnables == null)
      this.piperunnables = new ArrayList<>();
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

    this.sort();

    this.callv2(promise, 0, null);
    return _ret;
  }


  private void sort() {
    this.piperunnables.sort(Comparator.comparingInt(Piperunnable::ord));
    int nix;
    while ((nix = this.clocix(runnable -> runnable.ord() <= 0 && runnable.after() > 0)) != -1) {
      Piperunnable nrunnable = this.piperunnables.get(nix);
      int tix = this.clocix(runnable -> runnable.ord() == nrunnable.after());
      Collections.swap(this.piperunnables, nix, tix + 1);
    }
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
    int ord = piperunnable.ord();

    if (steppromise == null) {
      Pipepromise itempromise = piperunnable.call();
      this.callv2(endpromise, ix + 1, itempromise);
      return;
    }

    if (ord <= 0) {
      Pipepromise parallpromise = piperunnable.call();
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
      Pipepromise serialpromise = piperunnable.call();
      this.callv2(endpromise, ix + 1, serialpromise);
    });

  }

}
