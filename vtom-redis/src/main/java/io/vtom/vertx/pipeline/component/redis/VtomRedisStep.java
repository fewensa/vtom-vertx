package io.vtom.vertx.pipeline.component.redis;

import io.vertx.redis.RedisClient;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomRedisStep implements PipeStep<Redis> {


  private Pipeline pipeline;
  private RedisClient client;
  private List<Step<? extends Redis>> steps;


  public VtomRedisStep(Pipeline pipeline, RedisClient client) {
    this.pipeline = pipeline;
    this.client = client;
  }

  @Override
  public VtomRedisStep step(StepStack<Redis> stepstack) {
    this.step(Step.with(stepstack));
    return this;
  }

  @Override
  public VtomRedisStep step(Step<? extends Redis> step) {
    if (this.steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (this.steps == null)
      return this.pipeline;
    this.steps.forEach(step -> this.pipeline.next(new VtmRedisRunnable(this.client, step)));
    return this.pipeline;
  }
}
