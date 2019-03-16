package io.vtom.vertx.pipeline.step;

public interface StepIN {

  StepIN skip(StepSkip stepskip);

  <I extends StepIN> StepOUT out(StepWrapper<I> wrapper);


}
