package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.step.StepIN;

public interface PipeComponent<T extends StepIN> {

  default PipeStep<T> component() {
    return this.dependency(Pipeline.pipeline());
  }

  PipeStep<T> dependency(Pipeline pipeline);

}
