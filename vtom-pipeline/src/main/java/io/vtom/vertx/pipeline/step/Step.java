package io.vtom.vertx.pipeline.step;

public class Step<T extends StepIN> {

  private VtmStepContext<T> context;

  Step(VtmStepContext<T> context) {
    this.context = context;
  }

  public Step(StepStack<T> stepstack) {
    this(new VtmStepContext<>(stepstack));
  }

  public static <J extends StepIN> Step<J> with(StepStack<J> stepstack) {
    return new Step<>(stepstack);
  }

  protected VtmStepContext<T> context() {
    return this.context;
  }

  public Step<T> id(String id) {
    this.context.id(id);
    return this;
  }

  public Step<T> ord(int ord) {
    this.context.ord(ord);
    return this;
  }

  public Step<T> after(int after) {
    this.context.after(after);
    return this;
  }

}
