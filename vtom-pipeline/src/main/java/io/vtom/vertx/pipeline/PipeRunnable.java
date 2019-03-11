package io.vtom.vertx.pipeline;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

public interface PipeRunnable<I extends StepIN, O extends StepOUT> {

  StepWrapper<I> wrapper();

  void call(O stepout, Handler<AsyncResult<Object>> handler);

  void release(boolean ok, Handler<AsyncResult<Void>> handler);

}
