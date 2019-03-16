package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public interface StepOUT {

  String id();

  int ord();

  int after();

  void skip(Skip skip);

}
