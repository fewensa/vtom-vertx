package io.vtom.vertx.pipeline.component.web.client;

import io.enoa.toolkit.text.TextKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.multipart.MultipartForm;
import io.vtom.vertx.pipeline.component.web.client.enoa.HttpFormData;
import io.vtom.vertx.pipeline.tk.Pvtk;

import java.util.List;

class _VtmFormDataRequest implements VtmRequest {


  private Vertx vertx;
  private HttpRequest<Buffer> request;
  private VtmHttpClientOUT stepout;


  _VtmFormDataRequest(Vertx vertx, HttpRequest<Buffer> request, VtmHttpClientOUT stepout) {
    this.vertx = vertx;
    this.request = request;
    this.stepout = stepout;
  }

  @Override
  public void request(Handler<AsyncResult<HttpClientResponse>> handler) {
//    this.request.sendMultipartForm();
    MultipartForm mf = MultipartForm.create();
    List<HttpFormData> formDatas = this.stepout.formDatas();
    formDatas.forEach(data -> {
      switch (data.type()) {
        case TEXT:
          mf.attribute(data.name(), data.text());
          break;
        case BINARY:
          // fixme enoa binary file use byte[]
//          mf.binaryFileUpload(data.name(), data.filename(), data.binary(), null);
          break;
        case FILE:
          mf.textFileUpload(data.name(), TextKit.blanky(data.filename()) ? data.name() : data.filename(), data.file(), "text/plain");
          break;
      }
    });


    this.request.sendMultipartForm(mf, Pvtk.handleTo(handler));
  }

}
