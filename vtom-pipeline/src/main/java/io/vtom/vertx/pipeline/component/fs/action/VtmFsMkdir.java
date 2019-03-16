package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsMkdir extends AbstractFsAction<VtmFsMkdir> {

  private String perms;

  public VtmFsMkdir(String path) {
    super(path);
  }

  public VtmFsMkdir perms(String perms) {
    this.perms = perms;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (perms == null) {
          fs.mkdir(path(), Pvtk.handleTo(handler));
          return;
        }
        fs.mkdir(path(), perms, Pvtk.handleTo(handler));
      }
    };
  }
}
