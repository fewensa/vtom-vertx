package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsFFSProps extends AbstractFsAction<VtmFsFFSProps> {
  public VtmFsFFSProps(String path) {
    super(path);
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(Vertx vertx, Handler<AsyncResult<Object>> handler) {
        vertx.fileSystem().fsProps(path(), Pvtk.handleTo(handler));
      }
    };
  }
}
