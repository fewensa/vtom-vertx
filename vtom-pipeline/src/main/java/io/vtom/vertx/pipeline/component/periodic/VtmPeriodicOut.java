package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;

import java.util.List;

abstract class VtmPeriodicOut extends AbstractExecuteStepOUT {
  public VtmPeriodicOut(List<Handler<Skip>> skips) {
    super(skips);
  }
}
