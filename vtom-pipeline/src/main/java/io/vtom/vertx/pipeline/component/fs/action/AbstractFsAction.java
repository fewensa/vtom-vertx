package io.vtom.vertx.pipeline.component.fs.action;

import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFsAction<T extends Fs> implements Fs {

  private String path;
  private List<StepSkip> stepskips;

  public AbstractFsAction(String path) {
    this.path = path;
  }

  protected String path() {
    return this.path;
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
