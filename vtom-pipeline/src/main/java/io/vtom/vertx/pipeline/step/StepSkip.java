package io.vtom.vertx.pipeline.step;

import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public interface StepSkip {

  void skip(Skip skip);

}
