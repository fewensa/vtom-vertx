package io.vtom.vertx.pipeline.component.fs;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.fs.action.Fs;

public class VtomFileSystem implements PipeComponent<Fs> {


  private Vertx vertx;


  public VtomFileSystem(Vertx vertx) {
    this.vertx = vertx;
  }

  public static VtomFileSystem with(Vertx vertx) {
    return new VtomFileSystem(vertx);
  }

  @Override
  public VtomFileSystemStep component() {
    return this.dependency(Pipeline.pipeline());
  }

  @Override
  public VtomFileSystemStep dependency(Pipeline pipeline) {
    return new VtomFileSystemStep(this.vertx, pipeline);
  }

}
