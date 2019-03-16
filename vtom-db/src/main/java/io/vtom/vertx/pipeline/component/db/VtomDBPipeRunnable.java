package io.vtom.vertx.pipeline.component.db;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.enoa.toolkit.number.NumberKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.component.db.page.Page;
import io.vtom.vertx.pipeline.component.db.sql.TSql;
import io.vtom.vertx.pipeline.component.db.sql.VTSout;
import io.vtom.vertx.pipeline.component.db.sql.psql.IPSql;
import io.vtom.vertx.pipeline.component.db.sql.psql.PSql;
import io.vtom.vertx.pipeline.component.db.sql.reporter.ISqlReporter;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.tk.Pvtk;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

class VtomDBPipeRunnable implements PipeRunnable<TSql, VTSout> {

  private JDBCClient client;
  private Step<? extends TSql> step;
  private Kv shared;

  VtomDBPipeRunnable(JDBCClient client, Step<? extends TSql> step, Kv shared) {
    this.client = client;
    this.step = step;
    this.shared = shared;
  }

  @Override
  public Step<? extends TSql> step() {
    return this.step;
  }

  @Override
  public void call(VTSout stepout, Handler<AsyncResult<Object>> handler) {
    SQLConnection holderconn = this.shared.as("conn");
    if (holderconn != null) {
      try {
        this.vrun2(holderconn, stepout, handler);
      } catch (Exception e) {
        handler.handle(Future.failedFuture(e));
      }
      return;
    }
    this.client.getConnection(ar -> {
      if (ar.failed()) {
        handler.handle(Future.failedFuture(ar.cause()));
        return;
      }
      SQLConnection conn = ar.result();
      this.shared.set("conn", conn);

      Boolean tx = this.shared.bool("tx", Boolean.FALSE);
      if (!tx) {
        try {
          this.vrun2(conn, stepout, handler);
        } catch (Exception e) {
          handler.handle(Future.failedFuture(e));
        }
        return;
      }
      conn.setAutoCommit(false, acr -> {
        if (acr.failed()) {
          handler.handle(Future.failedFuture(acr.cause()));
          return;
        }
        try {
          this.vrun2(conn, stepout, handler);
        } catch (Exception e) {
          handler.handle(Future.failedFuture(e));
        }
      });
    });

  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    AtomicInteger arc = this.shared.as("arc");
    if (arc == null || arc.decrementAndGet() != 0) {
      handler.handle(Future.succeededFuture());
      return;
    }

    SQLConnection conn = this.shared.as("conn");

    if (conn == null) {
      CollectionKit.clear(this.shared);
      handler.handle(Future.succeededFuture());
      return;
    }

    Boolean tx = this.shared.bool("tx", Boolean.FALSE);
    if (!tx) {
      conn.close();
      CollectionKit.clear(this.shared);
      handler.handle(Future.succeededFuture());
      return;
    }

    Boolean txCanceled = this.shared.bool("tx_canceled", Boolean.FALSE);
    if (txCanceled) {
      CollectionKit.clear(this.shared);
      handler.handle(Future.succeededFuture());
      return;
    }
    this.shared.set("tx_canceled", Boolean.TRUE);

    if (ok) {
      conn.commit(handler);
      CollectionKit.clear(this.shared);
      return;
    }

    conn.rollback(handler);
    CollectionKit.clear(this.shared);
  }

  private void vrun2(SQLConnection conn, VTSout output, Handler<AsyncResult<Object>> handler) {
    String sql = output.sql();
    JsonArray paras = output.paras();

    String reportmark = this.reportMark(output);


    switch (output.action()) {
      case CALL:
//        conn.ca
        break;
      case UPDATE:
        this.reportSql(output, reportmark, sql, paras);
        conn.updateWithParams(sql, paras, ar -> {
          if (ar.failed()) {
            handler.handle(Future.failedFuture(ar.cause()));
            return;
          }
          UpdateResult result = ar.result();
          handler.handle(Future.succeededFuture(result));
        });
        break;
      case SELECT:

        if (!output.pageSelect()) {
          this.reportSql(output, reportmark, sql, paras);
          conn.queryWithParams(sql, paras, Pvtk.handleTo(handler));
          return;
        }


        IPSql ipsql = output.ipsql();
        PSql psql = ipsql.psql(sql);
        int ps = output.ps(),
          pn = output.pn();


        this.reportSql(output, reportmark, psql.countSql(), paras);
        conn.queryWithParams(psql.countSql(), paras, ar0 -> {
          if (ar0.failed()) {
            handler.handle(Future.failedFuture(ar0.cause()));
            return;
          }
          ResultSet countresult = ar0.result();
          Integer _rows = countresult.getResults().get(0).getInteger(0);
          if (_rows == 0) {
            Page page = new Page<>(pn, ps, 0, 0L, 0L, Collections.emptyList(), Collections.emptyList());
            handler.handle(Future.succeededFuture(page));
            return;
          }
          int _totalPage = NumberKit.integer(_rows / ps);
          if (_rows % ps != 0)
            _totalPage += 1;
          final int tpg = _totalPage;


          long offset = ps * (pn - 1);

          if (pn > tpg) {
            Page page = new Page<>(pn, ps, tpg, offset, _rows, Collections.emptyList(), Collections.emptyList());
            handler.handle(Future.succeededFuture(page));
            return;
          }

          String pageSql = output.dialect().pageSql(offset, ps, psql.selectSql());
          this.reportSql(output, reportmark, pageSql, paras);

          conn.queryWithParams(pageSql, paras, ar1 -> {
            if (ar1.failed()) {
              handler.handle(Future.failedFuture(ar1.cause()));
              return;
            }
            ResultSet presult = ar1.result();
            Page page = new Page<>(pn, ps, tpg, offset, _rows, presult.getColumnNames(), presult.getRows());
            handler.handle(Future.succeededFuture(page));
          });

        });

        break;
    }
  }


  private String reportMark(VTSout output) {
    if (!output.showSql())
      return null;
    ISqlReporter reporter = output.reporter();
    if (reporter == null)
      return null;
    return reporter.mark();
  }

  private void reportSql(VTSout output, String mark, String sql, JsonArray paras) {
    if (!output.showSql())
      return;
    ISqlReporter reporter = output.reporter();
    if (reporter == null)
      return;
    reporter.report(mark, sql, paras);
  }


}
