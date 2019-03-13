package io.vtom.vertx.db.data;

import io.enoa.toolkit.collection.CollectionKit;
import io.enoa.toolkit.convert.IConverter;
import io.enoa.toolkit.eo.tip.EnoaTipKit;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vtom.vertx.db.page.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class DBConverter {


  private static <T> void checkType(T origin, Class clazz) {
//    if (origin == null)
//      throw new IllegalArgumentException(EnoaTipKit.message("eo.tip.vtom.db.convert_origin_null"));
    if (clazz != origin.getClass())
      throw new IllegalArgumentException(EnoaTipKit.message("eo.tip.vtom.db.convert_origin_type_error", clazz, origin.getClass()));
  }

  public static <T> IConverter<Table, T> toTable() {
    return origin -> {
      if (origin == null)
        return null;

      checkType(origin, ResultSet.class);

      return new Table((ResultSet) origin);
    };
  }

  public static <T> IConverter<List<Row>, T> toRows() {
    return origin -> {
      if (origin == null)
        return Collections.emptyList();

      checkType(origin, ResultSet.class);

      ResultSet rs = (ResultSet) origin;
      return rs.getRows().stream()
        .map(Row::create)
        .collect(Collectors.toList());
    };
  }

  public static <T> IConverter<Row, T> toRow() {
    return origin -> {
      IConverter<List<Row>, T> converter = toRows();
      List<Row> rows = converter.convert(origin);
      return CollectionKit.isEmpty(rows) ? null : rows.get(0);
    };
  }

  public static <T> IConverter<Page<Row>, T> toPageRow() {

    return origin -> {
      if (origin == null)
        return null;

      checkType(origin, Page.class);

      Page po = (Page) origin;
      List rows0 = po.getRows();
      if (CollectionKit.isEmpty(rows0)) {
        return po;
      }

      List<Row> rows1 = new ArrayList<>(rows0.size());
      for (Object o1 : rows0) {
        if (!(o1 instanceof JsonObject))
          throw new IllegalArgumentException(EnoaTipKit.message("eo.tip.vtom.db.convert_origin_type_error", JsonObject.class, origin.getClass()));
        rows1.add(Row.create((JsonObject) o1));
      }
      po.setRows(rows1);
      CollectionKit.clear(rows0);
      return po;
    };

  }


}
