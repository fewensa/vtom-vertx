package io.vtom.vertx.pipeline.component.timer;

import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

abstract class VtmTimerOut extends AbstractExecuteStepOUT {
  public VtmTimerOut(List<StepSkip> skips) {
    super(skips);
  }
}
