package io.vtom.vertx.pipeline.component.periodic;

import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractPeriodic<T extends Periodic> implements Periodic {

  private List<StepSkip> stepskips;

  public AbstractPeriodic() {
  }

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
