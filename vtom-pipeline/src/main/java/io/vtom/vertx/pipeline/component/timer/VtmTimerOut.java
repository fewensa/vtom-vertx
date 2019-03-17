package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.List;

public abstract class VtmTimerOut implements StepOUT {

  private List<StepSkip> stepskips;

  public VtmTimerOut(List<StepSkip> stepskips) {
    this.stepskips = stepskips;
  }

  @Override
  public void skip(Skip skip) {
    if (this.stepskips == null)
      return;
    this.stepskips.forEach(stepskip -> stepskip.skip(skip));
  }

  protected abstract void execute(Vertx vertx, Handler<AsyncResult<Object>> handler);

}
