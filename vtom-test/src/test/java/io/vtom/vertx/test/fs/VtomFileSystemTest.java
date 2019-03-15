package io.vtom.vertx.test.fs;

import io.enoa.toolkit.digest.UUIDKit;
import io.enoa.toolkit.path.PathKit;
import io.enoa.toolkit.sys.EnvKit;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestSuite;
import io.vtom.vertx.pipeline.component.fs.VtomFileSystem;
import io.vtom.vertx.pipeline.component.fs.action.Fs;
import io.vtom.vertx.pipeline.lifecycle.scope.Scope;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.Step;
import org.junit.Before;
import org.junit.Test;

public class VtomFileSystemTest {


  private TestSuite suite;
  private Vertx vertx;


  private String sysuser = EnvKit.string("user.name");
  private String perms = "-rw-r--r--";
  private String testDir = PathKit.debugPath().resolve("src/test/resources/_tmp").toString();
  private String testFile = PathKit.debugPath().resolve("src/test/resources/_tmp/test.txt").toString();
  private String testCopyFile = PathKit.debugPath().resolve("src/test/resources/_tmp/test.copy.txt").toString();


  @Before
  public void setUp() throws Exception {
    this.vertx = Vertx.vertx();
    this.suite = TestSuite.create("fs");
  }

  /*

                         |
                         |
                         v            no     -----------------         fail
                    <exists testDir>  ---->  | mkdir testDir |  -->-->-->-->-->-->--|
                         |                   -----------------                      |
        v<--<--<--<--|   | yes                    | ok                              v
        |            |   v                        |                                 |
        |      -<-- <exists testFile> <------------                              --------
        v      |         v yes                                                   | done |
        |    n |    -------------------                    fail                  --------
        |    o v    | delete testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->--|
        v      |    -------------------                                             ^
        |      |         v                                                          |
        |      v    -------------------                    fail                     |
        v      ---> | create testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->--^
        |           -------------------                                             |
        |                v                                                          |
        v           ------------------                     fail                     ^
        |           | write testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->-->|
        |           ------------------                                              |
        v                v              yes   -----------------------               ^
        |<--<--<--<exists testCopyFile> --->  | delete testCopyFile |               |
        |                v no                 -----------------------               |
        v     ---------------------------------           |                         ^
        |     | copy testFile to testCopyFile |   <--------                         |
        |     ---------------------------------                                     |
        v            | ok     | fail                                                ^
        |            v        v                                                     |
        |-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->|


   */


