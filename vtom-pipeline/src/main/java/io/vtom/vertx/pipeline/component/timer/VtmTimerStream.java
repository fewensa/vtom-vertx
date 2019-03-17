package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.*;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.concurrent.atomic.AtomicBoolean;

public class VtmTimerStream extends AbstractTimer<VtmTimerStream> {


  private long delay;
  private Handler<Throwable> exceptionHandler;
  private Handler<Long> handler;
  private Long fetch;
  private Handler<Void> endHandler;

  public VtmTimerStream(long delay) {
    this.delay = delay;
  }

  public VtmTimerStream exceptionHandler(Handler<Throwable> exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
    return this;
  }

  public VtmTimerStream handler(Handler<Long> handler) {
    this.handler = handler;
    return this;
  }

  public VtmTimerStream fetch(Long fetch) {
    this.fetch = fetch;
    return this;
  }

  public VtmTimerStream endHandler(Handler<Void> endHandler) {
    this.endHandler = endHandler;
    return this;
  }

  @Override
  public StepOUT out() {
    return new VtmTimerOut(stepskips()) {
      @Override
      protected void execute(Vertx vertx, Handler<AsyncResult<Object>> _handler) {

        TimeoutStream stream = vertx.timerStream(delay);
        if (exceptionHandler != null)
          stream.exceptionHandler(exceptionHandler);
        if (endHandler != null)
          stream.endHandler(endHandler);
        if (fetch != null)
          stream.fetch(fetch);

        AtomicBoolean ab = new AtomicBoolean(false);
        stream.handler(id -> {

          if (!ab.get()) {
            _handler.handle(Future.succeededFuture(stream));
            ab.set(true);
          }

          if (handler != null)
            handler.handle(id);
        });

      }
    };
  }
}
