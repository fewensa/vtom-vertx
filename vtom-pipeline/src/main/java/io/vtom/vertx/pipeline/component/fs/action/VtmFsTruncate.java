package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsTruncate extends AbstractFsAction<VtmFsTruncate> {

  private long len;

  public VtmFsTruncate(String path) {
    super(path);
  }

  public VtmFsTruncate len(long len) {
    this.len = len;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().truncate(path(), len, Pvtk.handleTo(handler));
      }
    };
  }

}
