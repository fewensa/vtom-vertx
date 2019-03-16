package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsReadSymlink extends AbstractFsAction<VtmFsReadSymlink> {

  public VtmFsReadSymlink(String path) {
    super(path);
  }

  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.readSymlink(path(), Pvtk.handleTo(handler));
      }
    };
  }
}
