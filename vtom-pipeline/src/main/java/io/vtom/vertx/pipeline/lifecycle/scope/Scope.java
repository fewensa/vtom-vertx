package io.vtom.vertx.pipeline.lifecycle.scope;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.value.PipeValue;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Scope implements Serializable {

  VtmScopeContext context;

  private Kv danger;


  Scope(VtmScopeContext context) {
    this.context = context;
  }

  public static Scope scope() {
    return new VtmScopeContext().scope();
  }

  public PipeValue last() {
    return PipeValue.with(this.context.last());
  }

  public PipeValue value(String id) {
    Kv varid = this.context.varid();
    if (varid == null)
      return PipeValue.NULL;
    return PipeValue.with(varid.get(id));
  }

  public PipeValue value(Integer ord) {
    Map<Integer, Object> varord = this.context.varord();
    if (varord == null)
      return PipeValue.NULL;
    return PipeValue.with(varord.get(ord));
  }

  public List<PipeValue> parallelValues() {
    List<Object> varparallels = this.context.varparallels();
    if (varparallels == null)
      return Collections.emptyList();
    return varparallels.stream()
      .map(PipeValue::with)
      .collect(Collectors.toList());
  }

  public Kv danger(String key) {
    if (this.danger == null)
      this.danger = Kv.create(new ConcurrentHashMap<>());
    return (Kv) this.danger.computeIfAbsent(key, k -> Kv.create(new ConcurrentHashMap<>()));
  }

  public void clear() {
    this.context.clear();
    CollectionKit.clear(this.danger);
  }

  @Override
  public String toString() {
    return this.context.toString();
  }
}
