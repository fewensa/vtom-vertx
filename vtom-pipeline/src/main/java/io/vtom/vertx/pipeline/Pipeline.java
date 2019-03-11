package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.scope.Scope;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;

public interface Pipeline {

  static Pipeline pipeline() {
    return pipeline(Scope.context());
  }

  static Pipeline pipeline(ScopeContext context) {
    return new PipelineImpl(context);
  }

  static Pipeline join(List<PipeStep> steps) {
    Pipeline pipeline = null;
    for (PipeStep step : steps) {
      pipeline = step.join();
    }
    return pipeline;
  }

  PipeLifecycle cycle();

  <I extends StepIN, O extends StepOUT> Pipeline next(PipeRunnable<I, O> piperunnable);

  PipePromise enqueue();

}
