package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomRedis implements PipeComponent<Redis> {

  private RedisClient client;

  public VtomRedis(RedisClient client) {
    this.client = client;
  }

  public static VtomRedis create(RedisClient client) {
    return new VtomRedis(client);
  }

  @Override
  public VtomRedisStep dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public VtomRedisStep dependency(Pipeline pipeline) {
    return new VtomRedisStep(pipeline, this.client);
  }
}
