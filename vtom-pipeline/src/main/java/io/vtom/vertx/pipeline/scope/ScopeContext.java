package io.vtom.vertx.pipeline.scope;

import io.enoa.toolkit.map.Kv;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScopeContext {

  private Kv varid;
  private Map<Integer, Object> varord;
  private List<Object> varparallels;
  private String lastId;

  ScopeContext() {
    this.varid = Kv.create();
    this.varord = new HashMap<>();
    this.varparallels = new ArrayList<>();
  }

  public Scope scope() {
    return new Scope(this);
  }

  public static ScopeContext context(Scope scope) {
    return scope.context;
  }

  public ScopeContext put(StepOUT output, Object value) {
    this.varid.set(output.id(), value);
    if (output.ord() > 0) {
      this.varord.put(output.ord(), value);
      return this;
    }
    this.varparallels.add(value);
    return this;
  }

  public ScopeContext last(String lastId) {
    this.lastId = lastId;
    return this;
  }

  Object last() {
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
    this.varid.clear();
    this.varord.clear();
    this.varparallels.clear();
  }

  @Override
  public String toString() {
    return "VARID: " + this.varid + "\n" +
      "VARORD: " + this.varord + "\n" +
      "VARPARALLELS: " + this.varparallels;
  }
}
