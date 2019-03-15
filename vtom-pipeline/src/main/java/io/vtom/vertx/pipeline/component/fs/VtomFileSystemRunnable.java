package io.vtom.vertx.pipeline.component.fs;

import io.enoa.toolkit.map.Kv;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.fs.action.Fs;
import io.vtom.vertx.pipeline.component.fs.action.VtmFsOut;
import io.vtom.vertx.pipeline.step.StepWrapper;

class VtomFileSystemRunnable implements PipeRunnable<Fs, VtmFsOut> {

  private Vertx vertx;
  private Pipeline pipeline;
  private StepWrapper<? extends Fs> wrapper;
  private Kv shared;

  VtomFileSystemRunnable(Vertx vertx, Pipeline pipeline, StepWrapper<? extends Fs> wrapper, Kv shared) {
    this.vertx = vertx;
    this.pipeline = pipeline;
    this.wrapper = wrapper;
    this.shared = shared;
  }

  @Override
  public StepWrapper<? extends Fs> wrapper() {
    return this.wrapper;
  }

  @Override
  public void call(VtmFsOut stepout, Handler<AsyncResult<Object>> handler) {
    FileSystem fs = this.vertx.fileSystem();
    stepout.execute(fs, handler);
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    handler.handle(Future.succeededFuture());
  }
}
