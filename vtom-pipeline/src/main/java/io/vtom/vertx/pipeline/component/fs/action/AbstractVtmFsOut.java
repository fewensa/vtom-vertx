package io.vtom.vertx.pipeline.component.fs.action;

import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;

public abstract class AbstractVtmFsOut implements VtmFsOut {

  private StepWrapper<? extends StepIN> wrapper;

  public AbstractVtmFsOut(StepWrapper<? extends StepIN> wrapper) {
    this.wrapper = wrapper;
  }


  @Override
  public String id() {
    return this.wrapper.id();
  }

  @Override
  public int ord() {
    return this.wrapper.ord();
  }

  @Override
  public int after() {
    return this.wrapper.after();
  }


}
