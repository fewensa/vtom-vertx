package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.step.StepIN;

public interface PipeComponent<T extends StepIN> {

  default PipeStep<T> dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  PipeStep<T> dependency(Pipeline pipeline);

}
