

  
Vtom  
===
  
[![Build Status](https://drone.0u0.me/api/badges/feub/vtom-vertx/status.svg)](https://drone.0u0.me/feub/vtom-vertx)  
  
  
# 爲什麼使用 Vtom

Vtom 致力於解決 Vertx 的回調地獄問題, 在 Vertx 生態中, 一些成熟的同步框架 (例如使用 RxJava), 使用起來相對較麻煩, 需要理解其模式.

而 Vtom 採用了另外一種不同的設計方案, 採用了類似於 JavaScript 中的 Promise 方式來處理 Vertx 的回調問題. 在使用上相對容易理解.

並且 Vtom 的所有操作都是通過管道 (Pipeline) 進行的, 也就是在使用的過程中, 可以將多個異步操作同時打包在一個管道中進行執行, 這樣就能更加方便的進行異步代碼編寫.

# 沒有銀彈

也就是因爲採用了 Pipeline 的方式進行異步代碼的執行, 在獲取方法調用的返回值時就會變的更加痛苦, 因爲 Pipeline 中已經將返回的類型給丟了. 因此在瞭解這個爭相後才進行選擇是否使用 Vtom. 在 介紹 Pipeline 中將會對此部分進行詳細的說明. 請在看完後再確認是否要使用.

# Promise

Vtom 使用 Promise 的方式解決異步編碼的問題. Promise 使用的 [Enoa](https://github.com/fewensa/enoa) 項目中的 Promise 架構進行實現的.  Promise 與 JavaScript 中的 Promise 使用方式大致相同.

一個文件讀取的簡單案例:

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

關於 Promise 的部分. 需要知道, 一個 Pipeline 開始進行執行的時候 (調用 enqueue 方法), 會返回一個 `PipePromise` 這個 Promise 提供 `capture`, `done` 以及`always` 三個方法.

- capture
  用於接受 Pipeline 中出現異常後進行回調的方法.
- done
  Promise 執行完畢後會進行回調.
- always
  無論 Promise 執行成功與否都會進行回調.
 
通過 Promise 的方式就可以將 Vertx 中的回調參數轉換爲同步的方式書寫.

`PipePromise` 就是通過 `Pipeline` 的執行後獲取的.

## 同步代碼

注意, `Promise` 不適用與同步代碼, 如果你想要提供一個 Promise 的代碼, 你不能這麼寫

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

這是不行的, 因爲調用 `some` 時, 已經直接執行了 `Promise` 裏面的方法, 然而 `done` `always` 還尚未加入到 `Promiase` 中.

`Promise` 只適用與異步的方式. 因此這裏的代碼可以改成這樣.

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

讓這段代碼加入到 Vertx 的 Eventloop 中異步去執行. 但是當你這樣執行的時候, 你有可能也會發現仍然無法工作. 原因很簡單. 因爲執行的太快了, 仍然會發生 `Promise` 尚未填充就已經執行的情況, 這時候就需要使用延遲一段時間才能達到目的. 因此你的代碼是否適合用於 Promise 這就需要你去權衡了.


# Pipeline

Vtom 中的所有事件驅動都是通過 Pipeline 進行實現的, 例如在 [Promise](#Promise) 中所給的一個文件讀取的案例.

接下來針對 Pipeline 進行詳細分析.

在說明 Pipeline 之前, 先理解一些關於 Pipeline 中的相關概念. 這包括 [`Component`](#Component), [`Step`](#Step), [`LifeCycle`](#LifeCycle)

## Component

Pipeline 實際上只負責執行的流程, Component 則是 Pipeline 的實際執行者. 再次看一邊文件讀取的代碼.


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

這裏面 `VtomFileSystem` 便是一個 Component. 每個 Component 需要依賴一個 Pipeline 來進行執行. 

`VtomFileSystem.create().dependency(vertx)` 這樣我們就創建了一個文件操作的 Component, 同時也可以寫成這樣

```java
VtomFileSystem.create().dependency(Pipeline.pipeline(vertx))
```
組建創建完畢之後需要調用 `join` 方法加入到 `Pipeline` 中. 這樣 `Pipeline` 就接收到了一個執行者.

一個 `Pipeline` 支持多個 `Component`, 例如:

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

這樣就將兩個 `Component` 同時加入到一個 `Pipeline` 中, 當 `Pipeline` 執行後, 會分別調用者兩個組建. 關於執行的方式參見 [Step](#Step).

當然, 也支持不同的組建加入一個 `Pipeline` 中.

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



`Pipeline` 在執行後會返回一個 `PipePromise`, 在執行的過程中, 有任何一個 `Component` 發生異常都會跳出放棄後面的 `Component`, 而直接進入到 `PipePromise.capture` 中. 只有當所有的 `Component` 執行成功後才會進入到 `PipePromise.done`.

## Step

在看過之前的代碼後你應該注意到了 `Step.with(lifecycle -> xxx)` 這樣的代碼, 這裏便是 `Pipeline` 執行過程中最終進行執行的代碼. `Step` 由 `Component` 提供, 一個 `Component` 可以有多個 `Step`.

`Step` 有幾個重要的方法, 分別是 `id`, `ord`, `after`, 這和兩件事情相關, `id` 與 [`LifeCycle`](#LifeCycle) 有關, 而 `ord` 以及 `after` 與 `Pipeline` 的執行順序相關, 但是 `ord` 也可以在  [`LifeCycle`](#LifeCycle)  中使用.

- id

  單個 `Step` 的 ID, 默認會採用隨機生成一個 UUID, 在一個 `Pipeline` 中不可以出現重複的 ID.

- ord

   `Step` 在 `Pipeline` 中的執行順序, 將會按照正序排列, 有一個特殊值 0, 如果 `Step` 的 `ord` 設定爲 0, 那麼將會認爲這是一個不關心結果的 `Step`, 將會並行執行.
   而設定爲大於 0 的數字, 則會串行的方式進行執行. 也就是說每個步驟將會等待前一個步驟執行完畢後才會執行. 這將會很有用. 不用再將第二布執行的代碼放入到回調中了.

- after
  `after` 設定僅僅在 `ord` 爲 0 時才有效, 用於在某個 `Step` 執行完畢之後才進行並行的執行, 這樣就可以控制並行執行開始的時間.

`Step` 實際上分爲兩個部分 [`StepIN`](#StepIN), [`StepOUT`](#StepOUT), 接下來就來看這兩個部分

### StepIN

`StepIN` 是單個 `Step` 的執行代碼, 例如之前看到的 `Fs.readFile("test.txt")` 這便是一個 `StepIN`, 告訴 `Pipeline` 現在該讀取文件了.

通常 `StepIN` 的具體動作由各個 `Component` 自行實現. 但是有一個是需要注意的, 每個實現 `StepIN` 的類, 都會有一個 `skip` 的方法, 讓我們先來看下面的代碼, 再來說明 `skip`


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

這可能有點長. 這段代碼可以在[案例](https://github.com/fewensa/vtom-vertx/blob/dev/vtom-test/src/test/java/io/vtom/vertx/test/fs/VtomFileSystemTest.java)中找到.

講解這段代碼之前, 先看一下這段代碼在做什麼事情

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

看了這個流程之後, 相信聰明的你已經知道這段代碼在做什麼了. 首先判斷 testDir 目錄是否存在, 不存在則創建, 接着判斷 testFile 是否存在, 如果存在則刪除, 接着創建 testFile, 創建完畢後寫入一些內容. 寫入完畢後, 判斷 testCopyFile 是否存在, 如果存在同樣進行刪除, 之後複製 testFile 到 testCopyFile 結束整個流程.

那麼現在我們就來看一下 `skip` 的作用吧.

在這段代碼中有這樣代碼

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

第四步的時候, 首先獲取第三步的值, 第三步是用來判斷 testFile 是否存在. 考慮上方的流程, 如果發現 testFile 存在, 則需要進行刪除, 刪除後重新創建; 如果不存在直接進行創建即可. 那麼這裏就可以用 `skip` 實現.

第四步獲取到第三步檢測的值後, 發現如果存在, 則刪除 testFile, 接着就進入到第五步. 如果不存在第四步中就直接進行了文件的創建, 那麼第五步就不需要執行了, 因此直接在代碼中加入了 `.skip(skip -> skip.yes().skipOrd(5))` 用來跳過第五步.

再來看另外一個案例

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

這個是 `Vtom` 中數據庫操作的案例, 從 `ord` 可以發現這是第二步. 這一步執行的時候, 會先獲取第一步獲取到的博文分頁記錄, 我們需要給每一個文章的標籤加入到文章, 這裏我們將所有的文章 ID 取出來, 然後用 in 的方式一次性查詢出來, 然後拼接查詢標籤的 sql 語句; 當文章表還沒有任何數據的時候, rows 是沒有數據的, 那麼拼接完畢的 sql 就會是這樣

```sql
select mid, name from t_tags where mid in ()
```

這並非是一個合法的 sql 語句, 不能讓他執行. 這個時候, 就可以使用 `skip` 使其跳過, 但是有 ID 的時候要執行.

```java
.skip(skip -> skip.areYouSure(mids.isEmpty()).skipOrd(2))
```

就像這樣, `skip.areYouSure(mids.isEmpty()).skipOrd(2)` 告訴 Pipeline 當遇到 mids 是空的時候, 跳過 2 這個步驟.

是的, skip 可以跳過自身的步驟以及以後將要執行的步驟, 也可以使用 `skipId(id)` 來跳過一個 `Step` 只要在 `Step` 中設定了該 ID 即可.


### StepOUT

`StepOUT` 是通過 `StepIN` 獲取而來的, 而且是由 `Pipeline` 來獲取, 實際上這裏才是單個步驟最終執行的地方. 然而在使用的過程中通常不用擔心, 這是無需接觸到的.


## LifeCycle
 

`LifeCycle` 是 `Pipeline` 中最後一個要說明的部分了.

`LifeCycle` 在一個 `Pipeline` 中可以在執行時用來記錄或者交換值, 其中包括 [`Scope`](#Scope), [`Mount`](#Mount), 以及 `Vertx`


## Scope

`Scope` 會忠實的記錄每一個步驟執行的結果值. 但是有一點要注意的是, 因爲 `Pipeline` 將多個一步的操作打包在一起了, 執行完畢後將值寫入 `Scope` 中時就無法得知具體的值類型, 因此這需要考驗你對每個代碼的理解, 該操作返回的是什麼類型.

```java
Boolean exists = scope.value(3).as();
```

如之前看到的, 判斷文件是否存在, 返回的是一個 Boolean 值, 那麼可以直接使用 `as()` 將值轉換爲 Boolean 類型.

也可以使用 `to` 方法對值進行轉變, 例如上方的數據庫操作案例.

```java
Page<Row> page = scope.value(1).to(DBConverter.toPageRow());
```

通過 `to(DBConverter.toPageRow())` 將值轉變爲 `Page<Row>` 類型.


`Scope` 中存在一個 `danger` 方法, 這通常不應該由使用者進行操作, 這是 `Pipeline` 提供給 `Component` 用來記錄的地方. 例如在 [vtom-db](https://github.com/fewensa/vtom-vertx/tree/master/vtom-db) 中, 共用同一個 `SQLConnection` 就是通過這裏實現的, 包括事物的支持.

## Mount

在 `Scope` 中提及到的 `danger` 使用者不便操作, 因此在 `LifeCycle` 中提供了 `mount` 用來提供給在一個 `Pipeline` 中掛載數據的機會. 可以在這裏掛載數據, 在後續的步驟中都是可用的.



