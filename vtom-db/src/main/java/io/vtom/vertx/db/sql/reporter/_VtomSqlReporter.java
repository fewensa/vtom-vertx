package io.vtom.vertx.db.sql.reporter;

import io.enoa.toolkit.text.PadKit;
import io.enoa.toolkit.text.TextKit;
import io.enoa.toolkit.value.EnoaValue;
import io.vertx.core.json.JsonArray;

import java.util.Arrays;

class _VtomSqlReporter implements ISqlReporter {

  private static class Holder {
    private static _VtomSqlReporter INSTANCE = new _VtomSqlReporter();
  }

  static ISqlReporter instance() {
    return Holder.INSTANCE;
  }

  @Override
  public void report(String mark, String sql, JsonArray paras) {
    sql = TextKit.removeRightChar(sql, '\n');
    sql = sql.replace("\n ", "\n");
    sql = sql.replace("\n", PadKit.rpad("\n", " ", 14));
    StringBuilder sb = new StringBuilder();
    sb.append("SQL   ").append('(').append(mark).append(")=> ")
      .append(sql);
    if (paras != null && !paras.isEmpty()) {
      sb.append("\n");
      sb.append("PARAS ").append('(').append(mark).append(")=> ");
      sb.append(Arrays.toString(paras.stream().map(para -> {
        if (para == null)
          return null;
        String string = EnoaValue.with(para).string(null);
        if (TextKit.blanky(string))
          return null;
        string = string.replace("\r", "\\r")
          .replace("\n", "\\n")
          .replace("\t", "\\t");
        return string;
      }).toArray(String[]::new)));
    }
    String _sql = sb.toString();
    System.out.println(_sql);
  }
}
