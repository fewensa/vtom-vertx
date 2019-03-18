package io.vtom.vertx.test.http;

import io.enoa.toolkit.path.PathKit;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vertx.ext.web.client.HttpResponse;
import io.vtom.vertx.pipeline.component.web.client.Vhc;
import io.vtom.vertx.pipeline.component.web.client.VtomWebClient;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

public class VtomHttpClientTest {


  private TestSuite suite;
  private Vertx vertx;
  private VtomWebClient httpclient;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("web-client");
    this.httpclient = VtomWebClient.create();
  }


  @Test
  public void testGet() {
    this.suite.test("get", ctx -> {
      Async async = ctx.async();
      this.httpclient.dependency(this.vertx)
        .step(Step.with(lifecycle -> Vhc.request(HttpMethod.GET, "https://httpbin.org/get")
          .absolute()
        ).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
          HttpResponse<Buffer> response = lifecycle.scope().value(1).as();
          System.out.println(response.body().toString());
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

  @Test
  public void testUpload() {
    this.suite.test("upload", ctx -> {
      Async async = ctx.async();
      this.httpclient.dependency(this.vertx)
        .step(Step.with(lifecycle -> Vhc.request(HttpMethod.POST, "https://httpbin.org/post")
          .absolute()
          .header("token", "248525d")
          .para("name", "vtom")
          .cookie("sess","2484")
          .file("f", PathKit.debugPath().resolve("src/test/resources/rds.properties").toString())
        ).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
          HttpResponse<Buffer> response = lifecycle.scope().value(1).as();
          System.out.println(response.body().toString());
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }


}
