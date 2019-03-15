package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;

public interface StepStack<T extends StepIN> {

  T stepin(PipeLifecycle lifecycle);

}
