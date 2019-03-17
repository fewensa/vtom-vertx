package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;

public interface Pipeline {

  static Pipeline pipeline(Vertx vertx) {
    return new PipelineImpl(vertx);
  }

  static Pipeline join(List<PipeStep> steps) {
    Pipeline pipeline = null;
    for (PipeStep step : steps) {
      pipeline = step.join();
    }
    return pipeline;
  }

  PipeLifecycle lifecycle();

  <I extends StepIN, O extends StepOUT> Pipeline next(PipeRunnable<I, O> piperunnable);

  PipePromise enqueue();

}
