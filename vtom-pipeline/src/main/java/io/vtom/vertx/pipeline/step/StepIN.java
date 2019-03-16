package io.vtom.vertx.pipeline.step;

public interface StepIN {

  StepIN skip(StepSkip stepskip);

  StepOUT out();


}
