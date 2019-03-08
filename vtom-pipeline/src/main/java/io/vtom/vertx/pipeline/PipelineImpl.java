package io.vtom.vertx.pipeline;

import io.enoa.promise.Promise;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.runnable.Piperunnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class PipelineImpl implements Pipeline {

  private enum Stat {
    WAIT,
    RUN,
    STOP,
  }

  private Vertx vertx;
  private Scope scope;
  private Pipecycle pipecycle;
  private Stat stat;
  private int runStep;

  private List<Piperunnable> piperunnables;


  PipelineImpl(Vertx vertx, Scope scope) {
    this.vertx = vertx;
    this.scope = scope;
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

    this.piperunnables.sort(Comparator.comparingInt(Piperunnable::ord));


//    for (Piperunnable piperunnable : this.piperunnables) {
//      Pipepromise pipo = piperunnable.call();
//    }
    this.call(promise, 0, null);

    return _ret;
  }


  private void call(EPDoneArgPromiseBuilder<Pipecycle> finalpromise, int ix, Pipepromise steppromise) {
    if (this.stat == Stat.STOP) {
      if (finalpromise.always() != null && steppromise != null) {
        steppromise.always(() -> finalpromise.always().execute());
        return;
      }
      finalpromise.always().execute();
      return;
    }
    if (ix == this.piperunnables.size()) {
      finalpromise.dones().forEach(done -> done.execute(this.pipecycle));
      if (finalpromise.always() != null && steppromise != null) {
        steppromise.always(() -> finalpromise.always().execute());
        return;
      }
      finalpromise.always().execute();
      return;
    }

    Piperunnable piperunnable = this.piperunnables.get(ix);
    int ord = piperunnable.ord();


    if (steppromise == null || ord <= 0) {
      Pipepromise parallelpromise = piperunnable.call();
      parallelpromise.capture(thr -> {
        finalpromise.captures().forEach(capture -> capture.execute(thr));
        this.stat = Stat.STOP;
      });

      this.call(finalpromise, ix + 1, ord <= 0 ? steppromise : parallelpromise);
      return;
    }

    Pipepromise serialpromise  = piperunnable.call();
    serialpromise.capture(thr -> {
      finalpromise.captures().forEach(capture -> capture.execute(thr));
      this.stat = Stat.STOP;
    }).done(done -> this.call(finalpromise, ix + 1, serialpromise));


//    Pipepromise pipo = piperunnable.call();
//
//
//    pipo.done(pipecycle -> {
//      this.call(finalpromise, ix + 1);
//    })
//      .capture(thr -> {
//        finalpromise.captures().forEach(capture -> capture.execute(thr));
//        this.stat = Stat.STOP;
//      });
  }


}
