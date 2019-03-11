package io.vtom.vertx.db.thr;

import io.enoa.toolkit.thr.EoException;

public class VtomSqlException extends EoException {

  public VtomSqlException() {
    super();
  }

  public VtomSqlException(String message, Object... format) {
    super(message, format);
  }

  public VtomSqlException(String message, Throwable cause, Object... format) {
    super(message, cause, format);
  }

  public VtomSqlException(Throwable cause) {
    super(cause);
  }

  public VtomSqlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... format) {
    super(message, cause, enableSuppression, writableStackTrace, format);
  }
}
