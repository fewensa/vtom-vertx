package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.redis.RedisClient;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.AbstractStepOUT;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;

abstract class VtmRedisOUT extends AbstractStepOUT implements StepOUT {

  VtmRedisOUT(List<Handler<Skip>> skips) {
    super(skips);
  }

  public abstract void execute(RedisClient client, Handler<AsyncResult<Object>> handler);

}
