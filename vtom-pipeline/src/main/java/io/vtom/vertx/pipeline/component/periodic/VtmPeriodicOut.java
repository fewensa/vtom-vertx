package io.vtom.vertx.pipeline.component.periodic;

import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

abstract class VtmPeriodicOut extends AbstractExecuteStepOUT {
  public VtmPeriodicOut(List<StepSkip> skips) {
    super(skips);
  }
}
