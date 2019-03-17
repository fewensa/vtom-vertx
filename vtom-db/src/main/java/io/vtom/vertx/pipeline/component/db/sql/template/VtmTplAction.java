package io.vtom.vertx.pipeline.component.db.sql.template;

import io.vtom.vertx.pipeline.component.db.sql.ISqlAction;
import io.vtom.vertx.pipeline.component.db.sql.SqlAction;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;

public class VtmTplAction implements ISqlAction<VtmTplSql> {

  private String cfgname;

  public VtmTplAction(String cfgname) {
    this.cfgname = cfgname;
  }

  @Override
  public VtmTplSql select(String sqlname) {
    return VtmTplSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sqlname(sqlname);
  }

  @Override
  public VtmTplSql select(String sqlname, int pn, int ps) {
    return this.select(sqlname, pn, ps, IPSql.sqlfrom());
  }

  @Override
  public VtmTplSql select(String sqlname, int pn, int ps, IPSql ipsql) {
    return VtmTplSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sqlname(sqlname)
      .pageSelect()
      .pn(pn)
      .ps(ps)
      .ipsql(ipsql);
  }

  @Override
  public VtmTplSql update(String sqlname) {
    return VtmTplSql.with(this.cfgname)
      .action(SqlAction.UPDATE)
      .sqlname(sqlname);
  }

  @Override
  public VtmTplSql execute(String sqlname) {
    return VtmTplSql.with(this.cfgname)
      .action(SqlAction.EXECUTE)
      .sqlname(sqlname);
  }

  @Override
  public VtmTplSql call(String sqlname) {
    return VtmTplSql.with(this.cfgname)
      .action(SqlAction.CALL)
      .sqlname(sqlname);
  }

}
