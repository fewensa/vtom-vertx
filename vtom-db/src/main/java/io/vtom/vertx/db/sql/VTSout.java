package io.vtom.vertx.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepOUT;

public class VTSout implements StepOUT {

  static VTSout create(TSql tsql) {
    return new VTSout(tsql);
  }

  private TSql tsql;

  private VTSout(TSql tsql) {
    this.tsql = tsql;
  }

  public TSql.Action action() {
    return this.tsql.action;
  }

  public String sql() {
    return this.tsql.sql;
  }

  public JsonArray paras() {
    return new JsonArray();
  }

  @Override
  public int ord() {
    return this.tsql.ord;
  }

}
