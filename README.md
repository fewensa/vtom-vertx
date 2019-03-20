
  
Vtom
===


[![Build Status](https://drone.0u0.me/api/badges/feub/vtom-vertx/status.svg)](https://drone.0u0.me/feub/vtom-vertx)  


# About

Vtom use pipeline and promise sloves vertx Callback hell.

# Sample

## Read a File

```java
VtomFileSystem.create().dependency(vertx)
  .step(Step.with(lifecycle -> Fs.readFile("test.txt")).ord(1))
  .join()
  .enqueue()
  .capture(Throwable::printStackTrace)
  .done(lifecycle -> {
    String text = lifecycle.scope(1).as();
    System.out.println(text);
   })
  .always(async::complete);
```

## Multiple component

```java
Pipeline pipeline = Pipeline.pipeline(vertx);

VtomFileSystem.create().dependency(pipeline)
  .step(Step.with(lifecycle -> Fs.readFile("test.txt")).ord(1))
  .join();
  
VtomFileSystem.create().dependency(pipeline)
  .step(Step.with(lifecycle -> Fs.delete("test.txt")).ord(2))
  .join();
  
pipeline.enqueue()
  .capture(Throwable::printStackTrace)
  .done(lifecycle -> {
    String text = lifecycle.scope(1).as();
    System.out.println(text);
   })
  .always(async::complete);
```

## Multiple different components

```java
Pipeline pipeline = Pipeline.pipeline(vertx);

VtomWebClient.create().dependency(pipeline)  
  .step(Step.with(lifecycle -> Vhc.request(HttpMethod.GET, "https://httpbin.org/get")
    .para("name", "Tom")
    .absolute()
  ).ord(1))  
  .join();

VtomFileSystem.create().dependency(pipeline)
  .step(Step.with(lifecycle -> Fs.readFile("test.txt")).ord(1))
  .join();

pipeline.enqueue()
  .capture(Throwable::printStackTrace)
  .done(lifecycle -> {
    String text = lifecycle.scope(1).as();
    System.out.println(text);
   })
  .always(async::complete);
```


## Advanced


```java
this.suite.test("vtom.read", ctx -> {
  Async async = ctx.async();
  long start = System.currentTimeMillis();
  VtomFileSystem.create().dependency(this.vertx)
    .step(Step.with(lifecycle -> Fs.exists(testDir)).ord(1))
    .step(Step.with(lifecycle -> {
      Scope scope = lifecycle.scope();
      Boolean exists = scope.value(1).as();
      return Fs.mkdirs(testDir).skip(skip -> skip.areYouSure(exists).skipOrd(2));
    }).ord(2))
    .step(Step.with(lifecycle -> Fs.exists(testFile)).ord(3))
    .step(Step.with(lifecycle -> {
      Scope scope = lifecycle.scope();
      Boolean exists = scope.value(3).value().bool();
      if (exists)
        return Fs.delete(testFile);
      return Fs.createFile(testFile).skip(skip -> skip.yes().skipOrd(5));
    }).ord(4))
    .step(Step.with(lifecycle -> Fs.createFile(testFile)).ord(5))
    .step(Step.with(lifecycle -> Fs.writeFile(testFile).buffer(Buffer.buffer(UUIDKit.next()))).ord(6))
    .step(Step.with(lifecycle -> Fs.exists(testCopyFile)).ord(7))
    .step(Step.with(lifecycle -> {
      Scope scope = lifecycle.scope();
      Boolean exists = scope.value(7).as();
      if (exists)
        return Fs.delete(testCopyFile);
      return null;
    }).ord(8))
    .step(Step.with(lifecycle -> Fs.copy(testFile).to(testCopyFile)).ord(9))
    .join()
    .enqueue()
    .capture(Throwable::printStackTrace)
    .done(lifecycle -> {
      Scope scope = lifecycle.scope();
      System.out.println(scope);
     })
    .always(async::complete);
  async.awaitSuccess();
  long end = System.currentTimeMillis();
  System.out.println("TIME: " + (end - start));
}).run();
```

Flow of this code



```text
!                      |
!                      |
!                      v            no     -----------------         fail
!                 <exists testDir>  ---->  | mkdir testDir |  -->-->-->-->-->-->--+
!                      |                   -----------------                      |
!     +<--<--<--<--|   | yes                    | ok                              v
!     |            |   v                        |                                 |
!     |      +<-- <exists testFile> <-----------+                              --------
!     v      |         v yes                                                   | done |
!     |    n |    -------------------                    fail                  --------
!     |    o v    | delete testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->--+
!     v      |    -------------------                                             ^
!     |      |         v                                                          |
!     |      v    -------------------                    fail                     |
!     v      +--> | create testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->--^
!     |           -------------------                                             |
!     |                v                                                          |
!     v           ------------------                     fail                     ^
!     |           | write testFile | -->-->-->-->-->-->-->-->-->-->-->-->-->-->-->+
!     |           ------------------                                              |
!     v                v              yes   -----------------------               ^
!     +<--<--<--<exists testCopyFile> --->  | delete testCopyFile |               |
!     |                v no                 -----------------------               |
!     v     ---------------------------------           |                         ^
!     |     | copy testFile to testCopyFile |   <-------+                         |
!     |     ---------------------------------                                     |
!     v            | ok     | fail                                                ^
!     |            v        v                                                     |
!     +-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->-->+
```






