package io.vtom.vertx.pipeline;

import io.enoa.promise.Promise;
import io.enoa.promise.arg.PromiseArg;
import io.enoa.promise.arg.PromiseCapture;
import io.enoa.promise.arg.PromiseVoid;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.eo.tip.EnoaTipKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.lifecycle.scope.VtmScopeContext;
import io.vtom.vertx.pipeline.lifecycle.skip.VtmSkipContext;
import io.vtom.vertx.pipeline.step.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

class PipelineImpl implements Pipeline {

  private enum Stat {
    WAIT,
    RUN,
    STOP,
    END
  }

  private PipeLifecycle pipecycle;
  private AtomicReference<Stat> stat;
  private AtomicBoolean alwayscalled;

  private List<PipeRunnable> piperunnables;


  PipelineImpl(Vertx vertx) {
    this.pipecycle = new PipeLifecycle(vertx, Scope.scope());
    this.piperunnables = null;
    this.stat = new AtomicReference<>(Stat.WAIT);
    this.alwayscalled = new AtomicBoolean(Boolean.FALSE);
  }

  @Override
  public PipeLifecycle lifecycle() {
    return this.pipecycle;
  }

  @Override
  public <I extends StepIN, O extends StepOUT> Pipeline next(PipeRunnable<I, O> piperunnable) {
    if (piperunnable == null)
      return this;
    if (piperunnable.step() == null)
      return this;

    if (this.piperunnables == null)
      this.piperunnables = new ArrayList<>();
    boolean exists = this.piperunnables.stream()
      .filter(runnable -> runnable.step() != null)
      .filter(runnable -> VtmStepContext.context(runnable.step()).ord() > 0)
      .map(runnable -> VtmStepContext.context(runnable.step()).ord())
      .collect(Collectors.toSet())
      .contains(VtmStepContext.context(piperunnable.step()).ord());
    if (exists)
      throw new IllegalArgumentException(EnoaTipKit.message("eo.tip.vtom.pipeline.duplicates_ord"));
    this.piperunnables.add(piperunnable);
    return this;
  }

  @Override
  public PipePromise enqueue() {
    this.stat.set(Stat.RUN);

    EPDoneArgPromiseBuilder<PipeLifecycle> promise = Promise.builder().donearg();
    PipePromise _ret = new PipePromise(promise.build());


    this.pipecycle.vertx()
      .getOrCreateContext()
      .runOnContext(v -> {

        if (CollectionKit.isEmpty(this.piperunnables)) {
          // too fast returned
          this.pipecycle.vertx().setTimer(100, id -> {
            if (promise.always() != null)
              promise.always().execute();
          });
          return;
        }

        this.recheck();
        this.callv3(promise, 0);
      });

    return _ret;
  }


  private void recheck() {
    Set<String> idsets = this.piperunnables.stream()
      .filter(runnable -> runnable.step() != null)
      .map(runnable -> VtmStepContext.context(runnable.step()).id())
      .collect(Collectors.toSet());
    if (idsets.size() != this.piperunnables.size())
      throw new RuntimeException(EnoaTipKit.message("eo.tip.vtom.pipeline.duplicates_id"));
    CollectionKit.clear(idsets);

    this.piperunnables.sort(Comparator.comparingInt(o -> VtmStepContext.context(o.step()).ord()));
    Set<String> moveds = new HashSet<>(this.piperunnables.size());
    int nix;
    while ((nix = this.clocix(runnable ->
      !moveds.contains(VtmStepContext.context(runnable.step()).id()) &&
        VtmStepContext.context(runnable.step()).ord() <= 0 &&
        VtmStepContext.context(runnable.step()).after() > 0)) != -1) {
      PipeRunnable nrunnable = this.piperunnables.get(nix);
      moveds.add(VtmStepContext.context(nrunnable.step()).id());
      int tix = this.clocix(runnable -> VtmStepContext.context(runnable.step()).ord() == VtmStepContext.context(nrunnable.step()).after());
      if (tix == -1)
        continue;
//      Collections.swap(this.piperunnables, nix, tix);
      Collections.rotate(this.piperunnables.subList(nix, tix + 1), -1);
    }
    CollectionKit.clear(moveds);
  }

  private int clocix(CondIx cix) {
    for (int i = 0; i < this.piperunnables.size(); i++) {
      PipeRunnable piperunnable = this.piperunnables.get(i);
      if (piperunnable.step() == null)
        continue;
      if (cix.cond(piperunnable))
        return i;
    }
    return -1;
  }

  private interface CondIx {
    boolean cond(PipeRunnable piperunnable);
  }

