package io.vtom.vertx.db;

import com.alibaba.druid.pool.DruidDataSource;
import io.enoa.toolkit.prop.PropKit;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import io.vtom.vertx.db.sql.SqlAction;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.pipeline.scope.Scope;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class VtomDBTest {

  private TestSuite suite;
  private VtomDB vtomdb;
  private Vertx vertx;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("db");
    JDBCClient jdbcclient = this.jdbcclient(vertx);
    this.vtomdb = VtomDB.create(jdbcclient);
  }

  private JDBCClient jdbcclient(Vertx vertx) {
    DruidDataSource dds = new DruidDataSource();
    dds.setUrl(PropKit.string("rds.db.url"));
    dds.setUsername(PropKit.string("rds.db.user"));
    dds.setPassword(PropKit.string("rds.db.passwd"));
    dds.setInitialSize(10);
    dds.setMinIdle(10);
    dds.setMaxActive(100);
    dds.setValidationQuery("select 1");
    return JDBCClient.create(vertx, dds);
  }

  @Test
  public void ord() {
    this.suite.test("ord", ctx -> {
      Async async = ctx.async();

      this.vtomdb.component()
        .tx()
        .step(Step.with(cycle -> {
          System.out.println(11111);
          return TSql.def(SqlAction.SELECT).sql("select * from t_media");
        }).ord(0).after(1))
        .step(Step.with(cycle -> {
          System.out.println(2222);
          Scope scope = cycle.scope();
          return TSql.def(SqlAction.SELECT).sql("select * from t_tags where mid in ('1')");
        }).ord(0))
        .join()
        .enqueue()
        .done(cycle -> {
          Scope scope = cycle.scope();
          System.out.println(scope);
        })
        .capture(Throwable::printStackTrace)
        .always(() -> {
          System.out.println("Always. -ext.sql.ResultSet@-------- - " + System.nanoTime());
          async.complete();
        });

      async.awaitSuccess();

      try {
        TimeUnit.SECONDS.sleep(5L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }).run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
  }


}
