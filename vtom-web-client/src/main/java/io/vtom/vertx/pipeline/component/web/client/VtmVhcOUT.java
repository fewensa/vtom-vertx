package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpVersion;
import io.vertx.core.net.ProxyOptions;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpCookie;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpFormData;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpHeader;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpPara;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

interface VtmVhcOUT {

  HttpClientOptions options();

  HttpVersion version();

  boolean ssl();

  boolean alpn();

  boolean trustAll();

  HttpMethod method();

  Charset charset();

  String host();

  int port();

  String url();

  boolean traditional();

  boolean encode();

  String raw();

  String contentType();

  ProxyOptions proxy();

  byte[] binary();

  Set<HttpPara> paras();

  Set<HttpHeader> headers();

  List<HttpFormData> formDatas();

  Set<HttpCookie> cookies();

  boolean absolute();

  IRequestReporter reporter();

}
