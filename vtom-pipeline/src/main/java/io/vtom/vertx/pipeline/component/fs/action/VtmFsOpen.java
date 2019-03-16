package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsOpen extends AbstractFsAction<VtmFsOpen> {

  private OpenOptions options;

  public VtmFsOpen(String path) {
    super(path);
  }

  public VtmFsOpen options(OpenOptions options) {
    this.options = options;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.open(path(), options, Pvtk.handleTo(handler));
      }
    };
  }
}
