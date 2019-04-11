package io.vtom.vertx.pipeline.component.web.client;

class _DefaultRequestReporter implements IRequestReporter {

  private static class Holder {
    private static final _DefaultRequestReporter INSTANCE = new _DefaultRequestReporter();
  }

  static _DefaultRequestReporter instance() {
    return Holder.INSTANCE;
  }

  @Override
  public void report(VtmHttpClientOUT request) {
    System.out.println(request);
  }
}
