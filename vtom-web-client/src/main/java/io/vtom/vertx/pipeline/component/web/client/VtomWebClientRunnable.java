package io.vtom.vertx.pipeline.component.web.client;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.enoa.toolkit.text.TextKit;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ProxyOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.component.web.client.enoa.*;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.tk.Pvtk;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class VtomWebClientRunnable implements PipeRunnable<Vhc, VtmHttpClientOUT> {

  private Vertx vertx;
  private Step<? extends Vhc> step;
  private Kv shared;
  private WebClient client;

  VtomWebClientRunnable(Vertx vertx, Step<? extends Vhc> step, Kv shared) {
    this.vertx = vertx;
    this.step = step;
    this.shared = shared;
  }

  @Override
  public Step<? extends Vhc> step() {
    return this.step;
  }

  @Override
  public void call(VtmHttpClientOUT stepout, Handler<AsyncResult<Object>> handler) {
    this.client = WebClient.create(this.vertx, this.options(stepout));
    HttpRequest<Buffer> request = this.buildRequest(stepout);
    this.buildHeader(request, stepout);
    VtmRequest vtmrequest = this.vtmRequest(request, stepout);
    vtmrequest.request(Pvtk.handleTo(handler));
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    this.client.close();
    handler.handle(Future.succeededFuture());
  }


  private WebClientOptions options(VtmHttpClientOUT stepout) {
    HttpClientOptions _opts = stepout.options();
    HttpClientOptions options = _opts == null ? new HttpClientOptions() : _opts;

    HttpVersion version = stepout.version();
    if (version != null) {
      options.setProtocolVersion(version);
    }

    ProxyOptions proxy = stepout.proxy();
    if (proxy != null) {
      options.setProxyOptions(proxy);
    }

    if (stepout.ssl())
      options.setSsl(true);

    if (stepout.alpn())
      options.setUseAlpn(true);

    if (stepout.trustAll())
      options.setTrustAll(true);

    return new WebClientOptions(options);
  }

  private String url(VtmHttpClientOUT stepout) {
    EoUrl eourl = EoUrl.with(stepout.url())
      .charset(stepout.charset());
    if (stepout.traditional())
      eourl.traditional();
    if (stepout.encode())
      eourl.encode();

    HttpMethod method = stepout.method();

    if (method == HttpMethod.GET || stepout.raw() != null || stepout.binary() != null) {
      Set<HttpPara> paras = stepout.paras();
      if (CollectionKit.isEmpty(paras))
        return eourl.end();

      paras.forEach(para -> eourl.para(
        stepout.encode() ? SafeURL.encode(para.name(), stepout.charset()) : para.name(),
        stepout.encode() ? SafeURL.encode(para.value(), stepout.charset()) : para.value()
      ));
      return eourl.end();
    }

    return eourl.end();
  }

  private HttpRequest<Buffer> buildRequest(VtmHttpClientOUT stepout) {
    HttpMethod method = stepout.method();
    String url = this.url(stepout);
    if (stepout.absolute()) {
      return this.client.requestAbs(method, url);
    }

    String host = stepout.host();
    int port = stepout.port();
    if (TextKit.blankn(host) && port != -1) {
      return this.client.request(method, port, host, url);
    }
    if (TextKit.blankn(host) && port == -1) {
      return this.client.request(method, host, url);
    }

    return this.client.requestAbs(method, url);
  }

  private void buildHeader(HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
    this.fillCookie(request, stepout);
    Set<HttpHeader> headers = stepout.headers();
    if (CollectionKit.isEmpty(headers))
      return;

    headers.forEach(header -> request.putHeader(header.name(), header.value()));
    if (TextKit.blankn(stepout.contentType()))
      request.putHeader("Content-Type", stepout.contentType());
  }

  private void fillCookie(HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
    Set<HttpCookie> cookies = stepout.cookies();
    if (CollectionKit.isEmpty(cookies))
      return;
    String ck = String.join("; ", cookies.stream().map(c -> c.name().concat("=").concat(c.value())).collect(Collectors.toSet()));
    MultiMap headmap = request.headers();
    String _rdck = headmap.get("cookie");
    if (TextKit.blanky(_rdck)) {
      request.putHeader("cookie", ck);
      return;
    }
    ck = _rdck.concat("; ").concat(ck);
    request.putHeader("cookie", ck);
  }


  private VtmRequest vtmRequest(HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
    Set<HttpPara> paras = stepout.paras();
    String raw = stepout.raw();
    List<HttpFormData> formDatas = stepout.formDatas();
    byte[] binary = stepout.binary();

    if (paras != null && (raw == null && formDatas == null && binary == null)) {
      return new _VtmParasRequest(request, stepout);
    }

    if (raw != null && (formDatas == null && binary == null)) {
      return new _VtmRawRequest(request, stepout);
    }

    if (formDatas != null && (raw == null && binary == null)) {
      return new _VtmFormDataRequest(this.vertx, request, stepout);
    }

    if (binary != null && (raw == null && formDatas == null)) {
      return new _VtmBinaryRequest(request, stepout);
    }

    // get request
    if (paras == null && raw == null && formDatas == null && binary == null) {
      return new _VtmGetRequest(request, stepout);
    }

    throw new IllegalArgumentException("Multiple request types can not exist at the same time.");
  }


}
