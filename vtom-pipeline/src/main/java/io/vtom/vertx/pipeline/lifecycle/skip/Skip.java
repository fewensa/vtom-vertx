package io.vtom.vertx.pipeline.lifecycle.skip;

public class Skip {

  VtmSkipContext context;

  public Skip(VtmSkipContext context) {
    this.context = context;
  }

  public static Skip skip() {
    return new VtmSkipContext().skip();
  }


  public SkipSure yes() {
    return areYouSure(true);
  }

  public SkipSure areYouSure(boolean cond) {
    return new SkipSure(this, cond);
  }

}
