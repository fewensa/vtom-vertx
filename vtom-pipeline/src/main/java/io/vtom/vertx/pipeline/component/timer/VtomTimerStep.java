package io.vtom.vertx.pipeline.component.timer;

import io.enoa.toolkit.collection.CollectionKit;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomTimerStep implements PipeStep<Timer> {

  private Pipeline pipeline;
  private List<Step<? extends Timer>> steps;

  public VtomTimerStep(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @Override
  public VtomTimerStep step(StepStack<Timer> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomTimerStep step(Step<? extends Timer> step) {
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
      VtomTimerRunnable runnable = new VtomTimerRunnable(this.pipeline.lifecycle().vertx(), step);
      this.pipeline.next(runnable);
    });
    return this.pipeline;
  }
}
