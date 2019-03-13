package io.vtom.vertx.db.data;

import io.enoa.toolkit.map.EoMap;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.Map;

public class Row implements EoMap<Row> {

  private JsonObject jo;

  public static Row create(JsonObject jo) {
    return new Row(jo);
  }

  public Row(JsonObject jo) {
    this.jo = jo;
  }

  @Override
  public Map<String, Object> map() {
    return this.jo.getMap();
  }

  @Override
  public Object get(Object key) {
    return this.map().get(key);
  }

  @Override
  public Object origin(String key) {
    return this.get(key);
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
    return RowValue.with(this.origin(key));
  }

  public Row set(String key, Object value) {
    this.put(key, value);
    return this;
  }

  @Override
  public void clear() {
    this.jo.clear();
  }
}
