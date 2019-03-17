package io.vtom.vertx.pipeline.component.http.client;

import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomHttpClientStep implements PipeStep<Vhc> {

  private Pipeline pipeline;
  private List<Step<? extends Vhc>> steps;

  public VtomHttpClientStep(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @Override
  public VtomHttpClientStep step(StepStack<Vhc> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomHttpClientStep step(Step<? extends Vhc> step) {
    if (steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (this.steps == null)
      return this.pipeline;
    this.steps.forEach(step -> this.pipeline.next(new VtomHttpClientRunnable(this.pipeline.lifecycle().vertx(), step)));
    return this.pipeline;
  }
}
