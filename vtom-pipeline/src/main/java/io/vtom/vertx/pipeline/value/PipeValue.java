package io.vtom.vertx.pipeline.value;

import io.enoa.toolkit.convert.IConverter;
import io.enoa.toolkit.value.EnoaValue;

public class PipeValue {

  public static final PipeValue NULL = new PipeValue(null);

  private EnoaValue value;


  protected PipeValue(Object value) {
    this.value = EnoaValue.with(value);
  }

  public static PipeValue with(Object value) {
    if (value == null)
      return NULL;
    return new PipeValue(value);
  }

  public Object get() {
    return this.origin();
  }

  public Object origin() {
    return value.origin();
  }

  public <T> T as(T def) {
    return this.value.as(def);
  }

  public <T> T as() {
    return this.value.as();
  }

  public boolean isNull() {
    return this.value.isNull();
  }

  public <R, P> R to(IConverter<R, P> converter) {
    return this.value.to(converter);
  }

  public EnoaValue value() {
    return this.value;
  }

  @Override
  public String toString() {
    return this.value.toString();
  }
}
