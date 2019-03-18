package io.vtom.vertx.pipeline.component.timer;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTimer<T extends Timer> implements Timer {

  private List<Handler<Skip>> stepskips;

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
