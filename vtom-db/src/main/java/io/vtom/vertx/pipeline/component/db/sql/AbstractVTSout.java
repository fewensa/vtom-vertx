package io.vtom.vertx.pipeline.component.db.sql;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepSkip;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVTSout implements VTSout {

  private PipeLifecycle lifecycle;
  private StepWrapper<? extends StepIN> wrapper;
  private List<StepSkip> stepskips;

  protected AbstractVTSout(PipeLifecycle lifecycle, StepWrapper<? extends StepIN> wrapper, List<StepSkip> stepskips) {
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

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder();
    ret.append(this.sql());
    JsonArray paras = this.paras();
    if (paras == null || paras.isEmpty())
      return ret.toString();
    ret.append("       ----> [");
    ret.append(paras.stream().map(Object::toString).collect(Collectors.joining(",")));
    ret.append("]");
    return ret.toString();
  }
}
