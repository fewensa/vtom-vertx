package io.vtom.vertx.pipeline.component.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.stream.Collectors;

public abstract class AbstractVTSout implements VTSout {

  private StepWrapper<? extends StepIN> wrapper;

  protected AbstractVTSout(StepWrapper<? extends StepIN> wrapper) {
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
