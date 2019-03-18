package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface Periodic extends StepIN {

  static VtmPeriodicSubmit submit(Handler<Long> handler) {
    return new VtmPeriodicSubmit(handler);
  }

  static VtmPeriodicCancel cancel(long id) {
    return new VtmPeriodicCancel(id);
  }

  static VtmPeriodicStream stream(long delay) {
    return new VtmPeriodicStream(delay);
  }

  @Override
  Periodic skip(Handler<Skip> stepskip);

  @Override
  StepOUT out();
}
