package io.vtom.vertx.pipeline.lifecycle.scope;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VtmScopeContext {

  private Kv varid;
  private Map<Integer, Object> varord;
  private List<Object> varparallels;
  private String lastId;

  VtmScopeContext() {
  }

  Scope scope() {
    return new Scope(this);
  }

  public static VtmScopeContext context(Scope scope) {
    return scope.context;
  }

  public VtmScopeContext put(StepOUT output, Object value) {
    if (this.varid == null)
      this.varid = Kv.create();

    this.varid.set(output.id(), value);
    if (output.ord() > 0) {
      if (this.varord == null)
        this.varord = new HashMap<>();

      this.varord.put(output.ord(), value);
      return this;
    }
    if (this.varparallels == null)
      this.varparallels = new ArrayList<>();

    this.varparallels.add(value);
    return this;
  }

  public VtmScopeContext last(StepOUT output) {
    this.lastId = output.id();
    return this;
  }

  Object last() {
    if (this.varid == null)
      return null;

    return this.varid.get(this.lastId);
  }

  Kv varid() {
    return this.varid;
  }

  Map<Integer, Object> varord() {
    return this.varord;
  }

  List<Object> varparallels() {
    return this.varparallels;
  }

  void clear() {
    CollectionKit.clear(this.varid, this.varord);
    CollectionKit.clear(this.varparallels);
  }

  @Override
  public String toString() {
    return "VARID: " + this.varid + "\n" +
      "VARORD: " + this.varord + "\n" +
      "VARPARALLELS: " + this.varparallels;
  }
}
