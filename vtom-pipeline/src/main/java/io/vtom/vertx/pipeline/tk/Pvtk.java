package io.vtom.vertx.pipeline.tk;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public class Pvtk {

  public static <T> Handler<AsyncResult<T>> handleTo(Handler<AsyncResult<Object>> handler) {
    return ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }
      handler.handle(Future.succeededFuture(ar.result()));
    };
  }

}
