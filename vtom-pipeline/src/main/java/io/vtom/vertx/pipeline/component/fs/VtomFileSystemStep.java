package io.vtom.vertx.pipeline.component.fs;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.fs.action.Fs;
import io.vtom.vertx.pipeline.component.fs.action.VtmFsOut;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.ArrayList;
import java.util.List;

public class VtomFileSystemStep implements PipeStep<Fs> {

  private Pipeline pipeline;
  private Vertx vertx;
  private List<StepWrapper<? extends Fs>> wrappers;
  private Kv shared;

  public VtomFileSystemStep(Vertx vertx, Pipeline pipeline) {
    this.vertx = vertx;
    this.pipeline = pipeline;
  }

  @Override
  public VtomFileSystemStep step(StepStack<Fs> stepstack) {
    return this.step(Step.with(stepstack));
  }

  @Override
  public VtomFileSystemStep step(Step<? extends Fs> step) {
    StepWrapper<? extends Fs> wrapper = step._wrapper();
    if (this.wrappers == null)
      this.wrappers = new ArrayList<>();
    this.wrappers.add(wrapper);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (CollectionKit.isEmpty(this.wrappers))
      return this.pipeline;
    this.wrappers.forEach(wrapper -> this.pipeline.next(this.piperunable(wrapper)));
    return this.pipeline;
  }

  private PipeRunnable<Fs, VtmFsOut> piperunable(StepWrapper<? extends Fs> wrapper) {
    return new VtomFileSystemRunnable(this.vertx, this.pipeline, wrapper, this.shared);
  }
}
