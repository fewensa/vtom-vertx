package io.vtom.vertx.pipeline.component.db.data;

import io.enoa.toolkit.convert.ConvertKit;
import io.enoa.toolkit.text.TextKit;
import io.enoa.toolkit.value.EnoaValue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class RowValue extends EnoaValue {


  protected RowValue(Object value) {
    super(value);
  }

  public static RowValue with(Object value) {
    return new RowValue(value);
  }

  public Instant instant(Instant def) {
    String value = ConvertKit.string(super.origin());
    if (TextKit.blanky(value))
      return def;
    return Instant.from(ISO_INSTANT.parse(value));
  }

  public Instant instant() {
    return this.instant(null);
  }

  @Override
  public Date date(String format, Date def) {
    Instant instant = this.instant();
    if (instant == null)
      return def;
    return Date.from(instant);
  }

  @Override
  public Date date(String format) {
    return this.date(null, null);
  }

  @Override
  public Date date() {
    return this.date(null, null);
  }

  @Override
  public Timestamp timestamp(String format, Timestamp def) {
    Instant instant = this.instant();
    if (instant == null)
      return def;
    return Timestamp.from(instant);
  }

  @Override
  public Timestamp timestamp(String format) {
    return this.timestamp(null, null);
  }

  @Override
  public Timestamp timestamp() {
    return this.timestamp(null, null);
  }
}
