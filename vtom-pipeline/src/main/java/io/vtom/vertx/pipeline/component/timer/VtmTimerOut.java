package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;

import java.util.List;

abstract class VtmTimerOut extends AbstractExecuteStepOUT {
  public VtmTimerOut(List<Handler<Skip>> skips) {
    super(skips);
  }
}
