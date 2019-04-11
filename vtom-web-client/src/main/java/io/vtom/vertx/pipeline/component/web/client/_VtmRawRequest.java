//package io.vtom.vertx.pipeline.component.web.client;
//
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Handler;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.http.HttpClientResponse;
//import io.vertx.ext.web.client.HttpRequest;
//import io.vtom.vertx.pipeline.tk.Pvtk;
//
//class _VtmRawRequest implements VtmRequest {
//
//
//  private HttpRequest<Buffer> request;
//  private VtmHttpClientOUT stepout;
//
//  _VtmRawRequest(HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
//    this.request = request;
//    this.stepout = stepout;
//  }
//
//  @Override
//  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {
//    this.request.sendBuffer(Buffer.buffer(this.stepout.raw()), Pvtk.handleTo(handler));
//  }
//}
