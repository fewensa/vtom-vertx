package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsCreateFile extends AbstractFsAction<VtmFsCreateFile> {

  private String perms;

  public VtmFsCreateFile(String path) {
    super(path);
  }

  public VtmFsCreateFile perms(String perms) {
    this.perms = perms;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (perms == null) {
          fs.createFile(path(), Pvtk.handleTo(handler));
          return;
        }
        fs.createFile(path(), perms, Pvtk.handleTo(handler));
      }
    };
  }
}
