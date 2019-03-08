package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.runnable.Piperunnable;

public interface Pipeline {

  static Pipeline pipeline(Vertx vertx) {
    return pipeline(vertx, Scope.scope());
  }

  static Pipeline pipeline(Vertx vertx, Scope scope) {
    return new PipelineImpl(vertx, scope);
  }

  Pipecycle cycle();

  Pipeline next(Piperunnable piperunnable);

  Pipepromise enqueue();

}
