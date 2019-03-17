package io.vtom.vertx.test.periodic;

import io.vertx.core.TimeoutStream;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestOptions;
import io.vertx.ext.unit.TestSuite;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.periodic.Periodic;
import io.vtom.vertx.pipeline.component.periodic.VtomPeriodic;
import io.vtom.vertx.pipeline.component.timer.Timer;
import io.vtom.vertx.pipeline.component.timer.VtomTimer;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class VtomPeriodicTest {

  private TestSuite suite;
  private Vertx vertx;
  private VtomPeriodic vtomperiodic;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("periodic");
    this.vtomperiodic = VtomPeriodic.with(this.vertx);
  }


  @Test
  public void testSubmitParallel() {
    this.suite.test("submit.parallel", ctx -> {
      Async async = ctx.async();
      this.vtomperiodic.component()
        .step(Step.with(lifecycle -> Periodic.submit(System.out::println).delay(100)).id("periodic.parallel"))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("parallel id: " + lifecycle.scope().value("periodic.parallel").as()))
        .always(async::complete);// always scope never call. because parallel Periodic submit will return immediately
      async.awaitSuccess();
    }).run(new TestOptions().setTimeout(1000));
  }

  @Test
  public void testSubmitSerial() {
    this.suite.test("submit.serial", ctx -> {
      Async async = ctx.async();
      this.vtomperiodic.component()
        .step(Step.with(lifecycle -> Periodic.submit(System.out::println).delay(100)).ord(1))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("serial periodic: " + lifecycle.scope().value(1).as()))
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }

  @Test
  public void testStream() {
    this.suite.test("stream", ctx -> {
      Async async = ctx.async();
      AtomicInteger ai = new AtomicInteger();
      // always scope never call. because parallel Periodic submit will return immediately
      this.vtomperiodic.component()
        .step(Step.with(lifecycle -> Periodic.stream(100).handler(id -> {
          int i = ai.addAndGet(1);
          System.out.println(i);
          if (i < 5)
            return;
          Scope scope = lifecycle.scope();
          TimeoutStream timeoutstream = scope.value("periodic.stream").as();
          timeoutstream.cancel();
        })).id("periodic.stream"))
        .join()
        .enqueue()
        .done(lifecycle -> System.out.println("periodic stream"))
        .always(async::complete);// always scope never call. because parallel Periodic submit will return immediately
      async.awaitSuccess();
    }).run(new TestOptions().setTimeout(1000));
  }

  @Test
  public void testMix() {
    this.suite.test("submit.parallel", ctx -> {
      Async async = ctx.async();

      Pipeline pipeline = Pipeline.pipeline();

      this.vtomperiodic.dependency(pipeline)
        .step(Step.with(lifecycle -> Periodic.submit(System.out::println).delay(100)).id("periodic.parallel"))
        .join();

      VtomTimer.with(this.vertx).dependency(pipeline)
        .step(lifecycle -> Timer.submit(id -> {
          Long pid = lifecycle.scope().value("periodic.parallel").as();
          this.vertx.cancelTimer(pid);
          System.out.println("Canceled periodic -> " + pid);
        }).delay(1000))
        .join();

      pipeline.enqueue()
        .done(lifecycle -> System.out.println("parallel id: " + lifecycle.scope().value("periodic.parallel").as()))
        .always(async::complete);// always scope never call. because parallel Periodic submit will return immediately
      async.awaitSuccess();
    }).run(new TestOptions().setTimeout(3000));
  }

}
