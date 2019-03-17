package io.vtom.vertx.test.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vtom.vertx.pipeline.component.http.client.Vhc;
import io.vtom.vertx.pipeline.component.http.client.VtomHttpClient;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

public class VtomHttpClientTest {


  private TestSuite suite;
  private Vertx vertx;
  private VtomHttpClient httpclient;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("periodic");
    this.httpclient = VtomHttpClient.create();
  }


  @Test
  public void testGet() {
    this.suite.test("get", ctx -> {
      Async async = ctx.async();
      this.httpclient.dependency(this.vertx)
        .step(Step.with(lifecycle -> Vhc.request(HttpMethod.GET, "https://httpbin.org/get").absolute()).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }


}
