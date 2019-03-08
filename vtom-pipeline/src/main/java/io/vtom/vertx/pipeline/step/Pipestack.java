package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.Pipecycle;

public interface Pipestack<T extends StepIN> {

  T back(Pipecycle pipecycle);

}
