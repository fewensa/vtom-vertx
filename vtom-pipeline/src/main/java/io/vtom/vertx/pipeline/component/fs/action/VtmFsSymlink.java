package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
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
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.symlink(path(), existing, Pvtk.handleTo(handler));
      }
    };
  }
}
