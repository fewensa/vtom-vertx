package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsDelete extends AbstractFsAction<VtmFsDelete> {

  private boolean recursive;

  public VtmFsDelete(String path) {
    super(path);
  }

  public VtmFsDelete recursive() {
    return this.recursive(true);
  }

  public VtmFsDelete recursive(boolean recursive) {
    this.recursive = recursive;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(lifecycle, wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (recursive) {
          fs.deleteRecursive(path(), recursive, Pvtk.handleTo(handler));
          return;
        }

        fs.delete(path(), Pvtk.handleTo(handler));
      }
    };
  }
}
