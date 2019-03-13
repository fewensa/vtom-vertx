package io.vtom.vertx.db.data;

import io.enoa.toolkit.collection.CollectionKit;
import io.vertx.ext.sql.ResultSet;

import java.util.List;
import java.util.stream.Collectors;

public class Table {

  private List<Row> rows;
  private List<String> columns;

  public static Table create(ResultSet rs) {
    return new Table(rs);
  }

  public Table(ResultSet rs) {
    this.columns = rs.getColumnNames();
    this.rows = rs.getRows().stream()
      .map(Row::new)
      .collect(Collectors.toList());
  }

  public List<String> columns() {
    return this.columns;
  }

  public List<Row> rows() {
    return this.rows;
  }

  public Table clear() {
    CollectionKit.clear(this.rows, this.columns);
    return this;
  }


}
