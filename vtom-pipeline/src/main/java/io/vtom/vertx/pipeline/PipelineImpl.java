package io.vtom.vertx.pipeline;

import io.enoa.promise.Promise;
import io.enoa.promise.arg.PromiseCapture;
import io.enoa.promise.arg.PromiseVoid;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.eo.tip.EnoaTipKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

class PipelineImpl implements Pipeline {

  private enum Stat {
    WAIT,
    RUN,
    STOP,
    END
  }

  private PipeLifecycle pipecycle;
  private Stat stat;
  private AtomicBoolean alwayscalled;

  private List<PipeRunnable> piperunnables;


  PipelineImpl(ScopeContext context) {
    this.pipecycle = new PipeLifecycle(context.scope());
    this.piperunnables = null;
    this.stat = Stat.WAIT;
    this.alwayscalled = new AtomicBoolean(Boolean.FALSE);
  }

  @Override
  public PipeLifecycle cycle() {
    return this.pipecycle;
  }

  @Override
  public <I extends StepIN, O extends StepOUT> Pipeline next(PipeRunnable<I, O> piperunnable) {
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
  public PipePromise enqueue() {
    this.stat = Stat.RUN;

    EPDoneArgPromiseBuilder<PipeLifecycle> promise = Promise.builder().donearg();
    PipePromise _ret = new PipePromise(promise.build());
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
    while ((nix = this.clocix(runnable ->
      !moveds.contains(runnable.wrapper().id()) &&
        runnable.wrapper().ord() <= 0 &&
        runnable.wrapper().after() > 0)) != -1) {
      PipeRunnable nrunnable = this.piperunnables.get(nix);
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
    boolean cond(PipeRunnable piperunnable);
  }


  private void callv2(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise, int ix, PipePromise steppromise) {

    // if pipeline status is STOP, end call
    if (this.stat == Stat.STOP) {
      this.stat = Stat.END;

      if (!this.alwayscalled.get())
        Promise.builder().handler().handleAlways(endpromise);
      this.alwayscalled.set(Boolean.TRUE);
      return;
    }

    // last of piperunnable, end call
    if (ix == this.piperunnables.size()) {
      this.stat = Stat.END;

      steppromise.done(pipecycle -> this.release(ix - 1, true,
        () -> Promise.builder().handler().handleDoneArg(endpromise, this.pipecycle),
        thr -> this.captureCall(endpromise, thr)));

      steppromise.capture(thr -> this.release(ix - 1, false,
        () -> this.captureCall(endpromise, thr),
        rth -> {
          rth.addSuppressed(thr);
          this.captureCall(endpromise, rth);
        }));

      return;
    }

    PipeRunnable piperunnable = this.piperunnables.get(ix);
    StepWrapper wrapper = piperunnable.wrapper();


    // if parallel run, not wait step promise.
    if (wrapper.ord() <= 0) {
      StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
      PipePromise parallelpromise = piperunnable.call(out);

      // All pipeline are parallel pipeline, return last step pipeline promise
      boolean allparallel = ix + 1 == this.piperunnables.size() && steppromise == null;
      if (!allparallel) {
        parallelpromise.done(pipecycle -> this.release(ix, true,
          () -> {
          },
          thr -> {
            this.stat = Stat.STOP;
            this.captureCall(endpromise, thr);
          }));

        parallelpromise.capture(thr -> {
          this.stat = Stat.STOP;
          this.release(ix, false,
            () -> this.captureCall(endpromise, thr),
            rth -> {
              rth.addSuppressed(thr);
              this.captureCall(endpromise, rth);
            });
        });
      }

      this.callv2(endpromise, ix + 1, allparallel ? parallelpromise : steppromise);
      return;
    }


    // first serial run
    boolean isfirstserial = steppromise == null;
    if (steppromise == null) {
      StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
      steppromise = piperunnable.call(out);
      this.callv2(endpromise, ix + 1, steppromise);
      return;
    }

    // serial run, wait step promise.
    steppromise.done(pipecycle -> this.release(ix, true,
      () -> {
        StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);
        PipePromise serialpromise = piperunnable.call(out);
        this.callv2(endpromise, ix + 1, serialpromise);
      },
      thr -> this.captureCall(endpromise, thr)));

    steppromise.capture(thr -> this.release(ix, false,
      () -> this.captureCall(endpromise, thr),
      rth -> {
        rth.addSuppressed(thr);
        this.captureCall(endpromise, rth);
      }));

  }


  private void captureCall(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise, Throwable thr) {
    endpromise.captures().forEach(capture -> capture.execute(thr));
    if (!this.alwayscalled.get())
      endpromise.always().execute();
    this.alwayscalled.set(Boolean.TRUE);
  }


  private void release(int ix, boolean ok, PromiseVoid successHandler, PromiseCapture failHandler) {
    if (ix == this.piperunnables.size()) {
      successHandler.execute();
      return;
    }
    PipeRunnable runnable = this.piperunnables.get(ix);
    Handler<AsyncResult<Void>> handler = ar -> {
      if (ar.failed()) {
        failHandler.execute(ar.cause());
        return;
      }
      successHandler.execute();
    };
    runnable.release(ok, handler);
  }


}
