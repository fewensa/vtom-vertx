//package io.vtom.vertx.pipeline.component.web.client;
//
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Handler;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.http.HttpClientResponse;
//import io.vertx.core.http.HttpMethod;
//import io.vertx.ext.web.client.HttpRequest;
//import io.vtom.vertx.pipeline.component.web.client.enoa.HttpPara;
//import io.vtom.vertx.pipeline.tk.Pvtk;
//
//import java.util.Set;
//
//class _VtmParasRequest implements VtmRequest {
//
//  private HttpRequest<Buffer> request;
//  private VtmHttpClientOUT stepout;
//
//
//  _VtmParasRequest(HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
//    this.request = request;
//    this.stepout = stepout;
//  }
//
//
//  @Override
//  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {
//    HttpMethod method = stepout.method();
//    if (method == HttpMethod.GET) {
//      this.request.send(Pvtk.handleTo(handler));
//      return;
//    }
//
//    Set<HttpPara> paras = stepout.paras();
//    StringBuilder body = new StringBuilder();
//    int i = 0;
//    for (HttpPara para : paras) {
//      body.append(stepout.encode() ? para.output(stepout.traditional(), stepout.charset()) : para.output(stepout.traditional()));
//      if (i + 1 < paras.size())
//        body.append("&");
//      i += 1;
//    }
//
//    this.request.sendBuffer(Buffer.buffer(body.toString()), Pvtk.handleTo(handler));
//  }
//}
