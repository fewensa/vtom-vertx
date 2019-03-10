package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.Pipepromise;

public interface StepCaller {

  Pipepromise call();

}
