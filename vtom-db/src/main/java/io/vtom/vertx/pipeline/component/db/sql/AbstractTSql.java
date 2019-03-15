package io.vtom.vertx.pipeline.component.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.step.StepSkip;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTSql<T extends TSql> implements TSql {

  private JsonArray paras;
  private List<StepSkip> stepskips;

  @Override
  public T skip(StepSkip stepskip) {
    if (this.stepskips == null)
      this.stepskips = new ArrayList<>();
    this.stepskips.add(stepskip);
    return (T) this;
  }

  public T para(Object para) {
    if (para instanceof Iterable)
      return this.paras((Iterable) para);

    if (this.paras == null)
      this.paras = new JsonArray();
    if (para == null) {
      this.paras.addNull();
    } else {
      this.paras.add(para);
    }
    return (T) this;
  }

  public T paras(Iterable iterable) {
    this.paras = new JsonArray();
    iterable.forEach(item -> {
      if (item == null) {
        this.paras.addNull();
        return;
      }
      this.paras.add(item);
    });
    return (T) this;
  }

  protected JsonArray paras() {
    return this.paras;
  }

  protected List<StepSkip> stepskips() {
    return this.stepskips;
  }

}
