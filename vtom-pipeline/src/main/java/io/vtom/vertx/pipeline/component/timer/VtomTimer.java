package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomTimer implements PipeComponent<Timer> {


  public VtomTimer() {
  }

  public static VtomTimer create() {
    return new VtomTimer();
  }

  @Override
  public PipeStep<Timer> dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public PipeStep<Timer> dependency(Pipeline pipeline) {
    return new VtomTimerStep(pipeline);
  }
}
