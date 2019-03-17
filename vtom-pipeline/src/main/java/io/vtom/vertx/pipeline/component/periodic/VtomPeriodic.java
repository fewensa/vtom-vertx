package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomPeriodic implements PipeComponent<Periodic> {



  public VtomPeriodic() {
  }

  public static VtomPeriodic create() {
    return new VtomPeriodic();
  }

  @Override
  public VtomPeriodicStep dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public VtomPeriodicStep dependency(Pipeline pipeline) {
    return new VtomPeriodicStep(pipeline);
  }
}
