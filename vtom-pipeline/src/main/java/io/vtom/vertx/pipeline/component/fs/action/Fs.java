package io.vtom.vertx.pipeline.component.fs.action;

import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepSkip;

public interface Fs extends StepIN {

  static VtmFsChmod chmod(String path) {
    return new VtmFsChmod(path);
  }

  static VtmFsChown chown(String path) {
    return new VtmFsChown(path);
  }

  static VtmFsCopy copy(String from) {
    return new VtmFsCopy(from);
  }

  static VtmFsMove move(String from) {
    return new VtmFsMove(from);
  }

  static VtmFsTruncate truncate(String path) {
    return new VtmFsTruncate(path);
  }

  static VtmFsProps props(String path) {
    return new VtmFsProps(path);
  }

  static VtmFsLprops lprops(String path) {
    return new VtmFsLprops(path);
  }

  static VtmFsLink link(String link) {
    return new VtmFsLink(link);
  }

  static VtmFsSymlink symlink(String link) {
    return new VtmFsSymlink(link);
  }

  static VtmFsUnlink unlink(String link) {
    return new VtmFsUnlink(link);
  }

  static VtmFsReadSymlink readSymlink(String link) {
    return new VtmFsReadSymlink(link);
  }

  static VtmFsDelete delete(String path) {
    return new VtmFsDelete(path);
  }

  static VtmFsMkdir mkdir(String path) {
    return new VtmFsMkdir(path);
  }

  static VtmFsMkdirs mkdirs(String path) {
    return new VtmFsMkdirs(path);
  }

  static VtmFsReadDir readDir(String path) {
    return new VtmFsReadDir(path);
  }

  static VtmFsReadFile readFile(String path) {
    return new VtmFsReadFile(path);
  }

  static VtmFsWriteFile writeFile(String path) {
    return new VtmFsWriteFile(path);
  }

  static VtmFsOpen open(String path) {
    return new VtmFsOpen(path);
  }

  static VtmFsCreateFile createFile(String path) {
    return new VtmFsCreateFile(path);
  }

  static VtmFsExists exists(String path) {
    return new VtmFsExists(path);
  }

  static VtmFsFFSProps fsProps(String path) {
    return new VtmFsFFSProps(path);
  }

  static VtmFsCreateTempDirectory createTempDirectory(String prefix) {
    return new VtmFsCreateTempDirectory(prefix);
  }

  static VtmFsCreateTempFile createTempFile() {
    return new VtmFsCreateTempFile();
  }

  @Override
  StepIN skip(StepSkip stepskip);

  @Override
  VtmFsOut out();
}
