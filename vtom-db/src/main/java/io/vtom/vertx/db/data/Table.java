package io.vtom.vertx.db.data;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.convert.IConverter;
import io.vertx.ext.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Table implements IConverter<Table, ResultSet> {

  private List<Row> rows;
  private List<String> columnNames;

  public static Table create() {
    return new Table();
  }

  public static Table create(List<Row> rows) {
    return new Table(rows);
  }

  public Table() {
    this(new ArrayList<>());
  }

  public Table(List<Row> rows) {
    this.rows = rows;
  }

  public List<String> columnNames() {
    return this.columnNames;
  }

  public List<Row> rows() {
    return this.rows;
  }

  public Table clear() {
    CollectionKit.clear(this.rows, this.columnNames);
    return this;
  }

  @Override
  public Table convert(ResultSet rs) {
    this.columnNames = rs.getColumnNames();
    this.rows = rs.getRows().stream()
      .map(Row::new)
      .collect(Collectors.toList());
    return this;
  }


//  public List<String>

}
