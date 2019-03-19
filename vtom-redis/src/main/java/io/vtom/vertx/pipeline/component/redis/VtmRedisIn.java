package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.redis.RedisClient;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.ArrayList;
import java.util.List;

abstract class VtmRedisIn implements Redis {


  private List<Handler<Skip>> stepskips;

  @Override
  public Redis skip(Handler<Skip> stepskip) {
    if (this.stepskips == null)
      this.stepskips = new ArrayList<>();
    this.stepskips.add(stepskip);
    return this;
  }

  List<Handler<Skip>> stepskips() {
    return this.stepskips;
  }

  static VtmRedisIn with(CommandCaller caller) {
    return new VtmRedisIn() {
      @Override
      public StepOUT out() {
        return new VtmRedisOUT(stepskips()) {
          @Override
          public void execute(RedisClient client, Handler<AsyncResult<Object>> handler) {
            caller.call(client, handler);
          }
        };
      }
    };
  }

  interface CommandCaller {
    void call(RedisClient client, Handler<AsyncResult<Object>> handler);
  }

}
