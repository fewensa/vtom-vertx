package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.redis.RedisClient;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.step.Step;

class VtmRedisRunnable implements PipeRunnable<Redis, VtmRedisOUT> {

  private RedisClient client;
  private Step<? extends Redis> step;

  VtmRedisRunnable(RedisClient client, Step<? extends Redis> step) {
    this.client = client;
    this.step = step;
  }

  @Override
  public Step<? extends Redis> step() {
    return this.step;
  }

  @Override
  public void call(VtmRedisOUT stepout, Handler<AsyncResult<Object>> handler) {
    stepout.execute(this.client, handler);
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {
    handler.handle(Future.succeededFuture());
  }
}
