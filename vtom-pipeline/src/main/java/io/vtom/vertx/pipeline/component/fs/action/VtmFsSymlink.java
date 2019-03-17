package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsSymlink extends AbstractFsAction<VtmFsSymlink> {


  private String existing;

  public VtmFsSymlink(String link) {
    super(link);
  }

  public VtmFsSymlink existing(String existing) {
    this.existing = existing;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().symlink(path(), existing, Pvtk.handleTo(handler));
      }
    };
  }
}
