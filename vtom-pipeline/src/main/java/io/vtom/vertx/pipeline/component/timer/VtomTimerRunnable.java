package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.step.Step;

class VtomTimerRunnable implements PipeRunnable<Timer, VtmTimerOut> {

  private Vertx vertx;
  private Step<? extends Timer> step;

  VtomTimerRunnable(Vertx vertx, Step<? extends Timer> step) {
    this.vertx = vertx;
    this.step = step;
  }

  @Override
  public Step<? extends Timer> step() {
    return this.step;
  }

  @Override
  public void call(VtmTimerOut stepout, Handler<AsyncResult<Object>> handler) {
    stepout.execute(this.vertx, handler);
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    handler.handle(Future.succeededFuture());
  }
}
