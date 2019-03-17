package io.vtom.vertx.pipeline.component.fs;

import io.vertx.core.Vertx;
import io.vtom.vertx.pipeline.PipeComponent;
import io.vtom.vertx.pipeline.Pipeline;
import io.vtom.vertx.pipeline.component.fs.action.Fs;

public class VtomFileSystem implements PipeComponent<Fs> {


  public VtomFileSystem() {
  }

  public static VtomFileSystem create() {
    return new VtomFileSystem();
  }

  @Override
  public VtomFileSystemStep dependency(Vertx vertx) {
    return this.dependency(Pipeline.pipeline(vertx));
  }

  @Override
  public VtomFileSystemStep dependency(Pipeline pipeline) {
    return new VtomFileSystemStep(pipeline);
  }

}
