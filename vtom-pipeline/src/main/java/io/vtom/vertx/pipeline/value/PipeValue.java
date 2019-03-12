package io.vtom.vertx.pipeline.value;

import io.enoa.toolkit.convert.IConverter;

public class PipeValue {

  private Object value;

  private PipeValue(Object value) {
    this.value = value;
  }

  public static PipeValue with(Object value) {
    return new PipeValue(value);
  }

  public Object get() {
    return this.origin();
  }

  public Object origin() {
    return value;
  }

  public <T> T as(T def) {
    return this.value == null ? def : this.as();
  }

  public <T> T as() {
    return (T) this.value;
  }

  public boolean isNull() {
    return this.value == null;
  }

  public boolean notNull() {
    return !this.isNull();
  }

  public <R, P> R to(IConverter<R, P> converter) {
    return converter.convert(this.as());
  }

}
