package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsChmodRecursive extends AbstractFsAction<VtmFsChmodRecursive> {

  private String perms;
  private String dirPerms;

  public VtmFsChmodRecursive(String path, String perms) {
    super(path);
    this.perms = perms;
  }

  public VtmFsChmodRecursive dirPerms(String dirPerms) {
    this.dirPerms = dirPerms;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.chmodRecursive(path(), perms, dirPerms, Pvtk.handleTo(handler));
      }
    };
  }
}
