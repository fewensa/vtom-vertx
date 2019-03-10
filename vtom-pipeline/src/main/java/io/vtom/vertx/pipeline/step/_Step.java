package io.vtom.vertx.pipeline.step;

interface _Step<T extends StepIN> {

  StepWrapper<T> _wrapper();

}
