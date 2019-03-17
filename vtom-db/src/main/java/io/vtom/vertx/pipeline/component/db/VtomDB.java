package io.vtom.vertx.pipeline.component.db;

import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.db.sql.TSql;

public class VtomDB implements PipeComponent<TSql> {

  public static VtomDB create(JDBCClient client) {
    return new VtomDB(client);
  }

  private JDBCClient client;

  private VtomDB(JDBCClient client) {
    this.client = client;
  }

  @Override
  public VtomDBStep dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public VtomDBStep dependency(Pipeline pipeline) {
    return new VtomDBStep(pipeline, this.client);
  }

}
