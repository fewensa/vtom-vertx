package io.vtom.vertx.pipeline;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface PipeRunnable<I extends StepIN, O extends StepOUT> {

  Step<? extends I> step();

  void call(O stepout, Handler<AsyncResult<Object>> handler);

  void release(boolean ok, Handler<AsyncResult<Void>> handler);

}
