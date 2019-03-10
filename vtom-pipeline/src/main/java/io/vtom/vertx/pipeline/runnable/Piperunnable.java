package io.vtom.vertx.pipeline.runnable;

import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface Piperunnable {

//  StepOUT stepout();

  Pipepromise call();

}
