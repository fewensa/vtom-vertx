package io.vtom.vertx.pipeline.step;

public interface Pipestep<T extends StepIN> {

  Pipestep<T> step(Pipestack<T> pipestack);

  void end();

}
