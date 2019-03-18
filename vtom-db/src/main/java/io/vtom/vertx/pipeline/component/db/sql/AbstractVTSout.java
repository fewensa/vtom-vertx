package io.vtom.vertx.pipeline.component.db.sql;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVTSout implements VTSout {

  private List<Handler<Skip>> stepskips;

  protected AbstractVTSout(List<Handler<Skip>> stepskips) {
    this.stepskips = stepskips;
  }

  @Override
  public void skip(Skip skip) {
    if (CollectionKit.isEmpty(this.stepskips))
      return;
    this.stepskips.forEach(stepskip -> stepskip.handle(skip));
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
