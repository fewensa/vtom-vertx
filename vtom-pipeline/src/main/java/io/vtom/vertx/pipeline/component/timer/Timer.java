package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface Timer extends StepIN {


  static VtmTimerSubmit submit(Handler<Long> handler) {
    return new VtmTimerSubmit(handler);
  }

  static VtmTimerCancel cancel(long id) {
    return new VtmTimerCancel(id);
  }

  static VtmTimerStream stream(long delay) {
    return new VtmTimerStream(delay);
  }


  @Override
  Timer skip(Handler<Skip> stepskip);

  @Override
  StepOUT out();
}
