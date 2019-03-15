package io.vtom.vertx.pipeline.component.fs.action;

import io.enoa.toolkit.collection.CollectionKit;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepSkip;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.List;

public abstract class AbstractVtmFsOut implements VtmFsOut {

  private PipeLifecycle lifecycle;
  private StepWrapper<? extends StepIN> wrapper;
  private List<StepSkip> stepskips;

  public AbstractVtmFsOut(PipeLifecycle lifecycle, StepWrapper<? extends StepIN> wrapper, List<StepSkip> stepskips) {
    this.lifecycle = lifecycle;
    this.wrapper = wrapper;
    this.stepskips = stepskips;
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

  @Override
  public void skip() {
    if (CollectionKit.isEmpty(this.stepskips))
      return;
    this.stepskips.forEach(stepskip -> stepskip.skip(this.lifecycle.skip()));
  }
}
