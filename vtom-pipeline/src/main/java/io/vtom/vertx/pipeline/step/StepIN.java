package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public interface StepIN {

  StepIN skip(Skip skip);

  <I extends StepIN> StepOUT out(PipeLifecycle pipecycle, StepWrapper<I> wrapper);


}
