package io.vtom.vertx.pipeline;

import io.enoa.promise.DoneArgPromise;
import io.enoa.promise.arg.PromiseArg;
import io.enoa.promise.arg.PromiseCapture;
import io.enoa.promise.arg.PromiseVoid;
import io.vtom.vertx.pipeline.lifecycle.PipeLifecycle;

public class PipePromise implements DoneArgPromise<PipeLifecycle> {

  private DoneArgPromise<PipeLifecycle> promise;

  public PipePromise(DoneArgPromise<PipeLifecycle> promise) {
    this.promise = promise;
  }

  @Override
  public PipePromise done(PromiseArg<PipeLifecycle> done) {
    this.promise.done(done);
    return this;
  }

  @Override
  public PipePromise capture(PromiseCapture capture) {
    this.promise.capture(capture);
    return this;
  }

  @Override
  public void always(PromiseVoid always) {
    this.promise.always(always);
  }

}
