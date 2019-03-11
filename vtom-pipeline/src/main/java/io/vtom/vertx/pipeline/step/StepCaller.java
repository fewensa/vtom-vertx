package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.PipePromise;

public interface StepCaller {

  PipePromise call();

}