  private void callv3(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise, int ix) {

    // if pipeline status is STOP, end call
    if (this.stat.getAndSet(Stat.END) == Stat.STOP) {
      if (!this.alwayscalled.get())
        Promise.builder().handler().handleAlways(endpromise);
      this.alwayscalled.set(Boolean.TRUE);
      return;
    }

    // last of piperunnable, end call
    if (ix == this.piperunnables.size()) {
      this.handleDone(endpromise);
      if (!this.alwayscalled.get())
        Promise.builder().handler().handleAlways(endpromise);
      this.alwayscalled.set(Boolean.TRUE);
      return;
    }

    PipeRunnable piperunnable = this.piperunnables.get(ix);
    Step step = piperunnable.step();
    if (step == null) {
      this.callv3(endpromise, ix + 1);
      return;
    }

    VtmStepContext stepcontext = VtmStepContext.context(step);
    StepStack stepstack = stepcontext.stepstack();
    if (stepstack == null) {
      this.callv3(endpromise, ix + 1);
      return;
    }
    StepIN stepin = stepstack.stepin(this.pipecycle);
    if (stepin == null) {
      this.callv3(endpromise, ix + 1);
      return;
    }

    StepOUT stepout = stepin.out();
    if (stepout == null) {
      this.callv3(endpromise, ix + 1);
      return;
    }

    // only serial pipeline support skip
    if (stepcontext.ord() > 0) {
      // register skip
      stepout.skip(this.pipecycle.skip());

      VtmSkipContext skipcontext = VtmSkipContext.context(this.pipecycle.skip());

      if (skipcontext.skip(stepcontext)) {
        this.callv3(endpromise, ix + 1);
        return;
      }
    }

    // if parallel run, not wait step promise.
    if (stepcontext.ord() <= 0) {
      this.runnableCall(piperunnable, stepout,
        value -> this.release(ix, true,
          () -> {
            VtmScopeContext.context(this.pipecycle.scope()).put(stepcontext, value);
            // parallel pipeline, should not set last step id

            if (ix + 1 < this.piperunnables.size()) {
              return;
            }

            Set<PipeRunnable> ordset = this.piperunnables.stream()
              .filter(runnable -> VtmStepContext.context(runnable.step()).ord() > 0)
              .collect(Collectors.toSet());
            // all pipeline are parallel pipeline, last pipeline call promise done.
            if (ordset.size() == 0) {
              this.handleDone(endpromise);
              if (!this.alwayscalled.get())
                Promise.builder().handler().handleAlways(endpromise);
              this.alwayscalled.set(Boolean.TRUE);
            }
            CollectionKit.clear(ordset);
          },
          thr1 -> this.captureCall(endpromise, thr1)),
        thr0 -> {
//          this.stat = Stat.STOP;
          this.stat.set(Stat.STOP);
          this.release(ix, false,
            () -> this.captureCall(endpromise, thr0),
            thr1 -> {
              thr1.addSuppressed(thr0);
              this.captureCall(endpromise, thr1);
            });
        });
      this.callv3(endpromise, ix + 1);
      return;
    }

    // serial run, wait step promise.
    this.runnableCall(piperunnable, stepout,
      value -> this.release(ix, true,
        () -> {
          VtmScopeContext.context(this.pipecycle.scope()).put(stepcontext, value);
          VtmScopeContext.context(this.pipecycle.scope()).last(stepcontext);

          this.callv3(endpromise, ix + 1);
        },
        thr1 -> this.captureCall(endpromise, thr1)),
      thr0 -> this.release(ix, false,
        () -> this.captureCall(endpromise, thr0),
        thr1 -> {
          thr1.addSuppressed(thr0);
          this.captureCall(endpromise, thr1);
        }));

  }

  private void captureCall(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise, Throwable thr) {
    endpromise.captures().forEach(capture -> capture.execute(thr));
    if (!this.alwayscalled.get())
      endpromise.always().execute();
    this.alwayscalled.set(Boolean.TRUE);
  }


  private void release(int ix, boolean ok, PromiseVoid successHandler, PromiseCapture failHandler) {
//    if (ix == this.piperunnables.size()) {
//      successHandler.execute();
//      return;
//    }
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

  private void runnableCall(PipeRunnable runnable, StepOUT stepout, PromiseArg<Object> successHandler, PromiseCapture failHandler) {
    Handler<AsyncResult<Object>> handler = ar -> {
      if (ar.failed()) {
        failHandler.execute(ar.cause());
        return;
      }
      Object value = ar.result();
      successHandler.execute(value);
    };
    try {
      runnable.call(stepout, handler);
    } catch (Exception e) {
      failHandler.execute(e);
    }
  }

  private void handleDone(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise) {
    for (PromiseArg<PipeLifecycle> done : endpromise.dones()) {
      try {
        done.execute(this.pipecycle);
      } catch (Exception e) {
        for (PromiseCapture capture : endpromise.captures()) {
          try {
            capture.execute(e);
          } catch (Exception ex) {
            ex.printStackTrace();
            break;
          }
        }
        break;
      }
    }
  }

}
