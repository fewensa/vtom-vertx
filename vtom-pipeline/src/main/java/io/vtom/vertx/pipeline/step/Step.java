package io.vtom.vertx.pipeline.step;

public interface Step<T extends StepIN> extends _Step<T> {

  static <J extends StepIN> Step<J> with(StepStack<J> stepstack) {
    return new StepImpl<>(stepstack);
  }

  Step<T> id(String id);

  Step<T> ord(int ord);

  Step<T> after(int after);


}
