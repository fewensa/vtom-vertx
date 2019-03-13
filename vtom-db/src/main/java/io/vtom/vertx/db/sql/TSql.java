package io.vtom.vertx.db.sql;

import io.vtom.vertx.db.sql.dsql.DSqlAction;
import io.vtom.vertx.db.sql.template.TplAction;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepWrapper;

public interface TSql extends StepIN {

  static EPMTSql epm() {
    return EPMTSql.instance();
  }

  static DSqlAction sql() {
    return epm().dsqlaction();
  }

  static DSqlAction sql(String name) {
    return epm().dsqlaction(name);
  }

  static TplAction template() {
    return epm().tplaction();
  }

  static TplAction template(String name) {
    return epm().tplaction(name);
  }

  default TSql skipNoParas() {
    return this.skipNoParas(true);
  }

  TSql skipNoParas(boolean yes);

  @Override
  <I extends StepIN> VTSout out(StepWrapper<I> wrapper);


}
