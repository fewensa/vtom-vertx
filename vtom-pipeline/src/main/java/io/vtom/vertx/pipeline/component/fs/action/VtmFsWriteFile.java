package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vtom.vertx.pipeline.tk.Pvtk;


public class VtmFsWriteFile extends AbstractFsAction<VtmFsWriteFile> {

  private Buffer buffer;

  public VtmFsWriteFile(String path) {
    super(path);
  }

  public VtmFsWriteFile buffer(Buffer buffer) {
    this.buffer = buffer;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().writeFile(path(), buffer, Pvtk.handleTo(handler));
      }
    };
  }
}
