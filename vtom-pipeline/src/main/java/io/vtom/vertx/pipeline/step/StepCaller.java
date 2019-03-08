package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.promise.Pipepromise;

public interface StepCaller {

  Pipepromise call();

}
