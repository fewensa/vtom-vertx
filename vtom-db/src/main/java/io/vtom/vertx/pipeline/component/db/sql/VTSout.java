package io.vtom.vertx.pipeline.component.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.component.db.dialect.IDialect;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface VTSout extends StepOUT {

  IDialect dialect();

  SqlAction action();

  String sql();

  JsonArray paras();

  boolean showSql();

  ISqlReporter reporter();

  boolean pageSelect();

  IPSql ipsql();

  int ps();

  int pn();

}
