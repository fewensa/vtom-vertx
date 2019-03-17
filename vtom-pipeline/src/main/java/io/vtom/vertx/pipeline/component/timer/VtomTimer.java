package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomTimer implements PipeComponent<Timer> {

  private Vertx vertx;

  public VtomTimer(Vertx vertx) {
    this.vertx = vertx;
  }

  public static VtomTimer with(Vertx vertx) {
    return new VtomTimer(vertx);
  }

  @Override
  public PipeStep<Timer> component() {
    return this.dependency(Pipeline.pipeline());
  }

  @Override
  public PipeStep<Timer> dependency(Pipeline pipeline) {
    return new VtomTimerStep(this.vertx, pipeline);
  }
}
