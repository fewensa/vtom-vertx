package io.vtom.vertx.pipeline.component.http.client;

import io.enoa.toolkit.EoConst;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vtom.vertx.pipeline.component.http.client.enoa.HttpCookie;
import io.vtom.vertx.pipeline.component.http.client.enoa.HttpFormData;
import io.vtom.vertx.pipeline.component.http.client.enoa.HttpHeader;
import io.vtom.vertx.pipeline.component.http.client.enoa.HttpPara;
import io.vtom.vertx.pipeline.component.http.client.enoa.proxy.HttpProxy;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class VhcImpl implements Vhc {

  private HttpMethod method;
  private Charset charset;
  private String url;
  private boolean traditional;
  private boolean encode;
  private String raw;
  private String contentType;
  private HttpProxy proxy;
  private byte[] binary;
  private Set<HttpPara> paras;
  private Set<HttpHeader> headers;
  private List<HttpFormData> formDatas;
  private Set<HttpCookie> cookies;
  private boolean absolute;
  private List<StepSkip> stepskips;

  VhcImpl() {
    this.method = HttpMethod.GET;
    this.absolute = true;
    this.encode = false;
    this.traditional = false;
    this.charset = EoConst.CHARSET;
    this.headers = new HashSet<>();
    this.headers.add(new HttpHeader("User-Agent", "Mozilla/5.0 Vtom-vertx/1.0-SNAPSHOT HttpHelper/4.0 VtomHttpClient/1.0"));
  }

  private Object[] toArr(Object value) {
    int len = Array.getLength(value);
    Object[] ret = new Object[len];
    for (int i = 0; i < len; ++i) {
      ret[i] = Array.get(value, i);
    }
    return ret;
  }

  @Override
  public Vhc method(HttpMethod method) {
    this.method = method;
    return this;
  }

  @Override
  public Vhc charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public Vhc url(String url) {
    this.url = url;
    return this;
  }

  @Override
  public Vhc traditional(boolean traditional) {
    this.traditional = traditional;
    return this;
  }

  @Override
  public Vhc encode(boolean encode) {
    this.encode = encode;
    return this;
  }

  private Vhc para(String name, Object value, boolean array) {
    if (this.paras == null)
      this.paras = new HashSet<>();
    if (value instanceof Path) {
      return this.file(name, value.toString());
    }

    Set<HttpPara> itm = new HashSet<>();
    Iterator<HttpPara> iterator = this.paras.iterator();
    while (iterator.hasNext()) {
      HttpPara para = iterator.next();
      if (!para.name().equals(name))
        continue;
      // 当前添加的参数之前已添加, 将是否数组标志改为 true
      array = true;
      // 若之前添加的参数标记已是数组, 不进行操作
      if (para.array())
        continue;
      // 否则改变之前添加的参数标记为数组
      iterator.remove();
      itm.add(new HttpPara(para.name(), para.value(), true));
    }
    itm.add(new HttpPara(name, String.valueOf(value), array));
    this.paras.addAll(itm);
    itm.clear();
    return this;
  }

  @Override
  public Vhc para(String name, Object value) {
    if (name == null)
      throw new IllegalArgumentException("name == null");
    if (value == null)
      throw new IllegalArgumentException("value == null => name: " + name);
    if (value.getClass().isArray()) {
      return this.para(name, this.toArr(value));
    }
    return this.para(name, value.toString(), false);
  }

  @Override
  public Vhc para(String name, Object... values) {
    if (name == null)
      throw new IllegalArgumentException("name == null");
    if (values == null)
      throw new IllegalArgumentException("values == null => name: " + name);
    for (Object value : values) {
      this.para(name, value, false);
    }
    return this;
  }

  @Override
  public Vhc para(String name, Collection values) {
    if (name == null)
      throw new IllegalArgumentException("name == null");
    if (values == null)
      throw new IllegalArgumentException("values == null");
    values.forEach(c -> this.para(name, c, false));
    return this;
  }

  @Override
  public Vhc file(String name, String filename, byte[] bytes) {
    if (name == null)
      throw new IllegalArgumentException("name == null");
    if (this.formDatas == null)
      this.formDatas = new ArrayList<>();
    this.formDatas.add(new HttpFormData(name, filename == null ? name : filename, bytes));
    return this;
  }

  @Override
  public Vhc file(String name, String filename, String path) {
    if (path == null)
      throw new IllegalArgumentException("path == null");
    if (this.formDatas == null)
      this.formDatas = new ArrayList<>();
    this.formDatas.add(new HttpFormData(name, filename, path));
    return this;
  }

  @Override
  public Vhc file(String path) {
    return this.para(null, path);
  }

  @Override
  public Vhc raw(String raw, String contentType) {
    if (raw == null)
      throw new IllegalArgumentException("raw == null");
    this.raw = raw;
    if (contentType != null)
      this.contentType = contentType;
    return this;
  }

  @Override
  public Vhc header(String name, String value) {
    if ("content-type".equals(name.toLowerCase()))
      this.contentType(value);

    HttpHeader header = new HttpHeader(name, value);
    HttpHeader rmHeader = null;
    for (HttpHeader ahr : this.headers) {
      if (!ahr.name().equalsIgnoreCase(header.name())) {
        continue;
      }
      rmHeader = ahr;
      if (header.name().equalsIgnoreCase("cookie"))
        header = new HttpHeader(ahr.name(), ahr.value().concat("; ").concat(header.value()));
      break;
    }
    if (rmHeader != null)
      this.headers.remove(rmHeader);
    this.headers.add(header);
    return this;
  }

  @Override
  public Vhc cookie(String name, String value) {
    if (name == null)
      throw new IllegalArgumentException("name == null");
    if (value == null)
      throw new IllegalArgumentException("value == null");
    if (this.cookies == null)
      this.cookies = new HashSet<>();
    this.cookies.add(new HttpCookie.Builder().name(name).value(value).build());
    return this;
  }

  @Override
  public Vhc contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  @Override
  public Vhc proxy(HttpProxy proxy) {
    if (proxy == null)
      throw new IllegalArgumentException("proxy == null");
    this.proxy = proxy;
    return this;
  }

  @Override
  public Vhc binary(byte[] bytes) {
    if (bytes == null)
      throw new IllegalArgumentException("bytes == null");
    this.binary = bytes;
    return this;
  }

  @Override
  public Vhc absolute(boolean absolute) {
    this.absolute = absolute;
    return this;
  }

  @Override
  public Vhc skip(StepSkip stepskip) {
    if (this.stepskips == null)
      this.stepskips = new ArrayList<>();
    this.stepskips.add(stepskip);
    return this;
  }

  @Override
  public VtmHttpClientOUT out() {
    return null;
  }
}
