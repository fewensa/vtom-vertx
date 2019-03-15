package io.vtom.vertx.pipeline.lifecycle.skip;

import java.util.Set;

public class SkipSure {

  private boolean cond;
  private Skip skip;


  SkipSure(Skip skip, boolean cond) {
    this.cond = cond;
    this.skip = skip;
  }

  public Skip skipId(String id) {
    if (this.cond)
      this.skip.context.id(id);
    return this.skip;
  }

  public Skip skipId(Set<String> ids) {
    if (this.cond)
      this.skip.context.id(ids);
    return this.skip;
  }

  public Skip skipOrd(Integer ord) {
    if (this.cond)
      this.skip.context.ord(ord);
    return this.skip;
  }

  public Skip skipOrd(Set<Integer> ords) {
    if (this.cond)
      this.skip.context.ord(ords);
    return this.skip;
  }

  public Skip all() {
    if (this.cond)
      this.skip.context.all();
    return this.skip;
  }

}
