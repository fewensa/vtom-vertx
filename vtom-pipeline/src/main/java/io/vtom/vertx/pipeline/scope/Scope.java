package io.vtom.vertx.pipeline.scope;

import io.enoa.toolkit.map.Kv;
import io.enoa.toolkit.value.EnoaValue;
import io.vtom.vertx.pipeline.value.PipeValue;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Scope implements Serializable {

  ScopeContext context;

  private Kv danger;


  Scope(ScopeContext context) {
    this.context = context;
    this.danger = Kv.create(new ConcurrentHashMap<>());
  }

  public static ScopeContext context() {
    return new ScopeContext();
  }

  public PipeValue value(String id) {
    return PipeValue.with(this.context.varid().get(id));
  }

  public PipeValue value(Integer id) {
    return PipeValue.with(this.context.varord().get(id));
  }

  public List<PipeValue> parallelValues() {
    return this.context.varparallels().stream()
      .map(PipeValue::with)
      .collect(Collectors.toList());
  }

  public Kv danger(String key) {
    return (Kv) this.danger.computeIfAbsent(key, k -> Kv.create(new ConcurrentHashMap<>()));
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
