package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;
import io.vtom.vertx.pipeline.step.StepSkip;

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
  Timer skip(StepSkip stepskip);

  @Override
  StepOUT out();
}
