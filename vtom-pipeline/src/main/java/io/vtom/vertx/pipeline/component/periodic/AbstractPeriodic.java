package io.vtom.vertx.pipeline.component.periodic;

import io.vertx.core.Handler;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractPeriodic<T extends Periodic> implements Periodic {

  private List<Handler<Skip>> stepskips;

  public AbstractPeriodic() {
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
