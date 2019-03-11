package io.vtom.vertx.db.sql.dsql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.db.sql.SqlAction;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.db.sql.VTSout;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;

public class DSql implements TSql {

  private SqlAction action;
  private String sql;
  private JsonArray paras;

  public DSql(SqlAction action) {
    this.action = action;
  }

  public DSql sql(String sql) {
    this.sql = sql;
    return this;
  }


  @Override
  public <I extends StepIN> VTSout out(StepWrapper<I> wrapper) {
    return new VTSout() {
      @Override
      public SqlAction action() {
        return action;
      }

      @Override
      public String sql() {
        return sql;
      }

      @Override
      public JsonArray paras() {
        return paras;
      }

      @Override
      public String id() {
        return wrapper.id();
      }

      @Override
      public int ord() {
        return wrapper.ord();
      }

      @Override
      public int after() {
        return wrapper.after();
      }
    };
  }
}
