package io.vtom.vertx.pipeline.step;

// extends StepOUT
public interface StepWrapper<T extends StepIN> {

  String id();

  int ord();

  int after();

  StepStack<T> stepstack();

}
