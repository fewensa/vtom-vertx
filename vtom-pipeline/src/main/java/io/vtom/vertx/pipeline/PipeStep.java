package io.vtom.vertx.pipeline;

import io.enoa.toolkit.digest.UUIDKit;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.Stepstack;

public interface PipeStep<T extends StepIN> {

  default PipeStep<T> step(Stepstack<T> stepstack) {
    return this.step(Step.with(stepstack));
  }

  PipeStep<T> step(Step<? extends T> step);

  default Pipeline join() {
    return this.join(UUIDKit.next(false));
  }

  Pipeline join(String id);

}
