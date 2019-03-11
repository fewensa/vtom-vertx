package io.vtom.vertx.db.sql;

import io.vtom.vertx.db.sql.dsql.DSql;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

public interface TSql extends StepIN {

//  private SqlAction action;
//  private String sql;
//  private Kv kv;
//
//  public static TSql create(SqlAction action, String sql) {
//    return new TSql(action, sql);
//  }
//
//  public static TSql select(String sql) {
//    return create(SqlAction.SELECT, sql);
//  }
//
//  public static TSql update(String sql) {
//    return create(SqlAction.UPDATE, sql);
//  }
//
//  private TSql(SqlAction action, String sql) {
//    this.action = action;
//    this.sql = sql;
//  }
//
//  public TSql kv(Kv kv) {
//    this.kv = kv;
//    return this;
//  }
//
//  @Override
//  public <I extends StepIN> StepOUT out(StepWrapper<I> wrapper) {
//    return new VTSout() {
//      @Override
//      public SqlAction action() {
//        return action;
//      }
//
//      @Override
//      public String sql() {
//        return sql;
//      }
//
//      @Override
//      public JsonArray paras() {
//        // xtodo fill pars
//        return new JsonArray();
//      }
//
//      @Override
//      public String id() {
//        return wrapper.id();
//      }
//
//      @Override
//      public int ord() {
//        return wrapper.ord();
//      }
//
//      @Override
//      public int after() {
//        return wrapper.after();
//      }
//    };
//  }
//
//  @Override
//  public String toString() {
//    return this.sql;
//  }


  static DSql def(SqlAction action) {
    return new DSql(action);
  }

  @Override
  <I extends StepIN> VTSout out(StepWrapper<I> wrapper);
}
