package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.promise.Pipepromise;
import io.vtom.vertx.pipeline.runnable.Piperunnable;
import io.vtom.vertx.pipeline.scope.Scope;
import io.vtom.vertx.pipeline.scope.ScopeContext;

public interface Pipeline {

  static Pipeline pipeline(Vertx vertx) {
    return pipeline(vertx, Scope.context());
  }

  static Pipeline pipeline(Vertx vertx, ScopeContext context) {
    return new PipelineImpl(vertx, context);
  }

  Pipecycle cycle();

  Pipeline next(Piperunnable piperunnable);

  Pipepromise enqueue();

}
