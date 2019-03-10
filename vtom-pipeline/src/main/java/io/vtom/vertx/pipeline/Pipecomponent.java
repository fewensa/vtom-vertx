package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.step.StepIN;

public interface Pipecomponent<T extends StepIN> {

  Pipestep<T> dependency(Pipeline pipeline);

}
