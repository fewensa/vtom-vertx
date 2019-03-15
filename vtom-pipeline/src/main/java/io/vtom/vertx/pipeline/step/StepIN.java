package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;

public interface StepIN {

  StepIN skip(StepSkip stepskip);

  <I extends StepIN> StepOUT out(PipeLifecycle pipecycle, StepWrapper<I> wrapper);


}