  @Test
  public void testVertxFs() {
    this.suite.test("vertx.read", ctx -> {
      Async async = ctx.async(12);
      FileSystem fs = this.vertx.fileSystem();

      fs.exists(testDir, ar0 -> {
        if (ar0.failed()) {
          ar0.cause().printStackTrace();
          async.complete();
          return;
        }
        if (!ar0.result()) {
          fs.mkdirs(testDir, ar1 -> {
            if (ar1.failed()) {
              ar1.cause().printStackTrace();
              return;
            }

          });
          fs.exists(testFile, ar9 -> {
            if (ar9.failed()) {
              ar9.cause().printStackTrace();
              async.complete();
              return;
            }
            if (ar9.result()) {
              fs.delete(testFile, ar10 -> {
                if (ar10.failed()) {
                  ar10.cause().printStackTrace();
                  async.complete();
                  return;
                }
                fs.createFile(testFile, ar1 -> {
                  if (ar1.failed()) {
                    ar1.cause().printStackTrace();
                    async.complete();
                    return;
                  }
                  fs.writeFile(testFile, Buffer.buffer(UUIDKit.next()), ar2 -> {
                    if (ar2.failed()) {
                      ar2.cause().printStackTrace();
                      async.complete();
                      return;
                    }
                    fs.exists(testCopyFile, ar11 -> {
                      if (ar11.failed()) {
                        ar11.cause().printStackTrace();
                        async.complete();
                        return;
                      }
                      if (ar11.result()) {
                        fs.delete(testCopyFile, ar12 -> {
                          if (ar12.failed()) {
                            ar12.cause().printStackTrace();
                            async.complete();
                            return;
                          }

                          fs.copy(testFile, testCopyFile, ar3 -> {
                            if (ar3.failed()) {
                              ar3.cause().printStackTrace();
                              async.complete();
                              return;
                            }
                            async.complete();
                          });
                        });
                        return;
                      }

                      fs.copy(testFile, testCopyFile, ar3 -> {
                        if (ar3.failed()) {
                          ar3.cause().printStackTrace();
                          async.complete();
                          return;
                        }
                        async.complete();
                      });
                    });
                  });
                });
              });
              return;
            }

            fs.createFile(testFile, ar1 -> {
              if (ar1.failed()) {
                ar1.cause().printStackTrace();
                async.complete();
                return;
              }
              fs.writeFile(testFile, Buffer.buffer(UUIDKit.next()), ar2 -> {
                if (ar2.failed()) {
                  ar2.cause().printStackTrace();
                  async.complete();
                  return;
                }
                fs.exists(testCopyFile, ar11 -> {
                  if (ar11.failed()) {
                    ar11.cause().printStackTrace();
                    async.complete();
                    return;
                  }
                  if (ar11.result()) {
                    fs.delete(testCopyFile, ar12 -> {
                      if (ar12.failed()) {
                        ar12.cause().printStackTrace();
                        async.complete();
                        return;
                      }

                      fs.copy(testFile, testCopyFile, ar3 -> {
                        if (ar3.failed()) {
                          ar3.cause().printStackTrace();
                          async.complete();
                          return;
                        }
                        async.complete();
                      });
                    });
                    return;
                  }

                  fs.copy(testFile, testCopyFile, ar3 -> {
                    if (ar3.failed()) {
                      ar3.cause().printStackTrace();
                      async.complete();
                      return;
                    }
//              fs.deleteRecursive(testDir, true, ar4 -> {
//                if (ar4.failed()) {
//                  ar4.cause().printStackTrace();
//                }
//              });
                    async.complete();
                  });
                });
              });
            });
          });
          return;
        }

        fs.exists(testFile, ar9 -> {
          if (ar9.failed()) {
            ar9.cause().printStackTrace();
            async.complete();
            return;
          }
          if (ar9.result()) {
            fs.delete(testFile, ar10 -> {
              if (ar10.failed()) {
                ar10.cause().printStackTrace();
                async.complete();
                return;
              }
              fs.createFile(testFile, ar1 -> {
                if (ar1.failed()) {
                  ar1.cause().printStackTrace();
                  async.complete();
                  return;
                }
                fs.writeFile(testFile, Buffer.buffer(UUIDKit.next()), ar2 -> {
                  if (ar2.failed()) {
                    ar2.cause().printStackTrace();
                    async.complete();
                    return;
                  }
                  fs.exists(testCopyFile, ar11 -> {
                    if (ar11.failed()) {
                      ar11.cause().printStackTrace();
                      async.complete();
                      return;
                    }
                    if (ar11.result()) {
                      fs.delete(testCopyFile, ar12 -> {
                        if (ar12.failed()) {
                          ar12.cause().printStackTrace();
                          async.complete();
                          return;
                        }

                        fs.copy(testFile, testCopyFile, ar3 -> {
                          if (ar3.failed()) {
                            ar3.cause().printStackTrace();
                            async.complete();
                            return;
                          }
                          async.complete();
                        });
                      });
                      return;
                    }

                    fs.copy(testFile, testCopyFile, ar3 -> {
                      if (ar3.failed()) {
                        ar3.cause().printStackTrace();
                        async.complete();
                        return;
                      }
                      async.complete();
                    });
                  });
                });
              });
            });
            return;
          }

          fs.createFile(testFile, ar1 -> {
            if (ar1.failed()) {
              ar1.cause().printStackTrace();
              async.complete();
              return;
            }
            fs.writeFile(testFile, Buffer.buffer(UUIDKit.next()), ar2 -> {
              if (ar2.failed()) {
                ar2.cause().printStackTrace();
                async.complete();
                return;
              }
              fs.exists(testCopyFile, ar11 -> {
                if (ar11.failed()) {
                  ar11.cause().printStackTrace();
                  async.complete();
                  return;
                }
                if (ar11.result()) {
                  fs.delete(testCopyFile, ar12 -> {
                    if (ar12.failed()) {
                      ar12.cause().printStackTrace();
                      async.complete();
                      return;
                    }

                    fs.copy(testFile, testCopyFile, ar3 -> {
                      if (ar3.failed()) {
                        ar3.cause().printStackTrace();
                        async.complete();
                        return;
                      }
                      async.complete();
                    });
                  });
                  return;
                }

                fs.copy(testFile, testCopyFile, ar3 -> {
                  if (ar3.failed()) {
                    ar3.cause().printStackTrace();
                    async.complete();
                    return;
                  }
//              fs.deleteRecursive(testDir, true, ar4 -> {
//                if (ar4.failed()) {
//                  ar4.cause().printStackTrace();
//                }
//              });
                  async.complete();
                });
              });
            });
          });
        });
      });

      async.awaitSuccess();
    }).run();
  }


  @Test
  public void testVtomFs() {
    this.suite.test("vtom.read", ctx -> {
      Async async = ctx.async();
      VtomFileSystem.with(this.vertx).component()
        .step(Step.with(lifecycle -> Fs.exists(testDir)).ord(1))
        .step(Step.with(lifecycle -> {
          Scope scope = lifecycle.scope();
          Boolean exists = scope.value(1).value().bool();
          return Fs.mkdirs(testDir).skip(lifecycle.skip().condition(!exists).skipOrd(2));
        }).ord(2))
        .step(Step.with(lifecycle -> Fs.exists(testFile)).ord(3))
        .step(Step.with(lifecycle -> {
          Scope scope = lifecycle.scope();
          Boolean exists = scope.value(3).value().bool();
          if (exists)
            return Fs.delete(testFile);
          return Fs.createFile(testFile).skip(lifecycle.skip().condition(true).skipOrd(5));
        }).ord(4))
        .step(Step.with(lifecycle -> Fs.createFile(testFile)).ord(5))
        .step(Step.with(lifecycle -> Fs.writeFile(testFile).buffer(Buffer.buffer(UUIDKit.next()))).ord(6))
        .step(Step.with(lifecycle -> Fs.exists(testCopyFile)).ord(7))
        .step(Step.with(lifecycle -> {
          Scope scope = lifecycle.scope();
          Boolean exists = scope.value(7).value().bool();
          if (exists)
            return Fs.delete(testCopyFile);
          return Fs.copy(testFile).to(testCopyFile).skip(Skip.skip().condition(true).skipOrd(9));
        }).ord(8))
        .step(Step.with(lifecycle -> Fs.copy(testFile).to(testCopyFile)).ord(9))
        .join()
        .enqueue()
        .capture(Throwable::printStackTrace)
        .done(lifecycle -> {
          Scope scope = lifecycle.scope();
          System.out.println(scope);
        })
        .always(() -> async.complete());
      async.awaitSuccess();
    }).run();
  }


}
