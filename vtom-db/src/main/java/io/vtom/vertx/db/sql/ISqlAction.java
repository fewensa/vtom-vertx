package io.vtom.vertx.db.sql;

import io.vtom.vertx.db.sql.psql.IPSql;

public interface ISqlAction<T extends TSql> {


  T select(String sql);

  default T select(String sql, int pn, int ps) {
    return this.select(sql, pn, ps, IPSql.sqlfrom());
  }

  T select(String sql, int pn, int ps, IPSql ipsql);

  T update(String sql);

  T execute(String sql);

  T call(String sql);

//  T stream(String sql);

}
