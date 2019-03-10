package io.vtom.vertx.db.sql;

import io.enoa.toolkit.map.Kv;
import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepWrapper;

public class TSql implements StepIN {


  public enum Action {
    SELECT, UPDATE, CALL
  }

  private Action action;
  private String sql;
  private Kv kv;

  public static TSql create(Action action, String sql) {
    return new TSql(action, sql);
  }

  public static TSql select(String sql) {
    return create(Action.SELECT, sql);
  }

  public static TSql update(String sql) {
    return create(Action.UPDATE, sql);
  }

  private TSql(Action action, String sql) {
    this.action = action;
    this.sql = sql;
  }

  public TSql kv(Kv kv) {
    this.kv = kv;
    return this;
  }

  @Override
  public <I extends StepIN> StepOUT out(StepWrapper<I> wrapper) {
    return new VTSout() {
      @Override
      public Action action() {
        return action;
      }

      @Override
      public String sql() {
        return sql;
      }

      @Override
      public JsonArray paras() {
        // todo fill pars
        return new JsonArray();
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

  @Override
  public String toString() {
    return this.sql;
  }
}
