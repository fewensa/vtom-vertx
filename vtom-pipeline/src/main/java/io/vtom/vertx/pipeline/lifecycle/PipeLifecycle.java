package io.vtom.vertx.pipeline.lifecycle;

import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public class PipeLifecycle {

  private final Kv mount;
  private final Scope scope;
  private final Skip skip;

  public PipeLifecycle(Scope scope) {
    this.mount = Kv.create();
    this.scope = scope;
    this.skip = Skip.skip();
  }

  public Kv mount() {
    return this.mount;
  }

  public Scope scope() {
    return this.scope;
  }

  public Skip skip() {
    return this.skip;
  }

}
