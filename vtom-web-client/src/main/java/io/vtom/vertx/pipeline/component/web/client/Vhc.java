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

public interface Vhc extends StepIN {

  static Vhc request(HttpMethod method, String url) {
    return new VhcImpl().method(method).url(url);
  }

  default Vhc ssl() {
    return this.ssl(true);
  }

  Vhc ssl(boolean ssl);

  default Vhc alpn() {
    return this.alpn(true);
  }

  Vhc alpn(boolean alpn);

  default Vhc trustAll() {
    return this.trustAll(true);
  }

  Vhc trustAll(boolean trustAll);

  Vhc options(HttpClientOptions options);

  Vhc version(HttpVersion version);

  Vhc method(HttpMethod method);

  Vhc charset(Charset charset);

  Vhc host(String host);

  Vhc port(int port);

  Vhc url(String url);

  default Vhc url(EoUrl url) {
    return this.url(url.end());
  }

  default Vhc traditional() {
    return this.traditional(true);
  }

  Vhc traditional(boolean traditional);

  default Vhc encode() {
    return this.encode(true);
  }

  Vhc encode(boolean encode);

  Vhc para(String name, Object value);

  Vhc para(String name, Object... values);

  Vhc para(String name, Collection values);

//  default Vhc file(String name, byte[] bytes) {
//    return this.file(name, null, bytes);
//  }
//
//  Vhc file(String name, String filename, byte[] bytes);

  default Vhc file(String name, String path) {
    return this.file(name, null, path);
  }

  Vhc file(String name, String filename, String path);

  Vhc file(String path);

  default Vhc raw(String raw) {
    return this.raw(raw, null);
  }

  Vhc raw(String raw, String contentType);

  Vhc header(String name, String value);

  Vhc cookie(String name, String value);

  Vhc contentType(String contentType);

  Vhc proxy(ProxyOptions proxy);

  Vhc binary(byte[] bytes);

  default Vhc absolute() {
    return this.absolute(true);
  }

  Vhc absolute(boolean absolute);

  default Vhc binary(Buffer buffer) {
    return this.binary(buffer.getBytes());
  }

  @Override
  Vhc skip(Handler<Skip> stepskip);

  @Override
  VtmHttpClientOUT out();
}
