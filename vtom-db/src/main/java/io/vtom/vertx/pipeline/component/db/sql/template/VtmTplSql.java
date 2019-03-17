package io.vtom.vertx.pipeline.component.db.sql.template;

import io.enoa.firetpl.FireBody;
import io.enoa.toolkit.map.Kv;
import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.component.db.dialect.IDialect;
import io.vtom.vertx.pipeline.component.db.sql.*;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;


public class VtmTplSql extends AbstractTSql<VtmTplSql> {


  private TSqlOptions options;

  private SqlAction action;
  private String sqlname;
  private Kv parakv;
  private IPSql ipsql;
  private int ps;
  private int pn;
  private boolean pageSelect;

  private VtmTplSql(String cfgname) {
    this.options = TSql.epm().options(cfgname);
  }

  static VtmTplSql with(String cfgname) {
    return new VtmTplSql(cfgname);
  }


  VtmTplSql action(SqlAction action) {
    this.action = action;
    return this;
  }

  VtmTplSql sqlname(String sqlname) {
    this.sqlname = sqlname;
    return this;
  }

  VtmTplSql ipsql(IPSql ipsql) {
    this.ipsql = ipsql;
    return this;
  }

  VtmTplSql ps(int ps) {
    this.ps = ps;
    return this;
  }

  VtmTplSql pn(int pn) {
    this.pn = pn;
    return this;
  }

  VtmTplSql pageSelect() {
    return this.pageSelect(Boolean.TRUE);
  }

  VtmTplSql pageSelect(boolean pageSelect) {
    this.pageSelect = pageSelect;
    return this;
  }


  public VtmTplSql para(Kv kv) {
    this.parakv = kv;
    return this;
  }

  @Override
  public VTSout out() {
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
    return new AbstractVTSout(stepskips()) {

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
