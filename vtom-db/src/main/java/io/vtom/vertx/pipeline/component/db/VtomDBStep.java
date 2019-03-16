package io.vtom.vertx.pipeline.component.db;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.PipeStep;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.db.sql.TSql;
import io.vtom.vertx.pipeline.component.db.sql.VTSout;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VtomDBStep implements PipeStep<TSql> {

  private Pipeline pipeline;
  private JDBCClient client;
  private List<Step<? extends TSql>> steps;
  private Kv shared;
  private boolean tx;

  VtomDBStep(Pipeline pipeline, JDBCClient client) {
    this.pipeline = pipeline;
    this.client = client;
  }

  public VtomDBStep tx() {
    return this.tx(Boolean.TRUE);
  }

  public VtomDBStep tx(boolean tx) {
    this.tx = tx;
    return this;
  }

  @Override
  public VtomDBStep step(StepStack<TSql> stepstack) {
    this.step(Step.with(stepstack));
    return this;
  }

  @Override
  public VtomDBStep step(Step<? extends TSql> step) {
    if (this.steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(step);
    return this;
  }

  @Override
  public Pipeline join(String id) {
    if (CollectionKit.isEmpty(this.steps))
      return this.pipeline;

    this.shared = this.pipeline.lifecycle().scope().danger(id);
    AtomicInteger arc = (AtomicInteger) this.shared.computeIfAbsent("arc", key -> new AtomicInteger(0));
    if (this.tx)
      this.shared.set("tx", tx);


    this.steps.forEach(step -> this.pipeline.next(this.piperunable(step)));
    arc.set(arc.get() + this.steps.size());
    return this.pipeline;
  }

  private PipeRunnable<TSql, VTSout> piperunable(Step<? extends TSql> step) {
    return new VtomDBPipeRunnable(this.client, step, this.shared);
  }


}
