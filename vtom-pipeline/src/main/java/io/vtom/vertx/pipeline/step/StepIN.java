package io.vtom.vertx.pipeline.step;

public interface StepIN {

  StepIN id(String id);

  StepIN ord(int ord);

  StepIN after(int after);

  StepOUT output();

}
