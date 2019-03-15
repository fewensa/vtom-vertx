package io.vtom.vertx.pipeline.component.db.sql;

import io.enoa.firetpl.Firetpl;
import io.vtom.vertx.pipeline.component.db.dialect.IDialect;
import io.vtom.vertx.pipeline.component.db.dialect.MysqlDialect;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;

public class TSqlOptions {


  private Firetpl firetpl;
  private boolean showSql;
  private ISqlReporter reporter;
  private IDialect dialect;

  public TSqlOptions() {
    this.reporter = ISqlReporter.def();
    this.dialect = new MysqlDialect();
  }

  public Firetpl getFiretpl() {
    return firetpl;
  }

  public TSqlOptions setFiretpl(Firetpl firetpl) {
    this.firetpl = firetpl;
    return this;
  }

  public boolean isShowSql() {
    return showSql;
  }

  public TSqlOptions setShowSql(boolean showSql) {
    this.showSql = showSql;
    return this;
  }

  public ISqlReporter getReporter() {
    return reporter;
  }

  public TSqlOptions setReporter(ISqlReporter reporter) {
    this.reporter = reporter;
    return this;
  }

  public IDialect getDialect() {
    return dialect;
  }

  public TSqlOptions setDialect(IDialect dialect) {
    this.dialect = dialect;
    return this;
  }
}
