package io.vtom.vertx.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface VTSout extends StepOUT {


  TSql.Action action();

  String sql();

  JsonArray paras();

//  static VTSout create(TSql tsql) {
//    return new VTSout(tsql);
//  }
//
//  private TSql tsql;
//
//  private VTSout(TSql tsql) {
//    this.tsql = tsql;
//  }
//
//  public TSql.Action action() {
//    return this.tsql.action;
//  }
//
//  public String sql() {
//    return this.tsql.sql;
//  }
//
//  public JsonArray paras() {
//    return new JsonArray();
//  }
//
//  @Override
//  public String id() {
//    return null;
//  }
//
//  @Override
//  public int ord() {
//    return 0;
//  }
//
//  @Override
//  public int after() {
//    return 0;
//  }

//  @Override
//  public String toString() {
//    return TextKit.union(this.id(), " <", this.ord(), " AF ", this.after(), "> ", " [", this.sql(), "]");
//  }

}
