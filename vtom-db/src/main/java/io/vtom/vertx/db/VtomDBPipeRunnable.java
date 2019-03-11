package io.vtom.vertx.db;

import io.enoa.promise.Promise;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.db.sql.VTSout;
import io.vtom.vertx.pipeline.PipeLifecycle;
import io.vtom.vertx.pipeline.PipePromise;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.scope.ScopeContext;
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
  public PipePromise call(VTSout stepout) {
    EPDoneArgPromiseBuilder<PipeLifecycle> promise = Promise.builder().donearg();
    PipePromise _ret = new PipePromise(promise.build());

    SQLConnection holderconn = this.shared.as("conn");
    if (holderconn != null) {
      this.vrun(promise, holderconn, stepout);
      return _ret;
    }

    this.client.getConnection(ar -> {
      if (ar.failed()) {
        Promise.builder().handler().handleCapture(promise, ar.cause());
        return;
      }

      SQLConnection conn = ar.result();
      this.shared.set("conn", conn);

      Boolean tx = this.shared.bool("tx", Boolean.FALSE);
      if (!tx) {
        this.vrun(promise, conn, stepout);
        return;
      }

      conn.setAutoCommit(false, acr -> {
        if (acr.failed()) {
          Promise.builder().handler().handleCapture(promise, acr.cause());
          return;
        }
        this.vrun(promise, conn, stepout);
      });

    });
    return _ret;
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

  private void vrun(EPDoneArgPromiseBuilder<PipeLifecycle> promise, SQLConnection conn, VTSout output) {
    switch (output.action()) {
      case CALL:
//        conn.callWithParams(arg.sql(), arg.paras(), ar -> {
//          if (ar.failed()) {
//            promise.captures().forEach(capture -> capture.execute(ar.cause()));
//            return;
//          }
//          promise.dones().forEach(done -> done.execute(ar.result()));
//        });
        break;
      case UPDATE:
        conn.updateWithParams(output.sql(), output.paras(), this.conncall(promise, output));
        break;
      case SELECT:
        conn.queryWithParams(output.sql(), output.paras(), this.conncall(promise, output));
        break;
//      default:
//        Promise.builder().handler().handleCapture(promise, new NoStackTraceThrowable(EnoaTipKit.message("eo.tip.vtom.db.not_support_action")));
//        break;
    }
  }

  private <T> Handler<AsyncResult<T>> conncall(EPDoneArgPromiseBuilder<PipeLifecycle> promise, VTSout output) {
    return ar -> {
      if (ar.failed()) {
        Promise.builder().handler().handleCapture(promise, ar.cause());
        return;
      }

      T result = ar.result();

      System.out.println(result + " - " + System.nanoTime());
      PipeLifecycle cycle = this.pipeline.cycle();

      ScopeContext.context(cycle.scope()).put(output, result);

      promise.dones().forEach(done -> done.execute(cycle));
      if (promise.always() != null)
        promise.always().execute();
    };
  }

//  private Handler<AsyncResult<Void>> txcall(EPDoneArgPromiseBuilder<PipeLifecycle> promise) {
//    return ar -> {
//      CollectionKit.clear(this.shared);
//
//      if (ar.failed()) {
//        Promise.builder().handler().handleCapture(promise, ar.cause());
//        return;
//      }
//      Promise.builder().handler().handleDoneArg(promise, this.pipeline.cycle());
//    };
//  }


}
