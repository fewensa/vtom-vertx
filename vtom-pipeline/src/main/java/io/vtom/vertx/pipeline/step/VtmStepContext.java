package io.vtom.vertx.pipeline.step;

import io.enoa.toolkit.digest.UUIDKit;

public class VtmStepContext<T extends StepIN> {


  private String id;
  private int ord;
  private int after;
  private StepStack<T> stepstack;

  VtmStepContext(StepStack<T> stepstack) {
    this.stepstack = stepstack;
    this.id = UUIDKit.next(false);
  }

  public static <J extends StepIN> VtmStepContext<J> context(Step<J> step) {
    return step.context();
  }

  Step<T> step() {
    return new Step<>(this);
  }

  VtmStepContext<T> id(String id) {
    this.id = id;
    return this;
  }

  VtmStepContext<T> ord(int ord) {
    this.ord = ord;
    return this;
  }

  VtmStepContext<T> after(int after) {
    this.after = after;
    return this;
  }

  public String id() {
    return id;
  }

  public int ord() {
    return ord;
  }

  public int after() {
    return after;
  }

  public StepStack<T> stepstack() {
    return stepstack;
  }
}
