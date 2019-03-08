//package io.vtom.vertx.db;
//
//import io.enoa.promise.DoneArgPromise;
//import io.enoa.promise.DonePromise;
//import io.enoa.promise.Promise;
//import io.enoa.promise.builder.EPDoneArgPromiseBuilder;
//import io.enoa.promise.builder.EPDonePromiseBuilder;
//import io.enoa.toolkit.collection.CollectionKit;
//import io.enoa.toolkit.eo.tip.EnoaTipKit;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Handler;
//import io.vertx.core.Vertx;
//import io.vertx.core.impl.NoStackTraceThrowable;
//import io.vertx.ext.jdbc.JDBCClient;
//import io.vertx.ext.sql.SQLConnection;
//import io.vtom.vertx.pipeline.step.Arg;
//import io.vtom.vertx.pipeline.Pipeline;
//import io.vtom.vertx.pipeline.Pipecomponent;
//import io.vtom.vertx.pipeline.Scope;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class VDBRuntime implements Pipecomponent {
//
//  private Vertx vertx;
//  private JDBCClient client;
//  private List<TSql.VSql> args;
//  private Scope scope;
//
//  VDBRuntime(Vertx vertx, JDBCClient client, Scope scope) {
//    this.vertx = vertx;
//    this.client = client;
//    this.scope = scope;
//    this.args = new ArrayList<>();
//  }
//
//  @Override
//  public VDBRuntime next(TSql arg) {
//    this.args.add(arg.build());
//    return this;
//  }
//
//  @Override
//  public Pipecomponent next(Arg arg) {
//    return null;
//  }
//
//  @Override
//  public DonePromise emit() {
//    EPDonePromiseBuilder promise = Promise.builder().done();
//    if (CollectionKit.isEmpty(this.args)) {
//      NoStackTraceThrowable _thr = new NoStackTraceThrowable(EnoaTipKit.message("No have pipeline."));
//      promise.captures().forEach(capture -> capture.execute(_thr));
//      promise.always().execute();
//      return promise.build();
//    }
//    this.args.sort(Comparator.comparingInt(TSql.VSql::order));
//
//    this.client.getConnection(crn -> {
//      if (crn.failed()) {
//        promise.captures().forEach(capture -> capture.execute(crn.cause()));
//        promise.always().execute();
//        return;
//      }
//      SQLConnection conn = crn.result();
//
//      this.callvsql(promise, conn, 0, null);
//
//    });
//    return promise.build();
//  }
//
//  private void callvsql(EPDonePromiseBuilder promise, SQLConnection conn, int ix, DoneArgPromise runpromise) {
//    if (ix == this.args.size()) {
//      if (runpromise == null) {
//        promise.always().execute();
//        return;
//      }
//      runpromise.capture(thr -> promise.captures().forEach(capture -> capture.execute(thr)));
//      runpromise.always(() -> promise.always().execute());
//      return;
//    }
//    TSql.VSql arg = this.args.get(ix);
//
//
//    if (arg.order() == 0) {
//      DoneArgPromise<Object> dagp = this.vrun(conn, arg);
//      dagp.capture(thr -> promise.captures().forEach(capture -> capture.execute(thr)));
//      this.callvsql(promise, conn, ix + 1, runpromise);
//      return;
//    }
//
//    if (runpromise == null) {
//      runpromise = this.vrun(conn, arg);
//      this.callvsql(promise, conn, ix + 1, runpromise);
//      return;
//    }
//
//    runpromise.done(obj -> {
//      // todo Hook obj {ResultSet, UpdateResultSet}
//      DoneArgPromise<Object> serialdag = this.vrun(conn, arg);
//      this.callvsql(promise, conn, ix + 1, serialdag);
//    })
//      .capture(thr -> promise.captures().forEach(capture -> capture.execute(thr)));
//
//  }
//
//  private DoneArgPromise<Object> vrun(SQLConnection conn, TSql.VSql arg) {
//    EPDoneArgPromiseBuilder<Object> promise = Promise.builder().donearg();
//    switch (arg.action()) {
//      case CALL:
////        conn.callWithParams(arg.sql(), arg.paras(), ar -> {
////          if (ar.failed()) {
////            promise.captures().forEach(capture -> capture.execute(ar.cause()));
////            return;
////          }
////          promise.dones().forEach(done -> done.execute(ar.result()));
////        });
//        break;
//      case UPDATE:
//        conn.updateWithParams(arg.sql(), arg.paras(), this.conncall(promise));
//        break;
//      case SELECT:
//        conn.queryWithParams(arg.sql(), arg.paras(), this.conncall(promise));
//        break;
//      default:
//        promise.captures().forEach(capture -> capture.execute(new NoStackTraceThrowable(EnoaTipKit.message("Not support sql action."))));
//        promise.always().execute();
//        break;
//    }
//    return promise.build();
//  }
//
//  private <T> Handler<AsyncResult<T>> conncall(EPDoneArgPromiseBuilder<Object> promise) {
//    return ar -> {
//      if (ar.failed()) {
//        promise.captures().forEach(capture -> capture.execute(ar.cause()));
//        if (promise.always() != null)
//          promise.always().execute();
//        return;
//      }
//      T result = ar.result();
//      System.out.println(result + " - " + System.nanoTime());
//      promise.dones().forEach(done -> done.execute(result));
//      if (promise.always() != null)
//        promise.always().execute();
//    };
//  }
//
//}
