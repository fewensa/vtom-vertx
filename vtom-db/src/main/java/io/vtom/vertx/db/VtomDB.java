package io.vtom.vertx.db;

import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.pipeline.Pipecomponent;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.step.Pipestep;

public class VtomDB implements Pipecomponent<TSql> {

  public static VtomDB create(JDBCClient client) {
    return new VtomDB(client);
  }

  private JDBCClient client;

  private VtomDB(JDBCClient client) {
    this.client = client;
  }

  @Override
  public Pipestep<TSql> dependency(Pipeline pipeline) {
    return new VtomDBStep(pipeline, this.client);
  }

}
