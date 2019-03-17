package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.step.StepOUT;

public class VtmTimerSubmit extends AbstractTimer<VtmTimerSubmit> {

  private long delay;
  private Handler<Long> handler;

  public VtmTimerSubmit(Handler<Long> handler) {
    this.handler = handler;
  }

  public VtmTimerSubmit delay(long delay) {
    this.delay = delay;
    return this;
  }

  @Override
  public StepOUT out() {
    return new VtmTimerOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> _handler) {
        long id = vertx.setTimer(delay, handler);
        _handler.handle(Future.succeededFuture(id));
      }
    };
  }
}
