package io.vtom.vertx.pipeline.component.db.sql;

import io.vtom.vertx.pipeline.component.db.sql.dsql.DSqlAction;
import io.vtom.vertx.pipeline.component.db.sql.template.VtmTplAction;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepSkip;

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

  static VtmTplAction template() {
    return epm().tplaction();
  }

  static VtmTplAction template(String name) {
    return epm().tplaction(name);
  }

  @Override
  StepIN skip(StepSkip stepskip);

  @Override
  VTSout out();


}
