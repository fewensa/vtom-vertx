package io.vtom.vertx.db;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.db.runnable._VtomDBPipeRunnable;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.runnable.Piperunnable;
import io.vtom.vertx.pipeline.step.Pipestack;
import io.vtom.vertx.pipeline.step.Pipestep;

import java.util.ArrayList;
import java.util.List;

class VtomDBStep implements Pipestep<TSql> {

  private Pipeline pipeline;
  private JDBCClient client;
  private List<Pipestack<TSql>> steps;

  VtomDBStep(Pipeline pipeline, JDBCClient client) {
    this.pipeline = pipeline;
    this.client = client;
  }

  @Override
  public Pipestep<TSql> step(Pipestack<TSql> pipestack) {
//    TSql tsql = pipestack.back(this.pipeline.cycle());
    if (this.steps == null)
      this.steps = new ArrayList<>();
    this.steps.add(pipestack);
    return this;
  }

  @Override
  public void load() {
    if (CollectionKit.isEmpty(this.steps))
      return;
    this.steps.forEach(tsql -> this.pipeline.next(this.piperunable(tsql)));
  }

  private Piperunnable piperunable(Pipestack<TSql> pipestack) {
    return new _VtomDBPipeRunnable(this.pipeline, this.client, pipestack);
  }


}
