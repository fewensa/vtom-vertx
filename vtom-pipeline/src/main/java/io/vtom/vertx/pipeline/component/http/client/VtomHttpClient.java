package io.vtom.vertx.pipeline.component.http.client;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomHttpClient implements PipeComponent<Vhc> {


  public static VtomHttpClient create() {
    return new VtomHttpClient();
  }

  public VtomHttpClient() {
  }

  @Override
  public PipeStep<Vhc> dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public PipeStep<Vhc> dependency(Pipeline pipeline) {
    return new VtomHttpClientStep(pipeline);
  }
}
