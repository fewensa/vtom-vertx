package io.vtom.vertx.pipeline.lifecycle.skip;

public class Skip {

  VtmSkipContext context;

  public Skip(VtmSkipContext context) {
    this.context = context;
  }

  public static Skip skip() {
    return new VtmSkipContext().skip();
  }



  public SkipCond condition(boolean cond) {
    return new SkipCond(this, cond);
  }


//  public Skip id(String id) {
//    this.context.id(id);
//    return this;
//  }
//
//  public Skip id(Set<String> ids) {
//    this.context.id(ids);
//    return this;
//  }
//
//  public Skip ord(Integer ord) {
//    this.context.ord(ord);
//    return this;
//  }
//
//  public Skip ord(Set<Integer> ords) {
//    this.context.ord(ords);
//    return this;
//  }
//
//  public Skip all() {
//    this.context.all();
//    return this;
//  }


}
