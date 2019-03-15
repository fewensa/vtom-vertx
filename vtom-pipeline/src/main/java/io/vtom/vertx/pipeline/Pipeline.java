package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;

public interface Pipeline {

  static Pipeline pipeline() {
    return new PipelineImpl(Scope.scope());
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
