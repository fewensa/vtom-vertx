package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsCreateTempDirectory extends AbstractFsAction<VtmFsCreateTempDirectory> {

  private String dir;
  private String perms;

  public VtmFsCreateTempDirectory(String prefix) {
    super(prefix);
  }

  public VtmFsCreateTempDirectory dir(String dir) {
    this.dir = dir;
    return this;
  }

  public VtmFsCreateTempDirectory perms(String perms) {
    this.perms = perms;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        String prefix = path();
        if (dir != null && prefix != null && perms != null) {
          fs.createTempDirectory(dir, prefix, perms, Pvtk.handleTo(handler));
          return;
        }
        if (prefix != null && perms != null) {
          fs.createTempDirectory(prefix, perms, Pvtk.handleTo(handler));
          return;
        }
        fs.createTempDirectory(prefix, Pvtk.handleTo(handler));
      }
    };
  }
}
