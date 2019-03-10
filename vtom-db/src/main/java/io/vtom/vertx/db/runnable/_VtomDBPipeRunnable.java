package io.vtom.vertx.db.runnable;

import io.enoa.promise.Promise;
import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
import io.enoa.toolkit.eo.tip.EnoaTipKit;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.db.sql.VTSout;
import io.vtom.vertx.pipeline.Pipecycle;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.runnable.Piperunnable;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.Pipestack;
import io.vtom.vertx.pipeline.step.StepOUT;

public class _VtomDBPipeRunnable implements Piperunnable {

  private Pipeline pipeline;
  private JDBCClient client;
  private Pipestack<TSql> pipestack;

  public _VtomDBPipeRunnable(Pipeline pipeline, JDBCClient client, Pipestack<TSql> pipestack) {
    this.pipeline = pipeline;
    this.client = client;
    this.pipestack = pipestack;
  }

//  @Override
//  public StepOUT stepout() {
//    return this.out;
//  }

  @Override
  public Pipepromise call() {
    EPDoneArgPromiseBuilder<Pipecycle> promise = Promise.builder().donearg();
    Pipepromise _ret = new Pipepromise(promise.build());
    this.client.getConnection(ar -> {
      if (ar.failed()) {
        promise.captures().forEach(capture -> capture.execute(ar.cause()));
        if (promise.always() != null)
          promise.always().execute();
        return;
      }
      SQLConnection conn = ar.result();
      this.vrun(promise, conn, this.out);
    });
    return _ret;
  }


  private void vrun(EPDoneArgPromiseBuilder<Pipecycle> promise, SQLConnection conn, VTSout output) {
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
        conn.updateWithParams(output.sql(), output.paras(), this.conncall(promise));
        break;
      case SELECT:
        conn.queryWithParams(output.sql(), output.paras(), this.conncall(promise));
        break;
      default:
        promise.captures().forEach(capture -> capture.execute(new NoStackTraceThrowable(EnoaTipKit.message("eo.tip.vtom.db.not_support_action"))));
        promise.always().execute();
        break;
    }
  }

  private <T> Handler<AsyncResult<T>> conncall(EPDoneArgPromiseBuilder<Pipecycle> promise) {
    return ar -> {
      if (ar.failed()) {
        promise.captures().forEach(capture -> capture.execute(ar.cause()));
        if (promise.always() != null)
          promise.always().execute();
        return;
      }
      T result = ar.result();

      System.out.println(result + " - " + System.nanoTime());
      Pipecycle cycle = this.pipeline.cycle();

      ScopeContext.context(cycle.scope()).put(this.out, result);

      promise.dones().forEach(done -> done.execute(cycle));
      if (promise.always() != null)
        promise.always().execute();
    };
  }

}
