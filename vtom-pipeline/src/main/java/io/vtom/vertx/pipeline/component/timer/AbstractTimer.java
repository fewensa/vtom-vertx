package io.vtom.vertx.pipeline.component.timer;

import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTimer<T extends Timer> implements Timer {

  private List<StepSkip> stepskips;

  @Override
  public T skip(StepSkip stepskip) {
    if (this.stepskips == null)
      this.stepskips = new ArrayList<>();
    this.stepskips.add(stepskip);
    return (T) this;
  }

  protected List<StepSkip> stepskips() {
    return this.stepskips;
  }

}
