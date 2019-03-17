package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.step.StepOUT;

public class VtmPeriodicCancel extends AbstractPeriodic<VtmPeriodicCancel> {

  private long id;

  public VtmPeriodicCancel(long id) {
    this.id = id;
  }

  @Override
  public StepOUT out() {
    return new VtmPeriodicOut(stepskips()) {
      @Override
      protected void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        boolean ok = vertx.cancelTimer(id);
        handler.handle(Future.succeededFuture(ok));
      }
    };
  }
}
