package io.vtom.vertx.pipeline.step;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.List;

public abstract class AbstractStepOUT implements StepOUT {

  private List<Handler<Skip>> skips;

  public AbstractStepOUT(List<Handler<Skip>> skips) {
    this.skips = skips;
  }

  @Override
  public void skip(Skip skip) {
    if (CollectionKit.isEmpty(this.skips))
      return;
    this.skips.forEach(stepskip -> stepskip.handle(skip));
  }

}
