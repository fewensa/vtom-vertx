package io.vtom.vertx.pipeline.component.http.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;
import io.vtom.vertx.pipeline.PipeRunnable;
import io.vtom.vertx.pipeline.step.Step;

class VtomHttpClientRunnable implements PipeRunnable<Vhc, VtmHttpClientOUT> {


  private Vertx vertx;
  private Step<? extends Vhc> step;

  VtomHttpClientRunnable(Vertx vertx, Step<? extends Vhc> step) {
    this.vertx = vertx;
    this.step = step;
  }

  @Override
  public Step<? extends Vhc> step() {
    return this.step;
  }

  @Override
  public void call(VtmHttpClientOUT stepout, Handler<AsyncResult<Object>> handler) {
    HttpClient client = this.vertx.createHttpClient();
    HttpClientRequest request = client.get("");


    this.vertx.createHttpClient(new HttpClientOptions().setProxyOptions(new ProxyOptions().setType(ProxyType.SOCKS5)));
  }

  @Override
  public void release(boolean ok, Handler<AsyncResult<Void>> handler) {

  }
}
