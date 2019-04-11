package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpPara;

import java.util.Set;

class __VtmParasRequest implements VtmRequest {

  private HttpClientRequest request;
  private VtmHttpClientOUT stepout;

  __VtmParasRequest(HttpClientRequest request, VtmHttpClientOUT stepout) {
    this.request = request;
    this.stepout = stepout;
  }

  @Override
  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {
    HttpMethod method = this.stepout.method();
    if (method == HttpMethod.GET) {
      this.request
        .handler(response -> handler.handle(Future.succeededFuture(response)))
        .exceptionHandler(thr -> handler.handle(Future.failedFuture(thr)))
        .end();
      return;
    }

    Set<HttpPara> paras = stepout.paras();
    StringBuilder body = new StringBuilder();
    int i = 0;
    for (HttpPara para : paras) {
      body.append(stepout.encode() ? para.output(stepout.traditional(), stepout.charset()) : para.output(stepout.traditional()));
      if (i + 1 < paras.size())
        body.append("&");
      i += 1;
    }
    this.request
      .handler(response -> handler.handle(Future.succeededFuture(response)))
      .exceptionHandler(thr -> handler.handle(Future.failedFuture(thr)))
      .end(body.toString(), this.stepout.charset().toString());
  }
}
