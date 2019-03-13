package io.vtom.vertx.pipeline.step;

import io.enoa.toolkit.digest.UUIDKit;

class StepImpl<T extends StepIN> implements Step<T> {


  private String id;
  private int ord;
  private int after;
  private StepStack<T> stepstack;


  StepImpl(StepStack<T> stepstack) {
    this.stepstack = stepstack;
    this.id = UUIDKit.next(false);
  }

  @Override
  public Step<T> id(String id) {
    this.id = id;
    return this;
  }

  @Override
  public Step<T> ord(int ord) {
    this.ord = ord;
    return this;
  }

  @Override
  public Step<T> after(int after) {
    this.after = after;
    return this;
  }

  @Override
  public StepWrapper<T> _wrapper() {
    return new StepWrapper<T>() {
      @Override
      public String id() {
        return id;
      }

      @Override
      public int ord() {
        return ord;
      }

      @Override
      public int after() {
        return after;
      }

      @Override
      public StepStack<T> stepstack() {
        return stepstack;
      }
    };
  }

}
