package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomWebClient implements PipeComponent<Vhc> {


  public static VtomWebClient create() {
    return new VtomWebClient();
  }

  public VtomWebClient() {
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
