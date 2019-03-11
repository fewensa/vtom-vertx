package io.vtom.vertx.pipeline;

import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.scope.Scope;

public class PipeLifecycle {

  private final Kv mount;
  private final Scope scope;

  PipeLifecycle(Scope scope) {
    this.mount = Kv.create();
    this.scope = scope;
  }

  public Kv mount() {
    return this.mount;
  }

  public Scope scope() {
    return this.scope;
  }

}
