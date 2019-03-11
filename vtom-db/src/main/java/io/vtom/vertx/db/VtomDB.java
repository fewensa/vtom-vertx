package io.vtom.vertx.db;

import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.db.sql.TSql;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;

public class VtomDB implements PipeComponent<TSql> {

  public static VtomDB create(JDBCClient client) {
    return new VtomDB(client);
  }

  private JDBCClient client;

  private VtomDB(JDBCClient client) {
    this.client = client;
  }

  @Override
  public VtomDBStep dependency(Pipeline pipeline) {
    return new VtomDBStep(pipeline, this.client);
  }

}
