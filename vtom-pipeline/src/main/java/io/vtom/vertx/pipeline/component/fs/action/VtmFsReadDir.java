package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsReadDir extends AbstractFsAction<VtmFsReadDir> {

  private String filter;

  public VtmFsReadDir(String path) {
    super(path);
  }


  public VtmFsReadDir filter(String filter) {
    this.filter = filter;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(lifecycle, wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (filter == null) {
          fs.readDir(path(), Pvtk.handleTo(handler));
          return;
        }

        fs.readDir(path(), filter, Pvtk.handleTo(handler));
      }
    };
  }
}
