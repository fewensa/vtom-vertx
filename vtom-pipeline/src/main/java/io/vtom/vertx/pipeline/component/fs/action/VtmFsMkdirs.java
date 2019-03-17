package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsMkdirs extends AbstractFsAction<VtmFsMkdirs> {

  private String perms;

  public VtmFsMkdirs(String path) {
    super(path);
  }

  public VtmFsMkdirs perms(String perms) {
    this.perms = perms;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        FileSystem fs = vertx.fileSystem();
        if (perms == null) {
          fs.mkdirs(path(), Pvtk.handleTo(handler));
          return;
        }
        fs.mkdirs(path(), perms, Pvtk.handleTo(handler));
      }
    };
  }
}
