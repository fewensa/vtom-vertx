package io.vtom.vertx.db;

import com.alibaba.druid.pool.DruidDataSource;
import io.enoa.toolkit.prop.PropKit;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.scope.Scope;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

      Pipeline pipeline = Pipeline.pipeline(this.vertx, Scope.context());

      this.vtomdb.dependency(pipeline)
        .step(pipecycle -> TSql.create(TSql.Action.SELECT, "select * from t_media").ord(1))
        .step(pipecycle -> {
          Scope scope = pipecycle.scope();
          return TSql.create(TSql.Action.SELECT, "select * from t_tags where mid in ('1')").ord(2).after(1);
        })
        .load();

      pipeline.enqueue()
        .done(pipecycle -> {
          Scope scope = pipecycle.scope();
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

  @Test
  public void testList() {
    List<String> arr = new ArrayList<String>(){{
      add("A");
      add("B");
      add("C");
      add("D");
    }};

    System.out.println(arr);
//    Collections.swap(arr, 0, 3);
//    System.out.println(arr);

    Collections.rotate(arr.subList(1, 3), -1);
    System.out.println(arr);
  }

}
