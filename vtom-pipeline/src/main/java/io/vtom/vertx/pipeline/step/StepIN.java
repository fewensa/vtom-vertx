package io.vtom.vertx.pipeline.step;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public interface StepIN {

  StepIN skip(Handler<Skip> stepskip);

  StepOUT out();


}
