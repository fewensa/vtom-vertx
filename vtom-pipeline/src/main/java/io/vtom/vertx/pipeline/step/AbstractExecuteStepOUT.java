package io.vtom.vertx.pipeline.step;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.List;

public abstract class AbstractExecuteStepOUT extends AbstractStepOUT {

  public AbstractExecuteStepOUT(List<Handler<Skip>> skips) {
    super(skips);
  }

  public abstract void execute(Vertx vertx, Handler<AsyncResult<Object>> handler);

}

