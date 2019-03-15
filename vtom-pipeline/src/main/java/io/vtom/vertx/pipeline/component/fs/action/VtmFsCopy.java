package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.CopyOptions;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsCopy extends AbstractFsAction<VtmFsCopy> {

  private String to;
  private CopyOptions options;
  private boolean recursive;

  public VtmFsCopy(String from) {
    super(from);
  }

  public VtmFsCopy to(String to) {
    this.to = to;
    return this;
  }

  public VtmFsCopy options(CopyOptions options) {
    this.options = options;
    return this;
  }

  public VtmFsCopy recursive() {
    return this.recursive(true);
  }

  public VtmFsCopy recursive(boolean recursive) {
    this.recursive = recursive;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper) {
      @Override
      public Skip skip() {
        return _skip();
      }

      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (recursive) {
          fs.copyRecursive(path(), to, recursive, Pvtk.handleTo(handler));
          return;
        }

        if (options == null) {
          fs.copy(path(), to, Pvtk.handleTo(handler));
          return;
        }

        fs.copy(path(), to, options, Pvtk.handleTo(handler));
      }
    };
  }
}
