package io.vtom.vertx.pipeline.component.fs.action;

import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

abstract class AbstractVtmFsOut extends AbstractExecuteStepOUT implements VtmFsOut {
  public AbstractVtmFsOut(List<StepSkip> skips) {
    super(skips);
  }
}
