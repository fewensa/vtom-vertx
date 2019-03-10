package io.vtom.vertx.pipeline;

import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

public interface Piperunnable<I extends StepIN, O extends StepOUT> {

  StepWrapper<I> wrapper();

  Pipepromise call(O wr);

}
