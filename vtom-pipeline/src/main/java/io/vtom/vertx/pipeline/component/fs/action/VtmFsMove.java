package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsMove extends AbstractFsAction<VtmFsMove> {

  private String to;
  private CopyOptions options;

  public VtmFsMove(String from) {
    super(from);
  }

  public VtmFsMove to(String to) {
    this.to = to;
    return this;
  }

  public VtmFsMove options(CopyOptions options) {
    this.options = options;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        FileSystem fs = vertx.fileSystem();
        if (options == null) {
          fs.move(path(), to, Pvtk.handleTo(handler));
          return;
        }
        fs.move(path(), to, options, Pvtk.handleTo(handler));
      }
    };
  }
}
