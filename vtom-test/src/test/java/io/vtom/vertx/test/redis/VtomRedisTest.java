package io.vtom.vertx.test.redis;

import io.enoa.toolkit.sys.EnvKit;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import io.vtom.vertx.pipeline.component.redis.Redis;
import io.vtom.vertx.pipeline.component.redis.VtomRedis;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

public class VtomRedisTest {


  private TestSuite suite;
  private Vertx vertx;
  private VtomRedis vtomredis;

  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("redis");
    RedisOptions config = new RedisOptions()
      .setHost(EnvKit.string("REDIS_HOST", "127.0.0.1"));
    RedisClient redis = RedisClient.create(vertx, config);
    this.vtomredis = VtomRedis.create(redis);
  }


  @Test
  public void testGet() {
    this.suite.test("get", ctx -> {
      Async async = ctx.async();
      this.vtomredis.dependency(this.vertx)
        .step(Step.with(lifecycle -> Redis.set("name", "Foo")).ord(1))
        .step(Step.with(lifecycle -> Redis.get("name")).ord(2))
        .step(Step.with(lifecycle -> Redis.del("name")).ord(3))
        .join()
        .enqueue()
        .done(lifecycle -> {
          String name = lifecycle.scope().value(2).as();
          System.out.println(name);
        })
        .capture(Throwable::printStackTrace)
        .always(async::complete);
      async.awaitSuccess();
    }).run();
  }


}
