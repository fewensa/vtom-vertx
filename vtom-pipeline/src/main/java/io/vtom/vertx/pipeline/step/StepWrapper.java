package io.vtom.vertx.pipeline.step;

public interface StepWrapper<T extends StepIN> extends StepOUT {

  StepStack<T> stepstack();

}
