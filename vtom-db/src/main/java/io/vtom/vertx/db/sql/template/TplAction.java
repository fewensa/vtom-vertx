package io.vtom.vertx.db.sql.template;

import io.vtom.vertx.db.sql.ISqlAction;
import io.vtom.vertx.db.sql.SqlAction;
import io.vtom.vertx.db.sql.psql.IPSql;

public class TplAction implements ISqlAction<TplSql> {

  private String cfgname;

  public TplAction(String cfgname) {
    this.cfgname = cfgname;
  }

  @Override
  public TplSql select(String sqlname) {
    return TplSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sqlname(sqlname);
  }

  @Override
  public TplSql select(String sqlname, int pn, int ps) {
    return this.select(sqlname, pn, ps, IPSql.sqlfrom());
  }

  @Override
  public TplSql select(String sqlname, int pn, int ps, IPSql ipsql) {
    return TplSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sqlname(sqlname)
      .pageSelect()
      .pn(pn)
      .ps(ps)
      .ipsql(ipsql);
  }

  @Override
  public TplSql update(String sqlname) {
    return TplSql.with(this.cfgname)
      .action(SqlAction.UPDATE)
      .sqlname(sqlname);
  }

  @Override
  public TplSql execute(String sqlname) {
    return TplSql.with(this.cfgname)
      .action(SqlAction.EXECUTE)
      .sqlname(sqlname);
  }

  @Override
  public TplSql call(String sqlname) {
    return TplSql.with(this.cfgname)
      .action(SqlAction.CALL)
      .sqlname(sqlname);
  }

}
