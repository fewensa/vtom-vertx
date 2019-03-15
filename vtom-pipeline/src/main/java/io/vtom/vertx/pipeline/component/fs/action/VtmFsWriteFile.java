package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;


public class VtmFsWriteFile extends AbstractFsAction<VtmFsWriteFile> {

  private Buffer buffer;

  public VtmFsWriteFile(String path) {
    super(path);
  }

  public VtmFsWriteFile buffer(Buffer buffer) {
    this.buffer = buffer;
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
        fs.writeFile(path(), buffer, Pvtk.handleTo(handler));
      }
    };
  }
}
