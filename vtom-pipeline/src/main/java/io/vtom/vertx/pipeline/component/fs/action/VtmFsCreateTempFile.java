package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;
import io.vtom.vertx.pipeline.tk.Pvtk;

public class VtmFsCreateTempFile extends AbstractFsAction<VtmFsCreateTempFile> {

  private String dir;
  private String prefix;
  private String suffix;
  private String perms;

  public VtmFsCreateTempFile() {
    super(null);
  }

  public VtmFsCreateTempFile dir(String dir) {
    this.dir = dir;
    return this;
  }

  public VtmFsCreateTempFile prefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public VtmFsCreateTempFile suffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  public VtmFsCreateTempFile perms(String perms) {
    this.perms = perms;
    return this;
  }

  @Override
  public <I extends StepIN> VtmFsOut out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    return new AbstractVtmFsOut(lifecycle, wrapper, stepskips()) {
      @Override
      public void execute(FileSystem fs, Handler<AsyncResult<Object>> handler) {
        if (prefix != null && dir != null && suffix != null && perms != null) {
          fs.createTempFile(dir, prefix, suffix, perms, Pvtk.handleTo(handler));
          return;
        }
        if (prefix != null && suffix != null && perms != null) {
          fs.createTempFile(prefix, suffix, perms, Pvtk.handleTo(handler));
          return;
        }

        fs.createTempFile(prefix, suffix, Pvtk.handleTo(handler));
      }
    };
  }
}
