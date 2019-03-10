package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.scope.Scope;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;

public interface Pipeline {

  static Pipeline pipeline(Vertx vertx) {
    return pipeline(vertx, Scope.context());
  }

  static Pipeline pipeline(Vertx vertx, ScopeContext context) {
    return new PipelineImpl(vertx, context);
  }

  static Pipeline join(List<Pipestep> steps) {
    Pipeline pipeline = null;
    for (Pipestep step : steps) {
      pipeline = step.join();
    }
    return pipeline;
  }

  Pipecycle cycle();

  <I extends StepIN, O extends StepOUT> Pipeline next(Piperunnable<I, O> piperunnable);

  Pipepromise enqueue();

}
