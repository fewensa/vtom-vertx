package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsProps extends AbstractFsAction<VtmFsProps> {

  public VtmFsProps(String path) {
    super(path);
  }

  @Override
  public <I extends StepIN> VtmFsOut out(StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        fs.props(path(), Pvtk.handleTo(handler));
      }
    };
  }

}
