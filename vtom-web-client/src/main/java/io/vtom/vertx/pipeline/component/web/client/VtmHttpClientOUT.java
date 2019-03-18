package io.vtom.vertx.pipeline.component.web.client;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractStepOUT;

import java.util.List;

public abstract class VtmHttpClientOUT extends AbstractStepOUT implements VtmVhcOUT {
  public VtmHttpClientOUT(List<Handler<Skip>> skips) {
    super(skips);
  }

}
