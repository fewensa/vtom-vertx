package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsMkdirs extends AbstractFsAction<VtmFsMkdirs> {

  private String perms;

  public VtmFsMkdirs(String path) {
    super(path);
  }

  public VtmFsMkdirs perms(String perms) {
    this.perms = perms;
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
        if (perms == null) {
          fs.mkdirs(path(), Pvtk.handleTo(handler));
          return;
        }
        fs.mkdirs(path(), perms, Pvtk.handleTo(handler));
      }
    };
  }
}
