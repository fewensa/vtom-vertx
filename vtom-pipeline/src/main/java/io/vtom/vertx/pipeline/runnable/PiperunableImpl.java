//package io.vtom.vertx.pipeline.runnable;
//
//import io.vtom.vertx.pipeline.step.StepCaller;
//import io.vtom.vertx.pipeline.step.StepOUT;
//
//class PiperunableImpl implements Piperunnable {
//
//  private StepOUT output;
//
//  PiperunableImpl(StepOUT output) {
//    this.output = output;
//  }
//
//  @Override
//  public int ord() {
//    return this.output.ord();
//  }
//
//  @Override
//  public StepCaller caller() {
//    return null;
//  }
//}
