package io.vtom.vertx.pipeline.step;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.List;

public abstract class AbstractExecuteStepOUT implements StepOUT {

  private List<StepSkip> skips;

  public AbstractExecuteStepOUT(List<StepSkip> skips) {
    this.skips = skips;
  }

  @Override
  public void skip(Skip skip) {
    if (CollectionKit.isEmpty(this.skips))
      return;
    this.skips.forEach(stepskip -> stepskip.skip(skip));
  }

  public abstract void execute(Vertx vertx, Handler<AsyncResult<Object>> handler);

}

