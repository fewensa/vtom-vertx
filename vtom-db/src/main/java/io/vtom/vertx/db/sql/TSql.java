package io.vtom.vertx.db.sql;

import io.enoa.toolkit.digest.UUIDKit;
import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.step.StepIN;

public class TSql implements StepIN {

  public enum Action {
    SELECT, UPDATE, CALL
  }

  Action action;
  String sql;
  Kv kv;
  int ord;
  int after;
  String id;

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
    this.ord = 0;
    this.after = 0;
    this.id = UUIDKit.next(false);
  }

  public TSql kv(Kv kv) {
    this.kv = kv;
    return this;
  }

  @Override
  public TSql id(String id) {
    this.id = id;
    return this;
  }

  @Override
  public TSql ord(int ord) {
    this.ord = ord;
    return this;
  }

  @Override
  public TSql after(int after) {
    this.after = after;
    return this;
  }

  @Override
  public VTSout output() {
    return VTSout.create(this);
  }

  @Override
  public String toString() {
    return this.sql;
  }
}
