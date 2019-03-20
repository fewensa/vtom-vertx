

  
Vtom  
===
  
[![Build Status](https://drone.0u0.me/api/badges/feub/vtom-vertx/status.svg)](https://drone.0u0.me/feub/vtom-vertx)  
  
  
# 为什么使用 Vtom

Vtom 致力于解决 Vertx 的回调地狱问题, 在 Vertx 生态中, 一些成熟的同步框架 (例如使用 RxJava), 使用起来相对较麻烦, 需要理解其模式.

而 Vtom 采用了另外一种不同的设计方案, 采用了类似于 JavaScript 中的 Promise 方式来处理 Vertx 的回调问题. 在使用上相对容易理解.

并且 Vtom 的所有操作都是通过管道 (Pipeline) 进行的, 也就是在使用的过程中, 可以将多个异步操作同时打包在一个管道中进行运行, 这样就能更加方便的进行异步代码编写.

# 没有银弹

也就是因为采用了 Pipeline 的方式进行异步代码的运行, 在获取方法调用的返回值时就会变的更加痛苦, 因为 Pipeline 中已经将返回的类型给丢了. 因此在了解这个争相后才进行选择是否使用 Vtom. 在 介绍 Pipeline 中将会对此部分进行详细的说明. 请在看完后再确认是否要使用.

# Promise

Vtom 使用 Promise 的方式解决异步编码的问题. Promise 使用的 [Enoa](https://github.com/fewensa/enoa) 项目中的 Promise 架构进行实现的.  Promise 与 JavaScript 中的 Promise 使用方式大致相同.

一个文档读取的简单案例:

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

关于 Promise 的部分. 需要知道, 一个 Pipeline 开始进行运行的时候 (调用 enqueue 方法), 会返回一个 `PipePromise` 这个 Promise 提供 `capture`, `done` 以及`always` 三个方法.

- capture
  用于接受 Pipeline 中出现异常后进行回调的方法.
- done
  Promise 运行完毕后会进行回调.
- always
  无论 Promise 运行成功与否都会进行回调.
 
通过 Promise 的方式就可以将 Vertx 中的回调参数转换为同步的方式书写.

`PipePromise` 就是通过 `Pipeline` 的运行后获取的.

## 同步代码

注意, `Promise` 不适用与同步代码, 如果你想要提供一个 Promise 的代码, 你不能这么写

```java
public PipePromise some() {
  EPDoneArgPromiseBuilder<PipeLifecycle> promise = Promise.builder().donearg();
  PipePromise pipepromise = new PipePromise(promise.build());
  String sometext = "Hello";
  promise.dones(done -> done.execute(sometext));
  promise.always.execute();
  return pipepromise;
}

public void run() {
  this.some()
    .done(ret -> System.out.println(ret))
    .always(() -> System.out.println("always"));
}
```

这是不行的, 因为调用 `some` 时, 已经直接运行了 `Promise` 里面的方法, 然而 `done` `always` 还尚未加入到 `Promiase` 中.

`Promise` 只适用与异步的方式. 因此这里的代码可以改成这样.

```java
public PipePromise some(Vertx vertx) {
  EPDoneArgPromiseBuilder<PipeLifecycle> promise = Promise.builder().donearg();
  PipePromise pipepromise = new PipePromise(promise.build());
  vertx.getOrCreateContext()
    .runOnContext(v -> {
      String sometext = "Hello";
      promise.dones(done -> done.execute(sometext));
      promise.always.execute();
    });
  return pipepromise;
}
```

让这段代码加入到 Vertx 的 Eventloop 中异步去运行. 但是当你这样运行的时候, 你有可能也会发现仍然无法工作. 原因很简单. 因为运行的太快了, 仍然会发生 `Promise` 尚未填充就已经运行的情况, 这时候就需要使用延迟一段时间才能达到目的. 因此你的代码是否适合用于 Promise 这就需要你去权衡了.


# Pipeline

Vtom 中的所有事件驱动都是通过 Pipeline 进行实现的, 例如在 [Promise](#Promise) 中所给的一个文档读取的案例.

接下来针对 Pipeline 进行详细分析.

在说明 Pipeline 之前, 先理解一些关于 Pipeline 中的相关概念. 这包括 [`Component`](#Component), [`Step`](#Step), [`LifeCycle`](#LifeCycle)

## Component

Pipeline 实际上只负责运行的流程, Component 则是 Pipeline 的实际运行者. 再次看一边文档读取的代码.


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

这里面 `VtomFileSystem` 便是一个 Component. 每个 Component 需要依赖一个 Pipeline 来进行运行. 

`VtomFileSystem.create().dependency(vertx)` 这样我们就创建了一个文档操作的 Component, 同时也可以写成这样

```java
VtomFileSystem.create().dependency(Pipeline.pipeline(vertx))
```
组建创建完毕之后需要调用 `join` 方法加入到 `Pipeline` 中. 这样 `Pipeline` 就接收到了一个运行者.

一个 `Pipeline` 支持多个 `Component`, 例如:

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

这样就将两个 `Component` 同时加入到一个 `Pipeline` 中, 当 `Pipeline` 运行后, 会分别调用者两个组建. 关于运行的方式参见 [Step](#Step).

当然, 也支持不同的组建加入一个 `Pipeline` 中.

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



`Pipeline` 在运行后会返回一个 `PipePromise`, 在运行的过程中, 有任何一个 `Component` 发生异常都会跳出放弃后面的 `Component`, 而直接进入到 `PipePromise.capture` 中. 只有当所有的 `Component` 运行成功后才会进入到 `PipePromise.done`.

## Step

在看过之前的代码后你应该注意到了 `Step.with(lifecycle -> xxx)` 这样的代码, 这里便是 `Pipeline` 运行过程中最终进行运行的代码. `Step` 由 `Component` 提供, 一个 `Component` 可以有多个 `Step`.

`Step` 有几个重要的方法, 分别是 `id`, `ord`, `after`, 这和两件事情相关, `id` 与 [`LifeCycle`](#LifeCycle) 有关, 而 `ord` 以及 `after` 与 `Pipeline` 的运行顺序相关, 但是 `ord` 也可以在  [`LifeCycle`](#LifeCycle)  中使用.

- id

  单个 `Step` 的 ID, 默认会采用随机生成一个 UUID, 在一个 `Pipeline` 中不可以出现重复的 ID.

- ord

   `Step` 在 `Pipeline` 中的运行顺序, 将会按照正序排列, 有一个特殊值 0, 如果 `Step` 的 `ord` 设置为 0, 那么将会认为这是一个不关心结果的 `Step`, 将会并行运行.
   而设置为大于 0 的数字, 则会串行的方式进行运行. 也就是说每个步骤将会等待前一个步骤运行完毕后才会运行. 这将会很有用. 不用再将第二布运行的代码放入到回调中了.

- after
  `after` 设置仅仅在 `ord` 为 0 时才有效, 用于在某个 `Step` 运行完毕之后才进行并行的运行, 这样就可以控制并行运行开始的时间.

`Step` 实际上分为两个部分 [`StepIN`](#StepIN), [`StepOUT`](#StepOUT), 接下来就来看这两个部分

### StepIN

`StepIN` 是单个 `Step` 的运行代码, 例如之前看到的 `Fs.readFile("test.txt")` 这便是一个 `StepIN`, 告诉 `Pipeline` 现在该读取文档了.

通常 `StepIN` 的具体动作由各个 `Component` 自行实现. 但是有一个是需要注意的, 每个实现 `StepIN` 的类, 都会有一个 `skip` 的方法, 让我们先来看下面的代码, 再来说明 `skip`


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

这可能有点长. 这段代码可以在[案例](https://github.com/fewensa/vtom-vertx/blob/dev/vtom-test/src/test/java/io/vtom/vertx/test/fs/VtomFileSystemTest.java)中找到.

讲解这段代码之前, 先看一下这段代码在做什么事情

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

看了这个流程之后, 相信聪明的你已经知道这段代码在做什么了. 首先判断 testDir 目录是否存在, 不存在则创建, 接着判断 testFile 是否存在, 如果存在则删除, 接着创建 testFile, 创建完毕后写入一些内容. 写入完毕后, 判断 testCopyFile 是否存在, 如果存在同样进行删除, 之后复制 testFile 到 testCopyFile 结束整个流程.

那么现在我们就来看一下 `skip` 的作用吧.

在这段代码中有这样代码

```java
.step(Step.with(lifecycle -> Fs.exists(testFile)).ord(3))
.step(Step.with(lifecycle -> {
  Scope scope = lifecycle.scope();
  Boolean exists = scope.value(3).as();
  if (exists)
    return Fs.delete(testFile);
  return Fs.createFile(testFile).skip(skip -> skip.yes().skipOrd(5));
}).ord(4))
.step(Step.with(lifecycle -> Fs.createFile(testFile)).ord(5))
```

第四步的时候, 首先获取第三步的值, 第三步是用来判断 testFile 是否存在. 考虑上方的流程, 如果发现 testFile 存在, 则需要进行删除, 删除后重新创建; 如果不存在直接进行创建即可. 那么这里就可以用 `skip` 实现.

第四步获取到第三步检测的值后, 发现如果存在, 则删除 testFile, 接着就进入到第五步. 如果不存在第四步中就直接进行了文档的创建, 那么第五步就不需要运行了, 因此直接在代码中加入了 `.skip(skip -> skip.yes().skipOrd(5))` 用来跳过第五步.

再来看另外一个案例

```java
.step(Step.with(lifecycle -> {
    Scope scope = lifecycle.scope();
    Page<Row> page = scope.value(1).to(DBConverter.toPageRow());
    List<Row> rows = page.getRows();
    StringBuilder tagsql = new StringBuilder("select mid, name from t_tags where mid in (");
    JsonArray mids = new JsonArray();
    for (int i = 0; i < rows.size(); i++) {
      mids.add(rows.get(i).string("id"));
      tagsql.append("?");
      if (i + 1 < rows.size())
        tagsql.append(",");
    }
    tagsql.append(")");
    return TSql.sql().select(tagsql.toString()).paras(mids)
      .skip(skip -> skip.areYouSure(mids.isEmpty()).skipOrd(2));
  }).ord(2))
```

这个是 `Vtom` 中数据库操作的案例, 从 `ord` 可以发现这是第二步. 这一步运行的时候, 会先获取第一步获取到的博文分页记录, 我们需要给每一个文章的标签加入到文章, 这里我们将所有的文章 ID 取出来, 然后用 in 的方式一次性查找出来, 然后拼接查找标签的 sql 语句; 当文章表还没有任何数据的时候, rows 是没有数据的, 那么拼接完毕的 sql 就会是这样

```sql
select mid, name from t_tags where mid in ()
```

这并非是一个合法的 sql 语句, 不能让他运行. 这个时候, 就可以使用 `skip` 使其跳过, 但是有 ID 的时候要运行.

```java
.skip(skip -> skip.areYouSure(mids.isEmpty()).skipOrd(2))
```

就像这样, `skip.areYouSure(mids.isEmpty()).skipOrd(2)` 告诉 Pipeline 当遇到 mids 是空的时候, 跳过 2 这个步骤.

是的, skip 可以跳过自身的步骤以及以后将要运行的步骤, 也可以使用 `skipId(id)` 来跳过一个 `Step` 只要在 `Step` 中设置了该 ID 即可.


### StepOUT

`StepOUT` 是通过 `StepIN` 获取而来的, 而且是由 `Pipeline` 来获取, 实际上这里才是单个步骤最终运行的地方. 然而在使用的过程中通常不用担心, 这是无需接触到的.


## LifeCycle
 

`LifeCycle` 是 `Pipeline` 中最后一个要说明的部分了.

`LifeCycle` 在一个 `Pipeline` 中可以在运行时用来记录或者交换值, 其中包括 [`Scope`](#Scope), [`Mount`](#Mount), 以及 `Vertx`


## Scope

`Scope` 会忠实的记录每一个步骤运行的结果值. 但是有一点要注意的是, 因为 `Pipeline` 将多个一步的操作打包在一起了, 运行完毕后将值写入 `Scope` 中时就无法得知具体的值类型, 因此这需要考验你对每个代码的理解, 该操作返回的是什么类型.

```java
Boolean exists = scope.value(3).as();
```

如之前看到的, 判断文档是否存在, 返回的是一个 Boolean 值, 那么可以直接使用 `as()` 将值转换为 Boolean 类型.

也可以使用 `to` 方法对值进行转变, 例如上方的数据库操作案例.

```java
Page<Row> page = scope.value(1).to(DBConverter.toPageRow());
```

通过 `to(DBConverter.toPageRow())` 将值转变为 `Page<Row>` 类型.


`Scope` 中存在一个 `danger` 方法, 这通常不应该由用户进行操作, 这是 `Pipeline` 提供给 `Component` 用来记录的地方. 例如在 [vtom-db](https://github.com/fewensa/vtom-vertx/tree/master/vtom-db) 中, 共用同一个 `SQLConnection` 就是通过这里实现的, 包括事物的支持.

## Mount

在 `Scope` 中提及到的 `danger` 用户不便操作, 因此在 `LifeCycle` 中提供了 `mount` 用来提供给在一个 `Pipeline` 中挂载数据的机会. 可以在这里挂载数据, 在后续的步骤中都是可用的.



