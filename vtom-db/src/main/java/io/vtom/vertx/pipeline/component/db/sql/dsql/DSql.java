package io.vtom.vertx.pipeline.component.db.sql.dsql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.component.db.dialect.IDialect;
import io.vtom.vertx.pipeline.component.db.sql.*;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;

public class DSql extends AbstractTSql<DSql> {


  private TSqlOptions options;

  private SqlAction action;
  private IPSql ipsql;
  private int ps;
  private int pn;
  private boolean pageSelect;
  private String sql;

  protected DSql(String cfgname) {
    this.options = TSql.epm().options(cfgname);
  }

  static DSql with(String cfgname) {
    return new DSql(cfgname);
  }


  protected DSql action(SqlAction action) {
    this.action = action;
    return this;
  }

  protected DSql ipsql(IPSql ipsql) {
    this.ipsql = ipsql;
    return this;
  }

  protected DSql ps(int ps) {
    this.ps = ps;
    return this;
  }

  protected DSql pn(int pn) {
    this.pn = pn;
    return this;
  }

  protected DSql pageSelect() {
    return this.pageSelect(Boolean.TRUE);
  }

  protected DSql pageSelect(boolean pageSelect) {
    this.pageSelect = pageSelect;
    return this;
  }

  protected DSql sql(String sql) {
    this.sql = sql;
    return this;
  }


  @Override
  public <I extends StepIN> VTSout out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    JsonArray paras = super.paras();
    return new AbstractVTSout(lifecycle, wrapper, stepskips()) {
      @Override
      public IDialect dialect() {
        return options.getDialect();
      }

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
      public boolean showSql() {
        return options.isShowSql();
      }

      @Override
      public ISqlReporter reporter() {
        return options.getReporter();
      }

      @Override
      public boolean pageSelect() {
        return pageSelect;
      }

      @Override
      public IPSql ipsql() {
        return ipsql;
      }

      @Override
      public int ps() {
        return ps;
      }

      @Override
      public int pn() {
        return pn;
      }

    };
  }

}
