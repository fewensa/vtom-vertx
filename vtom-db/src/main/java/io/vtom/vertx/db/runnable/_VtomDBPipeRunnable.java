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
import io.vtom.vertx.pipeline.Pipepromise;
import io.vtom.vertx.pipeline.Piperunnable;
import io.vtom.vertx.pipeline.scope.ScopeContext;
import io.vtom.vertx.pipeline.step.StepWrapper;

public class _VtomDBPipeRunnable implements Piperunnable<TSql, VTSout> {

  private Pipeline pipeline;
  private JDBCClient client;
  private StepWrapper<TSql> wrapper;

  public _VtomDBPipeRunnable(Pipeline pipeline, JDBCClient client, StepWrapper<TSql> wrapper) {
    this.pipeline = pipeline;
    this.client = client;
    this.wrapper = wrapper;
  }

//  @Override
//  public StepOUT stepout() {
//    return this.out;
//  }

  @Override
  public StepWrapper<TSql> wrapper() {
    return this.wrapper;
  }

  @Override
  public Pipepromise call(VTSout output) {
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
      this.vrun(promise, conn, output);
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
        conn.updateWithParams(output.sql(), output.paras(), this.conncall(promise, output));
        break;
      case SELECT:
        conn.queryWithParams(output.sql(), output.paras(), this.conncall(promise, output));
        break;
      default:
        promise.captures().forEach(capture -> capture.execute(new NoStackTraceThrowable(EnoaTipKit.message("eo.tip.vtom.db.not_support_action"))));
        promise.always().execute();
        break;
    }
  }

  private <T> Handler<AsyncResult<T>> conncall(EPDoneArgPromiseBuilder<Pipecycle> promise, VTSout output) {
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

      ScopeContext.context(cycle.scope()).put(output, result);

      promise.dones().forEach(done -> done.execute(cycle));
      if (promise.always() != null)
        promise.always().execute();
    };
  }

}
