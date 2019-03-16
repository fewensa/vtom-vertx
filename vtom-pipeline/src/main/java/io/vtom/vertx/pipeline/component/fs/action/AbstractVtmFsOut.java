package io.vtom.vertx.pipeline.component.fs.action;

import io.enoa.toolkit.collection.CollectionKit;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

public abstract class AbstractVtmFsOut implements VtmFsOut {

  private List<StepSkip> stepskips;

  public AbstractVtmFsOut(List<StepSkip> stepskips) {
    this.stepskips = stepskips;
  }

  @Override
  public void skip(Skip skip) {
    if (CollectionKit.isEmpty(this.stepskips))
      return;
    this.stepskips.forEach(stepskip -> stepskip.skip(skip));
  }
}
