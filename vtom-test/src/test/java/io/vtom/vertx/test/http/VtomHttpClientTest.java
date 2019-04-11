package io.vtom.vertx.test.http;

import io.enoa.toolkit.path.PathKit;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vtom.vertx.pipeline.component.web.client.Client;
import io.vtom.vertx.pipeline.component.web.client.IRequestReporter;
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
        .step(Step.with(lifecycle -> Client.request(HttpMethod.GET, "https://httpbin.org/get")
          .absolute()
          .report(IRequestReporter.def())
        ).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
          HttpClientResponse response = lifecycle.scope().value(1).as();
          response.handler(buffer -> System.out.println(buffer.toString()));
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
        .step(Step.with(lifecycle -> Client.request(HttpMethod.POST, "https://httpbin.org/post")
          .absolute()
          .report(IRequestReporter.def())
          .header("token", "248525d")
          .para("name", "vtom")
          .cookie("sess","2484")
          .file("f", PathKit.debugPath().resolve("src/test/resources/rds.properties").toString())
        ).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
          HttpClientResponse response = lifecycle.scope().value(1).as();
          response.handler(buffer -> System.out.println(buffer.toString()));
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

  @Test
  public void testRaw() {
    this.suite.test("raw", ctx -> {
      Async async = ctx.async();
      this.httpclient.dependency(this.vertx)
        .step(Step.with(lifecycle -> Client.request(HttpMethod.POST, "https://httpbin.org/post")
          .report(IRequestReporter.def())
          .header("content-type", "application/json")
          .para("name", "vtom")
          .raw("{\"age\": 15}")
        ).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> {
          HttpClientResponse response = lifecycle.scope().value(1).as();
          response.handler(buffer -> System.out.println(buffer.toString()));
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

  @Test
  public void testMulti() {
    this.suite.test("multi", ctx -> {
      Async async = ctx.async(2);
      this.httpclient.dependency(this.vertx)
        .step(Step.with(lifecycle -> Client.request(HttpMethod.GET, "https://httpbin.org/get").absolute().report(IRequestReporter.def())).ord(1))
        .step(Step.with(lifecycle -> Client.request(HttpMethod.POST, "https://httpbin.org/post")
          .absolute()
          .report(IRequestReporter.def())
          .header("token", "248525d")
          .para("name", "vtom")
          .cookie("sess","2484")
          .file("f", PathKit.debugPath().resolve("src/test/resources/rds.properties").toString())
        ).ord(2))
        .join()
        .enqueue()
        .done(lifecycle -> {
          // fixme: not handler
          HttpClientResponse response0 = lifecycle.scope().value(1).as();
          response0.bodyHandler(buffer -> {
            System.out.println(buffer.toString());
            async.countDown();
          }).exceptionHandler(Throwable::printStackTrace);

          HttpClientResponse response1 = lifecycle.scope().value(2).as();
          response1.bodyHandler(buffer -> {
            System.out.println(buffer.toString());
            async.countDown();
          }).exceptionHandler(Throwable::printStackTrace);

//          HttpClientResponse response2 = lifecycle.scope().value(3).as();
//          response2.bodyHandler(buffer -> {
//            System.out.println(buffer.toString());
//            async.countDown();
//          }).exceptionHandler(Throwable::printStackTrace);
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

}
