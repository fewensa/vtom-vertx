package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ProxyOptions;
import io.vtom.vertx.pipeline.component.web.client.enoa.EoUrl;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;

import java.nio.charset.Charset;
import java.util.Collection;

public interface Client extends StepIN {

  static Client request(HttpMethod method, String url) {
    return new ClientImpl().method(method).url(url);
  }

  default Client ssl() {
    return this.ssl(true);
  }

  Client ssl(boolean ssl);

  default Client alpn() {
    return this.alpn(true);
  }

  Client alpn(boolean alpn);

  default Client trustAll() {
    return this.trustAll(true);
  }

  Client trustAll(boolean trustAll);

  Client options(HttpClientOptions options);

  Client version(HttpVersion version);

  Client method(HttpMethod method);

  Client charset(Charset charset);

  Client host(String host);

  Client port(int port);

  Client url(String url);

  default Client url(EoUrl url) {
    return this.url(url.end());
  }

  default Client traditional() {
    return this.traditional(true);
  }

  Client traditional(boolean traditional);

  default Client encode() {
    return this.encode(true);
  }

  Client encode(boolean encode);

  Client para(String name, Object value);

  Client para(String name, Object... values);

  Client para(String name, Collection values);

//  default Client file(String name, byte[] bytes) {
//    return this.file(name, null, bytes);
//  }
//
//  Client file(String name, String filename, byte[] bytes);

  default Client file(String name, String path) {
    return this.file(name, path, null);
  }

  Client file(String name, String path, String filename);

  Client file(String path);

  default Client raw(String raw) {
    return this.raw(raw, null);
  }

  Client raw(String raw, String contentType);

  Client header(String name, String value);

  Client cookie(String name, String value);

  Client contentType(String contentType);

  Client proxy(ProxyOptions proxy);

  Client binary(byte[] bytes);

  default Client absolute() {
    return this.absolute(true);
  }

  Client absolute(boolean absolute);

  default Client binary(Buffer buffer) {
    return this.binary(buffer.getBytes());
  }

  Client report(IRequestReporter reporter);

  @Override
  Client skip(Handler<Skip> stepskip);

  @Override
  VtmHttpClientOUT out();
}
