package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;

 class __VtmBinaryRequest implements VtmRequest {

  private HttpClientRequest request;
  private VtmHttpClientOUT stepout;

  __VtmBinaryRequest(HttpClientRequest request, VtmHttpClientOUT stepout) {
    this.request = request;
    this.stepout = stepout;
  }

  @Override
  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {
    this.request.handler(response -> handler.handle(Future.succeededFuture(response)))
      .exceptionHandler(thr -> handler.handle(Future.failedFuture(thr)))
      .end(Buffer.buffer(this.stepout.binary()));
  }
}
