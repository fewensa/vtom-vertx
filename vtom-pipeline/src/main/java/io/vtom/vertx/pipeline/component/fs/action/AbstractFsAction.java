package io.vtom.vertx.pipeline.component.fs.action;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFsAction<T extends Fs> implements Fs {

  private String path;
  private List<Handler<Skip>> stepskips;

  public AbstractFsAction(String path) {
    this.path = path;
  }

  protected String path() {
    return this.path;
  }

  @Override
  public T skip(Handler<Skip> stepskip) {
    if (this.stepskips == null)
      this.stepskips = new ArrayList<>();
    this.stepskips.add(stepskip);
    return (T) this;
  }

  protected List<Handler<Skip>> stepskips() {
    return this.stepskips;
  }
}
