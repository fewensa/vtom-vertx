package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.step.Step;

class VtomPeriodicRunnable implements PipeRunnable<Periodic, VtmPeriodicOut> {

  private Vertx vertx;
  private Step<? extends Periodic> step;

  VtomPeriodicRunnable(Vertx vertx, Step<? extends Periodic> step) {
    this.vertx = vertx;
    this.step = step;
  }

  @Override
  public Step<? extends Periodic> step() {
    return this.step;
  }

  @Override
  public void call(VtmPeriodicOut stepout, Handler<AsyncResult<Object>> handler) {
    stepout.execute(this.vertx, handler);
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    handler.handle(Future.succeededFuture());
  }
}
