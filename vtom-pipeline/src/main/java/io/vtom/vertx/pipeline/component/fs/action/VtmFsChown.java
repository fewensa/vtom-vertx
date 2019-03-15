package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsChown extends AbstractFsAction<VtmFsChown> {

  private String user;
  private String group;

  public VtmFsChown(String path) {
    super(path);
  }

  public VtmFsChown user(String user) {
    this.user = user;
    return this;
  }

  public VtmFsChown group(String group) {
    this.group = group;
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
        fs.chown(path(), user, group, Pvtk.handleTo(handler));
      }
    };
  }
}
