package io.vtom.vertx.pipeline.component.db.sql.template;

import io.enoa.firetpl.FireBody;
import io.enoa.toolkit.map.Kv;
import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.component.db.dialect.IDialect;
import io.vtom.vertx.pipeline.component.db.sql.*;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;


public class TplSql extends AbstractTSql<TplSql> {


  private TSqlOptions options;

  private SqlAction action;
  private String sqlname;
  private Kv parakv;
  private IPSql ipsql;
  private int ps;
  private int pn;
  private boolean pageSelect;

  private TplSql(String cfgname) {
    this.options = TSql.epm().options(cfgname);
  }

  static TplSql with(String cfgname) {
    return new TplSql(cfgname);
  }


  TplSql action(SqlAction action) {
    this.action = action;
    return this;
  }

  TplSql sqlname(String sqlname) {
    this.sqlname = sqlname;
    return this;
  }

  TplSql ipsql(IPSql ipsql) {
    this.ipsql = ipsql;
    return this;
  }

  TplSql ps(int ps) {
    this.ps = ps;
    return this;
  }

  TplSql pn(int pn) {
    this.pn = pn;
    return this;
  }

  TplSql pageSelect() {
    return this.pageSelect(Boolean.TRUE);
  }

  TplSql pageSelect(boolean pageSelect) {
    this.pageSelect = pageSelect;
    return this;
  }


  public TplSql para(Kv kv) {
    this.parakv = kv;
    return this;
  }

  @Override
  public <I extends StepIN> VTSout out(PipeLifecycle lifecycle, StepWrapper<I> wrapper) {
    FireBody firebody = this.options.getFiretpl().render(this.sqlname, this.parakv);
    Object[] fireparas = firebody.paras();
    JsonArray paja = super.paras();
    if (paja == null) {
      paja = new JsonArray();
      for (Object firepara : fireparas) {
        if (firepara == null) {
          paja.addNull();
          continue;
        }
        paja.add(firepara);
      }
    }

    final JsonArray _paras = paja;
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
        return firebody.tpl();
      }

      @Override
      public JsonArray paras() {
        return _paras;
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
