package io.vtom.vertx.test.db;

import com.alibaba.druid.pool.DruidDataSource;
import io.enoa.stove.firetpl.enjoy.EnjoyFiretpl;
import io.enoa.toolkit.path.PathKit;
import io.enoa.toolkit.prop.PropKit;
import io.enoa.toolkit.sys.EnvKit;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.unit.report.ReportOptions;
import io.vtom.vertx.pipeline.component.db.VtomDB;
import io.vtom.vertx.pipeline.component.db.data.DBConverter;
import io.vtom.vertx.pipeline.component.db.data.Row;
import io.vtom.vertx.pipeline.component.db.data.Table;
import io.vtom.vertx.pipeline.component.db.dialect.PostgreSQLDialect;
import io.vtom.vertx.pipeline.component.db.page.Page;
import io.vtom.vertx.pipeline.component.db.sql.TSql;
import io.vtom.vertx.pipeline.component.db.sql.TSqlOptions;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

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
    EnjoyFiretpl firetpl = new EnjoyFiretpl(PathKit.debugPath().resolve("src/test/resources/sqls"), "template.sql");
    TSql.epm().install(new TSqlOptions().setFiretpl(firetpl).setShowSql(true).setDialect(new PostgreSQLDialect()));
  }

  private JDBCClient jdbcclient(Vertx vertx) {
    DruidDataSource dds = new DruidDataSource();
    dds.setUrl(EnvKit.string("DB_URL", PropKit.string("rds.db.url")));
    dds.setUsername(EnvKit.string("DB_USER", PropKit.string("rds.db.user")));
    dds.setPassword(EnvKit.string("DB_PASSWD", PropKit.string("rds.db.passwd")));
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

      this.vtomdb.dependency(this.vertx)
        .tx()
        .step(Step.with(cycle -> {
          System.out.println(11111);
          return TSql.sql().select("select * from t_article", 1, 10);
        }).ord(2).after(1))
        .step(Step.with(cycle -> {
          System.out.println(2222);
          Scope scope = cycle.scope();
          return TSql.sql().select("select * from t_tags where aid in ('1')");
        }).ord(1))
        .join()
        .enqueue()
        .done(cycle -> {
          Scope scope = cycle.scope();
          System.out.println("DONE");
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
  public void testTSql() {
    this.suite.test("tsql", ctx -> {
      Async async = ctx.async();

      this.vtomdb.dependency(this.vertx)
        .step(Step.with(cycle -> TSql.template().select("Tpl.articleList", 1, 10)).ord(1))
        .step(Step.with(cycle -> TSql.template().select("Tpl.tagList")).ord(2))
        .join()
        .enqueue()
        .done(cycle -> {
          Scope scope = cycle.scope();
          Table table = scope.value(2).to(DBConverter.toTable());
          List<Row> rows = table.rows();
          Row row = rows.get(0);
          row.set("aa", "22");

          Page<Row> page = scope.value(1).to(DBConverter.toPageRow());


          System.out.println("DONE");
        })
        .capture(Throwable::printStackTrace)
        .always(() -> {
          System.out.println("Always. -ext.sql.ResultSet@-------- - " + System.nanoTime());
          async.complete();
        });

      async.awaitSuccess();

    }).run(new TestOptions().addReporter(new ReportOptions().setTo("console")));
  }

}
