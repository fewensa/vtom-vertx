package io.vtom.vertx.pipeline.component.web.client;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.streams.Pump;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpFormData;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpPara;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class __VtmFormDataRequest implements VtmRequest {


  private static final String HYPHENS = "---------------------------";
  private static final String DISPOSITION_PREFIX = "--";
  private static final String DISPOSITION_END = "\r\n";

  private Vertx vertx;
  private HttpClientRequest request;
  private VtmHttpClientOUT stepout;
  private String boundary;
  private String charset;

  __VtmFormDataRequest(Vertx vertx, HttpClientRequest request, VtmHttpClientOUT stepout) {
    this.vertx = vertx;
    this.request = request;
    this.stepout = stepout;
    this.charset = stepout.charset().toString();
  }

  @Override
  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {


    this.request.handler(response -> handler.handle(Future.succeededFuture(response)))
      .exceptionHandler(thr -> handler.handle(Future.failedFuture(thr)));


    List<HttpFormData> formdatas = this.stepout.formDatas();

    Set<HttpPara> paras = this.stepout.paras();
    if (CollectionKit.notEmpty(paras)) {
      Set<String> fdNames = formdatas.stream().map(HttpFormData::name).collect(Collectors.toSet());
      paras.forEach(para -> {
        if (fdNames.contains(para.name()))
          return;
        formdatas.add(new HttpFormData(para.name(), para.value()));
      });
    }
    this.boundary = this.newBoundary();
    String contentType = "multipart/form-data; boundary=".concat(HYPHENS).concat(this.boundary);

    this.request.setChunked(true);


    this.fillData(formdatas, 0, handler, v -> {

      MultiMap headers = this.request.headers();
      headers.remove("Content-Type");
      headers.add("Content-Type", contentType);

      this.request.end();
    });

  }

  private String endDisposition() {
    return HYPHENS.concat("--").concat(this.boundary).concat("--").concat(DISPOSITION_END);
  }

  private String newBoundary() {
    String uuid = UUID.randomUUID().toString();
    uuid = uuid.replace("-", "");
    return "HttpHelperBoundary".concat(uuid);
  }

  private String createDisposition(String name) {
    StringBuilder ret = new StringBuilder();
    ret.append(DISPOSITION_PREFIX)
      .append(HYPHENS)
      .append(this.boundary)
      .append(DISPOSITION_END);
    ret.append("Content-Disposition: form-data; ")
      .append("name=\"").append(name).append("\"")
      .append(DISPOSITION_END);
    ret.append(DISPOSITION_END);
    return ret.toString();
  }

  private String createDisposition(String name, String filename) {
    return createDisposition(name, filename, null);
  }

  private String createDisposition(String name, String filename, String contentType) {
    StringBuilder ret = new StringBuilder();
    ret.append(DISPOSITION_PREFIX)
      .append(HYPHENS)
      .append(this.boundary)
      .append(DISPOSITION_END);
//    String fileName = path.getFileName().toString();
    ret.append("Content-Disposition: form-data; ")
      .append("name=\"").append(name == null ? "" : name).append("\"; ")
      .append("filename=\"").append(filename).append("\"")
      .append(DISPOSITION_END);

    if (contentType == null) {
      ret.append(DISPOSITION_END);
      return ret.toString();
    }
    ret.append("Content-Type: ").append(contentType).append(DISPOSITION_END);
    ret.append(DISPOSITION_END);
    return ret.toString();
  }


  private String filename(String file) {
    int last = file.lastIndexOf(File.separator);
    return file.substring(last + 1);
  }


  private void fillData(List<HttpFormData> formdatas, int ix, Handler<AsyncResult<HttpClientResponse>> endHandler, Handler<Void> fillHandler) {
    if (ix == formdatas.size()) {
      String dispEnd = this.endDisposition();
      this.request.write(dispEnd);
      fillHandler.handle(null);
      return;
    }
    HttpFormData data = formdatas.get(ix);
    switch (data.type()) {
      case FILE:
        String fileType = null;
        try {
          fileType = Files.probeContentType(Paths.get(data.file()));
        } catch (IOException ignored) {
        }
        FileSystem fs = this.vertx.fileSystem();

        String dispFile = this.createDisposition(data.name(), data.filename() == null ? this.filename(data.file()) : data.filename(), fileType);
        this.request.write(dispFile);
        fs.open(data.file(), new OpenOptions(), ar -> {
          if (ar.failed()) {
            endHandler.handle(Future.failedFuture(ar.cause()));
            return;
          }
          ar.result().handler(buffer -> {
            this.request.write(buffer);
            this.request.write(DISPOSITION_END);
            this.fillData(formdatas, ix + 1, endHandler, fillHandler);
          });
        });
        break;
      case BINARY:
        String dispBinary = this.createDisposition(data.name(), data.filename());
        this.request.write(dispBinary, this.charset)
          .write(Buffer.buffer(data.binary()))
          .write(DISPOSITION_END);
        this.fillData(formdatas, ix + 1, endHandler, fillHandler);
        break;
      case TEXT:
        String dispText = new StringBuilder()
          .append(this.createDisposition(data.name()))
          .append(data.text()).toString();
        this.request.write(dispText, this.charset)
          .write(DISPOSITION_END);
        this.fillData(formdatas, ix + 1, endHandler, fillHandler);
        break;
    }
  }

}
