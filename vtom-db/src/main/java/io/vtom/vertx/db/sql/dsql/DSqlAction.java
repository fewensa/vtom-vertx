package io.vtom.vertx.db.sql.dsql;

import io.vtom.vertx.db.sql.ISqlAction;
import io.vtom.vertx.db.sql.SqlAction;
import io.vtom.vertx.db.sql.psql.IPSql;

public class DSqlAction implements ISqlAction<DSql> {

  private String cfgname;

  public DSqlAction(String cfgname) {
    this.cfgname = cfgname;
  }

  @Override
  public DSql select(String sql) {
    return DSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sql(sql);
  }

  @Override
  public DSql select(String sql, int pn, int ps, IPSql ipsql) {
    return DSql.with(this.cfgname)
      .action(SqlAction.SELECT)
      .sql(sql)
      .pageSelect()
      .pn(pn)
      .ps(ps)
      .ipsql(ipsql);
  }

  @Override
  public DSql update(String sql) {
    return DSql.with(this.cfgname)
      .action(SqlAction.UPDATE)
      .sql(sql);
  }

  @Override
  public DSql execute(String sql) {
    return DSql.with(this.cfgname)
      .action(SqlAction.EXECUTE)
      .sql(sql);
  }

  @Override
  public DSql call(String sql) {
    return DSql.with(this.cfgname)
      .action(SqlAction.CALL)
      .sql(sql);
  }

}
