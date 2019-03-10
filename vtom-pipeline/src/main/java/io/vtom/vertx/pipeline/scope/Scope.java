package io.vtom.vertx.pipeline.scope;

import io.enoa.toolkit.value.EnoaValue;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class Scope implements Serializable {

  ScopeContext context;

  Scope(ScopeContext context) {
    this.context = context;
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

//  public List<Object> origins() {
//    List<Object> values = this.context.varord().get(0);
//    return values == null ? Collections.emptyList() : values;
//  }
//
//  public List<EnoaValue> values(Integer id) {
//    return this.origins(id).stream()
//      .map(EnoaValue::with)
//      .collect(Collectors.toList());
//  }

  public void clear() {
    this.context.clear();
  }

  @Override
  public String toString() {
    return this.context.toString();
  }
}
