package io.vtom.vertx.pipeline.component.web.client;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.digest.UUIDKit;
import io.enoa.toolkit.text.TextKit;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpFormData;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpHeader;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpPara;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractStepOUT;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class VtmHttpClientOUT extends AbstractStepOUT implements VtmVhcOUT {
  public VtmHttpClientOUT(List<Handler<Skip>> skips) {
    super(skips);
  }

  private static final String HYPHENS = "---------------------------";
  private static final String BOUNDARY = UUIDKit.next(false);

  @Override
  public String toString() {
    StringBuilder _ret = new StringBuilder();
    String url = this.url();
    String uri = url.substring(url.indexOf("//") + 2);
    uri = uri.substring(uri.indexOf("/"));
    _ret.append(this.method().name()).append(" ").append(uri).append(" ").append("HTTP/1.1").append("\r\n");


    Set<HttpHeader> headers = this.headers();
    headers.forEach(header -> _ret.append(header.name()).append(" ").append(header.value()).append("\r\n"));
    _ret.append("\r\n");

    String bodystring = this.bodystring();
    if (TextKit.isBlank(bodystring))
      return _ret.toString();

    _ret.append(bodystring);
    return _ret.toString();
  }


  private String bodystring() {
    if (this.method() == HttpMethod.GET) {
      return null;
    }
    if (this.raw() != null) {
      return this.raw();
    }
    Set<HttpPara> paras = this.paras();

    List<HttpFormData> fmds = this.formDatas();
    if (CollectionKit.notEmpty(fmds)) {
      if (CollectionKit.notEmpty(paras)) {
        Set<String> fdNames = fmds.stream().map(HttpFormData::name).collect(Collectors.toSet());
        paras.forEach(para -> {
          if (fdNames.contains(para.name()))
            return;
          fmds.add(new HttpFormData(para.name(), para.value()));
        });
      }
      StringBuilder builder = new StringBuilder();

      fmds.forEach(fd -> {
        switch (fd.type()) {
          case TEXT:
            builder.append("--").append(HYPHENS).append(BOUNDARY).append("\r\n")
              .append("Content-Disposition: form-data; ")
              .append("name=\"").append(fd.name()).append("\"")
              .append("\r\n\r\n");
            builder.append(fd.text()).append("\r\n");
            break;
          case BINARY:
            builder.append("--").append(HYPHENS).append(BOUNDARY).append("\r\n")
              .append("Content-Disposition: form-data; ")
              .append("name=\"").append(fd.name()).append("\"")
              .append("\r\n\r\n");
            builder.append("+============================================+\r\n");
            builder.append("+ Not support binary data                    +\r\n");
            builder.append("+============================================+\r\n");
            break;
          case FILE:
            builder.append("--").append(HYPHENS).append(BOUNDARY).append("\r\n")
              .append("Content-Disposition: form-data; ")
              .append("name=\"").append(fd.name()).append("\"").append("; ")
              .append("filename=\"").append(fd.filename() == null ? this.filename(fd.file()) : fd.filename()).append("\"")
              .append("\r\n\r\n");
            builder.append("+============================================+\r\n");
            builder.append("+ Not support file content                   +\r\n");
            builder.append("+============================================+\r\n");
            break;
        }
      });
      builder.append(HYPHENS).append("--")
        .append(BOUNDARY)
        .append("--")
        .append("\r\n");
      return builder.toString();
    }
    if (paras != null) {
      return this.parastring(paras);
    }

    return null;
  }


  private String filename(String file) {
    int last = file.lastIndexOf(File.separator);
    return file.substring(last + 1);
  }

  private String parastring(Set<HttpPara> paras) {
    if (paras != null) {
      StringBuilder body = new StringBuilder();
      int i = 0;
      for (HttpPara para : paras) {
        body.append(this.encode() ? para.output(this.traditional(), this.charset()) : para.output(this.traditional()));
        if (i + 1 < paras.size())
          body.append("&");
        i += 1;
      }
      return body.toString();
    }
    return null;
  }

}
