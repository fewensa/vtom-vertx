package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepIN;

public interface Pipestep<T extends StepIN> {

  Pipestep<T> step(Step<T> step);

  Pipeline join();

}
