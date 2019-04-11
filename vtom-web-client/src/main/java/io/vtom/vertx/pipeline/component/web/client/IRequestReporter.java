package io.vtom.vertx.pipeline.component.web.client;

public interface IRequestReporter {

  static IRequestReporter def() {
    return _DefaultRequestReporter.instance();
  }

  void report(VtmHttpClientOUT request);

}
