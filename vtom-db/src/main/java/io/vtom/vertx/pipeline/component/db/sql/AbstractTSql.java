package io.vtom.vertx.pipeline.component.db.sql;

import io.vertx.core.json.JsonArray;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;

public abstract class AbstractTSql<T extends TSql> implements TSql {

  private JsonArray paras;
  private Skip skip;

  @Override
  public T skip(Skip skip) {
    this.skip = skip;
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

  protected Skip skip() {
    return this.skip;
  }

}
