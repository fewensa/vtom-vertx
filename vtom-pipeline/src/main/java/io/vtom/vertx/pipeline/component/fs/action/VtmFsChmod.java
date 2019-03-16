package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsChmod extends AbstractFsAction<VtmFsChmod> {

  private String perms;

  public VtmFsChmod(String path) {
    super(path);
  }

  public VtmFsChmod perms(String perms) {
    this.perms = perms;
    return this;
  }

  public VtmFsChmodRecursive recursive() {
    return new VtmFsChmodRecursive(super.path(), this.perms);
  }


  @Override
  public VtmFsOut out() {
    return new AbstractVtmFsOut(stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.chmod(path(), perms, Pvtk.handleTo(handler));
      }
    };
  }

}
