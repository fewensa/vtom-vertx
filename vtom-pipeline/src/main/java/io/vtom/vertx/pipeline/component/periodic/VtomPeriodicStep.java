package io.vtom.vertx.pipeline.component.periodic;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomPeriodicStep implements PipeStep<Periodic> {

  private Vertx vertx;
  private Pipeline pipeline;
  private List<Step<? extends Periodic>> steps;

  public VtomPeriodicStep(Vertx vertx, Pipeline pipeline) {
    this.vertx = vertx;
    this.pipeline = pipeline;
  }

  @Override
  public VtomPeriodicStep step(StepStack<Periodic> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomPeriodicStep step(Step<? extends Periodic> step) {
    if (step == null)
      return this;
    if (this.steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (CollectionKit.isEmpty(this.steps))
      return this.pipeline;
    this.steps.forEach(step -> {
      VtomPeriodicRunnable runnable = new VtomPeriodicRunnable(this.vertx, step);
      this.pipeline.next(runnable);
    });
    return this.pipeline;
  }

}
