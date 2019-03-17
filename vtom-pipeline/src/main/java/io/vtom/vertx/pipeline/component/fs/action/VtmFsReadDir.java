package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsReadDir extends AbstractFsAction<VtmFsReadDir> {

  private String filter;

  public VtmFsReadDir(String path) {
    super(path);
  }


  public VtmFsReadDir filter(String filter) {
    this.filter = filter;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        FileSystem fs = vertx.fileSystem();
        if (filter == null) {
          fs.readDir(path(), Pvtk.handleTo(handler));
          return;
        }

        fs.readDir(path(), filter, Pvtk.handleTo(handler));
      }
    };
  }
}
