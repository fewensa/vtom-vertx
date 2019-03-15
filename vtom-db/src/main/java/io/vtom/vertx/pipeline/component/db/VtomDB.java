package io.vtom.vertx.pipeline.component.db;

import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.pipeline.component.db.sql.TSql;
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
  public VtomDBStep component() {
    return this.dependency(Pipeline.pipeline());
  }

  @Override
  public VtomDBStep dependency(Pipeline pipeline) {
    return new VtomDBStep(pipeline, this.client);
  }

}
