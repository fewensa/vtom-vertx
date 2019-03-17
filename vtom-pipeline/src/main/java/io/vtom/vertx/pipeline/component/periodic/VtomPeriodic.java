package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomPeriodic implements PipeComponent<Periodic> {


  private Vertx vertx;

  public VtomPeriodic(Vertx vertx) {
    this.vertx = vertx;
  }

  public static VtomPeriodic with(Vertx vertx) {
    return new VtomPeriodic(vertx);
  }

  @Override
  public VtomPeriodicStep component() {
    return this.dependency(Pipeline.pipeline());
  }

  @Override
  public VtomPeriodicStep dependency(Pipeline pipeline) {
    return new VtomPeriodicStep(this.vertx, pipeline);
  }
}
