package io.vtom.vertx.pipeline.component.web.client;

import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomHttpClientStep implements PipeStep<Client> {

  private Pipeline pipeline;
  private List<Step<? extends Client>> steps;
  private Kv shared;

  public VtomHttpClientStep(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @Override
  public VtomHttpClientStep step(StepStack<Client> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomHttpClientStep step(Step<? extends Client> step) {
    if (step == null)
      return this;
    if (steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (this.steps == null)
      return this.pipeline;
    this.shared = this.pipeline.lifecycle().scope().danger(id);

//    this.steps.forEach(step -> this.pipeline.next(new VtomWebClientRunnable(this.pipeline.lifecycle().vertx(), step, this.shared)));
    this.steps.forEach(step -> this.pipeline.next(new VtomHttpClientRunnable(this.pipeline.lifecycle().vertx(), step, this.shared)));
    return this.pipeline;
  }
}
