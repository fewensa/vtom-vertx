package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vtom.vertx.pipeline.step.StepOUT;

public interface VtmFsOut extends StepOUT {


  void execute(FileSystem fs, Handler<AsyncResult<Object>> handler);

}
