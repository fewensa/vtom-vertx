package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.step.StepIN;

public interface PipeComponent<T extends StepIN> {

  PipeStep<T> dependency(Pipeline pipeline);

}
