package io.vtom.vertx.pipeline;

import java.util.*;
import java.util.stream.Stream;

public class Scope implements Iterable<Object> {


  private Map<Integer, Object> variable;

  private Scope() {
    this.variable = new HashMap<>();
  }

  public static Scope scope() {
    return new Scope();
  }

  @Override
  public Iterator<Object> iterator() {
    return null;
  }


  //  private List<Object> variables;
//
//  private Scope(int capacity) {
//    this.variables = new ArrayList<>(capacity);
//  }
//
//  public static Scope scope() {
//    return scope(0);
//  }
//
//  public static Scope scope(int capacity) {
//    return new Scope(capacity);
//  }
//
//  public Scope set(int ix, Object value) {
//    return this;
//  }
//
//  @Override
//  public Iterator<Object> iterator() {
//    return this.variables.iterator();
//  }
//
//  public Stream<Object> stream() {
//    return this.variables.stream();
//  }
//
//  @Override
//  public String toString() {
//    return this.variables.toString();
//  }

}
