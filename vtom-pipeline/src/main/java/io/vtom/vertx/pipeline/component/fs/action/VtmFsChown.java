package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsChown extends AbstractFsAction<VtmFsChown> {

  private String user;
  private String group;

  public VtmFsChown(String path) {
    super(path);
  }

  public VtmFsChown user(String user) {
    this.user = user;
    return this;
  }

  public VtmFsChown group(String group) {
    this.group = group;
    return this;
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().chown(path(), user, group, Pvtk.handleTo(handler));
      }
    };
  }
}
