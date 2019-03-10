package io.vtom.vertx.db;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.db.runnable._VtomDBPipeRunnable;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.db.sql.VTSout;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.Piperunnable;
import io.vtom.vertx.pipeline.Pipestep;
import io.vtom.vertx.pipeline.step.Step;
import io.vtom.vertx.pipeline.step.StepWrapper;

import java.util.ArrayList;
import java.util.List;

class VtomDBStep implements Pipestep<TSql> {

  private Pipeline pipeline;
  private JDBCClient client;
  private List<StepWrapper<TSql>> wrappers;

  VtomDBStep(Pipeline pipeline, JDBCClient client) {
    this.pipeline = pipeline;
    this.client = client;
  }

  @Override
  public Pipestep<TSql> step(Step<TSql> step) {
    StepWrapper<TSql> wrapper = step._wrapper();
    if (this.wrappers == null)
      this.wrappers = new ArrayList<>();
    this.wrappers.add(wrapper);
    return this;
  }

  @Override
  public Pipeline join() {
    if (CollectionKit.isEmpty(this.wrappers))
      return this.pipeline;

    this.wrappers.forEach(wrapper -> this.pipeline.next(this.piperunable(wrapper)));
    return this.pipeline;
  }

  private Piperunnable<TSql, VTSout> piperunable(StepWrapper<TSql> wrapper) {
    return new _VtomDBPipeRunnable(this.pipeline, this.client, wrapper);
  }


}
