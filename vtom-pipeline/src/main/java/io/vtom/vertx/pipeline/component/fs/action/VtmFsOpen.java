package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsOpen extends AbstractFsAction<VtmFsOpen> {

  private OpenOptions options;

  public VtmFsOpen(String path) {
    super(path);
  }

  public VtmFsOpen options(OpenOptions options) {
    this.options = options;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().open(path(), options, Pvtk.handleTo(handler));
      }
    };
  }
}
