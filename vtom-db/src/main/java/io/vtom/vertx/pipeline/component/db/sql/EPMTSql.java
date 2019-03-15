package io.vtom.vertx.pipeline.component.db.sql;

import io.vtom.vertx.pipeline.component.db.sql.dsql.DSqlAction;
import io.vtom.vertx.pipeline.component.db.sql.template.TplAction;

import java.util.HashMap;
import java.util.Map;

public class EPMTSql {

  private static class Holder {
    private static final EPMTSql INSTANCE = new EPMTSql();
  }

  static EPMTSql instance() {
    return Holder.INSTANCE;
  }

  private Map<String, TSqlOptions> optionsmap;
  private Map<String, TplAction> actionmap;
  private Map<String, DSqlAction> dsqlmap;

  private EPMTSql() {
    this.optionsmap = new HashMap<>();
    this.actionmap = new HashMap<>();
    this.dsqlmap = new HashMap<>();
  }

  public void install(TSqlOptions options) {
    this.install("main", options);
  }

  public void install(String name, TSqlOptions options) {
    this.optionsmap.put(name, options);
  }

  public TSqlOptions options() {
    return options("main");
  }

  public TSqlOptions options(String name) {
    return this.optionsmap.get(name);
  }

  TplAction tplaction() {
    return this.tplaction("main");
  }

  TplAction tplaction(String name) {
    return this.actionmap.computeIfAbsent(name, key -> new TplAction(name));
  }

  DSqlAction dsqlaction() {
    return this.dsqlaction("main");
  }

  DSqlAction dsqlaction(String name) {
    return this.dsqlmap.computeIfAbsent(name, key -> new DSqlAction(name));
  }

}
