package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.PipeLifecycle;

public interface Stepstack<T extends StepIN> {

  T stepin(PipeLifecycle pipecycle);

}
