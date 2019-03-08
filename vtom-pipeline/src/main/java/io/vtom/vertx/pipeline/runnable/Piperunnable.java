package io.vtom.vertx.pipeline.runnable;

import io.vtom.vertx.pipeline.promise.Pipepromise;

public interface Piperunnable {

  int ord();

  Pipepromise call();

}
