package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.*;
import io.vtom.vertx.pipeline.step.StepOUT;

public class VtmPeriodicStream extends AbstractPeriodic<VtmPeriodicStream> {

  private long delay;
  private Handler<Throwable> exceptionHandler;
  private Handler<Long> handler;
  private Long fetch;
  private Handler<Void> endHandler;


  public VtmPeriodicStream(long delay) {
    this.delay = delay;
  }

  public VtmPeriodicStream exceptionHandler(Handler<Throwable> exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    return this;
  }

  public VtmPeriodicStream handler(Handler<Long> handler) {
    this.handler = handler;
    return this;
  }

  public VtmPeriodicStream fetch(Long fetch) {
    this.fetch = fetch;
    return this;
  }

  public VtmPeriodicStream endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  @Override
  public StepOUT out() {
    return new VtmPeriodicOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> _handler) {
        TimeoutStream stream = vertx.periodicStream(delay);
        if (exceptionHandler != null)
          stream.exceptionHandler(exceptionHandler);
        if (endHandler != null)
          stream.endHandler(endHandler);
        if (fetch != null)
          stream.fetch(fetch);
        if (handler != null)
          stream.handler(handler);

        _handler.handle(Future.succeededFuture(stream));
      }
    };
  }
}
