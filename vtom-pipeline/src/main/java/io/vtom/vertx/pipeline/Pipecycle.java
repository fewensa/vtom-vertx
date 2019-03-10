package io.vtom.vertx.pipeline;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.scope.Scope;

public class Pipecycle {

  private final Vertx vertx;
  private final Scope scope;

  Pipecycle(Vertx vertx, Scope scope) {
    this.vertx = vertx;
    this.scope = scope;
  }

  public Vertx vertx() {
    return this.vertx;
  }

  public Scope scope() {
    return this.scope;
  }

}
