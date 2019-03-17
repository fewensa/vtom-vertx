package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.step.StepOUT;

public class VtmPeriodicSubmit extends AbstractPeriodic<VtmPeriodicSubmit> {

  private long delay;
  private Handler<Long> handler;

  public VtmPeriodicSubmit(Handler<Long> handler) {
    this.handler = handler;
  }

  public VtmPeriodicSubmit delay(long delay) {
    this.delay = delay;
    return this;
  }

  @Override
  public StepOUT out() {
    return new VtmPeriodicOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> _handler) {
        long id = vertx.setPeriodic(delay, handler);
        _handler.handle(Future.succeededFuture(id));
      }
    };
  }
}
