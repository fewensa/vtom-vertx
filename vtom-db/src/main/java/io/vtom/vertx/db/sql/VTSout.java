package io.vtom.vertx.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface VTSout extends StepOUT {

  SqlAction action();

  String sql();

  JsonArray paras();

}
