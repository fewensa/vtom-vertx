package io.vtom.vertx.test.timer;

import io.vertx.core.TimeoutStream;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vtom.vertx.pipeline.component.timer.Timer;
import io.vtom.vertx.pipeline.component.timer.VtomTimer;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

public class VtomTimerTest {


  private TestSuite suite;
  private Vertx vertx;
  private VtomTimer vtomtimer;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("timer");
    this.vtomtimer = VtomTimer.create();
  }


  @Test
  public void testSubmitParallel() {
    this.suite.test("submit.parallel", ctx -> {
      Async async = ctx.async();
      this.vtomtimer.dependency(this.vertx)
        .step(Step.with(lifecycle -> Timer.submit(System.out::println).delay(100)).id("timer.parallel"))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("parallel id: " + lifecycle.scope().value("timer.parallel").as()))
        .always(async::complete);// always scope never call. because parallel Timer submit will return immediately
      async.awaitSuccess();
    }).run(new TestOptions().setTimeout(1000));
  }

  @Test
  public void testSubmitSerial() {
    this.suite.test("submit.serial", ctx -> {
      Async async = ctx.async();
      this.vtomtimer.dependency(this.vertx)
        .step(Step.with(lifecycle -> Timer.submit(System.out::println).delay(100)).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("serial timer: " + lifecycle.scope().value(1).as()))
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

  @Test
  public void testStream() {
    this.suite.test("stream", ctx -> {
      Async async = ctx.async();
      // always scope never call. because parallel Timer submit will return immediately
      this.vtomtimer.dependency(this.vertx)
        .step(Step.with(lifecycle -> Timer.stream(100).handler(id -> {
          System.out.println(id);
          Scope scope = lifecycle.scope();
          TimeoutStream timeoutstream = scope.value("timer.stream").as();
          timeoutstream.cancel();
        })).id("timer.stream"))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("timer stream"))
        .always(async::complete);// always scope never call. because parallel Timer submit will return immediately
      async.awaitSuccess();
    }).run(new TestOptions().setTimeout(1000));
  }

}
