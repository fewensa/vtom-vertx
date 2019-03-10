package io.vtom.vertx.pipeline.step;

public interface StepIN {

  <I extends StepIN> StepOUT out(StepWrapper<I> wrapper);



}
