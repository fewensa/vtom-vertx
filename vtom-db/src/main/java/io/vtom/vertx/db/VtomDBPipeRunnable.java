package io.vtom.vertx.db;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.db.sql.VTSout;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.concurrent.atomic.AtomicInteger;

class VtomDBPipeRunnable implements PipeRunnable<TSql, VTSout> {

  private Pipeline pipeline;
  private JDBCClient client;
  private StepWrapper<TSql> wrapper;
  private Kv shared;

  VtomDBPipeRunnable(Pipeline pipeline, JDBCClient client, StepWrapper<TSql> wrapper, Kv shared) {
    this.pipeline = pipeline;
    this.client = client;
    this.wrapper = wrapper;
    this.shared = shared;
  }

  @Override
  public StepWrapper<TSql> wrapper() {
    return this.wrapper;
  }

  @Override
  public void call(VTSout stepout, Handler<AsyncResult<Object>> handler) {
    SQLConnection holderconn = this.shared.as("conn");
    if (holderconn != null) {
      this.vrun2(holderconn, stepout, handler);
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
        this.vrun2(conn, stepout, handler);
        return;
      }
      conn.setAutoCommit(false, acr -> {
        if (acr.failed()) {
          handler.handle(Future.failedFuture(acr.cause()));
          return;
        }
        this.vrun2(conn, stepout, handler);
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
    switch (output.action()) {
      case CALL:
//        conn.ca
        break;
      case UPDATE:
        conn.updateWithParams(output.sql(), output.paras(), ar -> {
          if (ar.failed()) {
            handler.handle(Future.failedFuture(ar.cause()));
            return;
          }
          UpdateResult result = ar.result();
          handler.handle(Future.succeededFuture(result));
        });
        break;
      case SELECT:
        conn.queryWithParams(output.sql(), output.paras(), ar -> {
          if (ar.failed()) {
            handler.handle(Future.failedFuture(ar.cause()));
            return;
          }
          ResultSet result = ar.result();
          System.out.println(result + " - " + System.nanoTime());
          handler.handle(Future.succeededFuture(result));
        });
        break;
    }
  }

}
