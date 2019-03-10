package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.Pipecycle;

public interface Stepstack<T extends StepIN> {

  T stepin(Pipecycle pipecycle);

}
