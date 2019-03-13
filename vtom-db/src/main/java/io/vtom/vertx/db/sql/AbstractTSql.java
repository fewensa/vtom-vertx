package io.vtom.vertx.db.sql;

import io.vertx.core.json.JsonArray;

public abstract class AbstractTSql<T extends TSql> implements TSql {

  private JsonArray paras;
  private boolean skipNoParas;


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

  protected boolean isSkipNoParas() {
    return this.skipNoParas;
  }

  @Override
  public TSql skipNoParas(boolean yes) {
    this.skipNoParas = yes;
    return this;
  }
}
