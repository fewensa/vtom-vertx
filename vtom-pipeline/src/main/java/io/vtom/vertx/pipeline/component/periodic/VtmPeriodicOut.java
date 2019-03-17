package io.vtom.vertx.pipeline.component.periodic;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

public abstract class VtmPeriodicOut implements StepOUT {

  private List<StepSkip> skips;

  public VtmPeriodicOut(List<StepSkip> skips) {
    this.skips = skips;
  }

  @Override
  public void skip(Skip skip) {
    if (CollectionKit.isEmpty(this.skips))
      return;
    this.skips.forEach(stepskip -> stepskip.skip(skip));
  }


  protected abstract void execute(Vertx vertx, Handler<AsyncResult<Object>> handler);

}
