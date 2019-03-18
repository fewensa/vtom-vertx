package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientResponse;

interface VtmRequest {


  void request(Handler<AsyncResult<HttpClientResponse>> handler);

}
