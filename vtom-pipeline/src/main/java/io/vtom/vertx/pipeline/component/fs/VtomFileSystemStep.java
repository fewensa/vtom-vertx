package io.vtom.vertx.pipeline.component.fs;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.fs.action.Fs;
import io.vtom.vertx.pipeline.component.fs.action.VtmFsOut;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;

public class VtomFileSystemStep implements PipeStep<Fs> {

  private Pipeline pipeline;
  private List<Step<? extends Fs>> steps;
  private Kv shared;

  public VtomFileSystemStep(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @Override
  public VtomFileSystemStep step(StepStack<Fs> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomFileSystemStep step(Step<? extends Fs> step) {
    if (this.steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (CollectionKit.isEmpty(this.steps))
      return this.pipeline;
    this.steps.forEach(step -> this.pipeline.next(this.piperunable(step)));
    return this.pipeline;
  }

  private PipeRunnable<Fs, VtmFsOut> piperunable(Step<? extends Fs> step) {
    return new VtomFileSystemRunnable(this.pipeline.lifecycle().vertx(), step, this.shared);
  }
}
