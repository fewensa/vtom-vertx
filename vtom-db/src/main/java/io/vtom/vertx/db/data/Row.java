package io.vtom.vertx.db.data;

import io.enoa.toolkit.map.FastKv;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.HashMap;

public class Row extends HashMap<String, Object> implements FastKv {

  private JsonObject jo;

  Row(JsonObject jo) {
    super(jo.getMap());
    this.jo = jo;
  }

  @Override
  public Object origin(String key) {
    return this.jo.getValue(key);
  }

  @Override
  public boolean exists(String key) {
    return this.origin(key) != null;
  }

  public Instant instant(String key, Instant def) {
    return this.value(key).instant(def);
  }

  public Instant instant(String key) {
    return this.value(key).instant();
  }

  @Override
  public RowValue value(String key) {
    return new RowValue(this.origin(key));
  }

  public Row set(String key, Object value) {
    super.put(key, value);
    if (value == null) {
      this.jo.putNull(key);
      return this;
    }
    this.jo.put(key, value);
    return this;
  }

  @Override
  public void clear() {
    super.clear();
    this.jo.clear();
  }
}
