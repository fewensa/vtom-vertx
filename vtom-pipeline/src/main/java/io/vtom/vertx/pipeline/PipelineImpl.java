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

//    this.callv2(promise, 0, null);
    this.callv3(promise, 0);
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

  private void callv3(EPDoneArgPromiseBuilder<PipeLifecycle> endpromise, int ix) {

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
      this.handleDone(endpromise);
      if (!this.alwayscalled.get())
        Promise.builder().handler().handleAlways(endpromise);
      this.alwayscalled.set(Boolean.TRUE);
      return;
    }

    PipeRunnable piperunnable = this.piperunnables.get(ix);
    StepWrapper wrapper = piperunnable.wrapper();
    StepOUT out = wrapper.stepstack().stepin(this.pipecycle).out(wrapper);

    // if parallel run, not wait step promise.
    if (wrapper.ord() <= 0) {
      this.runnableCall(piperunnable, out,
        value -> this.release(ix, true,
          () -> {
            ScopeContext.context(this.pipecycle.scope()).put(out, value);
            // parallel pipeline, should not set last step id

            if (ix + 1 < this.piperunnables.size()) {
              return;
            }

            Set<PipeRunnable> ordset = this.piperunnables.stream()
              .filter(runnable -> runnable.wrapper().ord() > 0)
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
          this.stat = Stat.STOP;
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
    this.runnableCall(piperunnable, out,
      value -> this.release(ix, true,
        () -> {
          ScopeContext.context(this.pipecycle.scope()).put(out, value);
          ScopeContext.context(this.pipecycle.scope()).last(out.id());

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

}
