package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractExecuteStepOUT;

import java.util.List;

abstract class AbstractVtmFsOut extends AbstractExecuteStepOUT implements VtmFsOut {
  public AbstractVtmFsOut(List<Handler<Skip>> skips) {
    super(skips);
  }
}
