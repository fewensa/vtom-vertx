package io.vtom.vertx.pipeline.scope;

import io.enoa.toolkit.map.Kv;
import io.enoa.toolkit.value.EnoaValue;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Scope implements Serializable {

  ScopeContext context;

  private Kv danger;


  Scope(ScopeContext context) {
    this.context = context;
    this.danger = Kv.create();
  }

  public static ScopeContext context() {
    return new ScopeContext();
  }

  public Object origin(String id) {
    return this.context.varid().get(id);
  }

  public EnoaValue value(String id) {
    return this.context.varid().value(id);
  }

  public Object origin(Integer ord) {
    return this.context.varord().get(ord);
  }

  public EnoaValue value(Integer ord) {
    return EnoaValue.with(this.origin(ord));
  }

  public List<Object> parallelOrigins() {
    return this.context.varparallels();
  }

  public List<EnoaValue> parallelValues() {
    return this.parallelOrigins().stream()
      .map(EnoaValue::with)
      .collect(Collectors.toList());
  }

  public Kv danger(String key) {
    return (Kv) this.danger.computeIfAbsent(key, k -> Kv.create());
  }

  public void clear() {
    this.context.clear();
    this.danger.clear();
  }

  @Override
  public String toString() {
    return this.context.toString();
  }
}
