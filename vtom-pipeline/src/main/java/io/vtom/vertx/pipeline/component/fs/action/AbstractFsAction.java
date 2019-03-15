package io.vtom.vertx.pipeline.component.fs.action;

import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public abstract class AbstractFsAction<T extends Fs> implements Fs {

  private String path;
  private Skip skip;

  public AbstractFsAction(String path) {
    this.path = path;
  }

  protected String path() {
    return this.path;
  }

  @Override
  public T skip(Skip skip) {
    this.skip = skip;
    return (T) this;
  }

  protected Skip _skip() {
    return this.skip;
  }
}
