package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public interface StepOUT {

  void skip(Skip skip);

}
