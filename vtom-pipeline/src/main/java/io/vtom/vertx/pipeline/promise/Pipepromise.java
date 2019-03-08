package io.vtom.vertx.pipeline.promise;

import io.enoa.promise.DoneArgPromise;
import io.enoa.promise.arg.PromiseArg;
import io.enoa.promise.arg.PromiseCapture;
import io.enoa.promise.arg.PromiseVoid;
import io.vtom.vertx.pipeline.Pipecycle;

public class Pipepromise implements DoneArgPromise<Pipecycle> {

  private DoneArgPromise<Pipecycle> promise;

  public Pipepromise(DoneArgPromise<Pipecycle> promise) {
    this.promise = promise;
  }

  @Override
  public Pipepromise done(PromiseArg<Pipecycle> done) {
    this.promise.done(done);
    return this;
  }

  @Override
  public Pipepromise capture(PromiseCapture capture) {
    this.promise.capture(capture);
    return this;
  }

  @Override
  public void always(PromiseVoid always) {
    this.promise.always(always);
  }

}
