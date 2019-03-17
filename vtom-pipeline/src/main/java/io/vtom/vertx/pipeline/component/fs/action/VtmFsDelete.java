package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsDelete extends AbstractFsAction<VtmFsDelete> {

  private boolean recursive;

  public VtmFsDelete(String path) {
    super(path);
  }

  public VtmFsDelete recursive() {
    return this.recursive(true);
  }

  public VtmFsDelete recursive(boolean recursive) {
    this.recursive = recursive;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        FileSystem fs = vertx.fileSystem();
        if (recursive) {
          fs.deleteRecursive(path(), recursive, Pvtk.handleTo(handler));
          return;
        }

        fs.delete(path(), Pvtk.handleTo(handler));
      }
    };
  }
}
