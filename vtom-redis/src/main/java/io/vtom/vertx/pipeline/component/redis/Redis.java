package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.Script;
import io.vertx.redis.op.*;
import io.vtom.vertx.pipeline.lifecycle.skip.Skip;
import io.vtom.vertx.pipeline.step.StepIN;
import io.vtom.vertx.pipeline.step.StepOUT;

import java.util.List;
import java.util.Map;

public interface Redis extends StepIN {

  static Redis close() {
    return VtmRedisCommand.instance().close();
  }

  static Redis append(String key, String value) {
    return VtmRedisCommand.instance().append(key, value);
  }

  static Redis auth(String password) {
    return VtmRedisCommand.instance().auth(password);
  }

  static Redis bgrewriteaof() {
    return VtmRedisCommand.instance().bgrewriteaof();
  }

  static Redis bgsave() {
    return VtmRedisCommand.instance().bgsave();
  }

  static Redis bitcount(String key) {
    return VtmRedisCommand.instance().bitcount(key);
  }

  static Redis bitcountRange(String key, long start, long end) {
    return VtmRedisCommand.instance().bitcountRange(key, start, end);
  }

  static Redis bitop(BitOperation operation, String destkey, List<String> keys) {
    return VtmRedisCommand.instance().bitop(operation, destkey, keys);
  }

  static Redis bitpos(String key, int bit) {
    return VtmRedisCommand.instance().bitpos(key, bit);
  }

  static Redis bitposFrom(String key, int bit, int start) {
    return VtmRedisCommand.instance().bitposFrom(key, bit, start);
  }

  static Redis bitposRange(String key, int bit, int start, int stop) {
    return VtmRedisCommand.instance().bitposRange(key, bit, start, stop);
  }

  static Redis blpop(String key, int seconds) {
    return VtmRedisCommand.instance().blpop(key, seconds);
  }

  static Redis blpopMany(List<String> keys, int seconds) {
    return VtmRedisCommand.instance().blpopMany(keys, seconds);
  }

  static Redis brpop(String key, int seconds) {
    return VtmRedisCommand.instance().brpop(key, seconds);
  }

  static Redis brpopMany(List<String> keys, int seconds) {
    return VtmRedisCommand.instance().brpopMany(keys, seconds);
  }

  static Redis brpoplpush(String key, String destkey, int seconds) {
    return VtmRedisCommand.instance().brpoplpush(key, destkey, seconds);
  }

  static Redis clientKill(KillFilter filter) {
    return VtmRedisCommand.instance().clientKill(filter);
  }

  static Redis clientList() {
    return VtmRedisCommand.instance().clientList();
  }

  static Redis clientGetname() {
    return VtmRedisCommand.instance().clientGetname();
  }

  static Redis clientPause(long millis) {
    return VtmRedisCommand.instance().clientPause(millis);
  }

  static Redis clientSetname(String name) {
    return VtmRedisCommand.instance().clientSetname(name);
  }

  static Redis clusterAddslots(List<Long> slots) {
    return VtmRedisCommand.instance().clusterAddslots(slots);
  }

  static Redis clusterCountFailureReports(String nodeId) {
    return VtmRedisCommand.instance().clusterCountFailureReports(nodeId);
  }

  static Redis clusterCountkeysinslot(long slot) {
    return VtmRedisCommand.instance().clusterCountkeysinslot(slot);
  }

  static Redis clusterDelslots(long slot) {
    return VtmRedisCommand.instance().clusterDelslots(slot);
  }

  static Redis clusterDelslotsMany(List<Long> slots) {
    return VtmRedisCommand.instance().clusterDelslotsMany(slots);
  }

  static Redis clusterFailover() {
    return VtmRedisCommand.instance().clusterFailover();
  }

  static Redis clusterFailOverWithOptions(FailoverOptions options) {
    return VtmRedisCommand.instance().clusterFailOverWithOptions(options);
  }

  static Redis clusterForget(String nodeId) {
    return VtmRedisCommand.instance().clusterForget(nodeId);
  }

  static Redis clusterGetkeysinslot(long slot, long count) {
    return VtmRedisCommand.instance().clusterGetkeysinslot(slot, count);
  }

  static Redis clusterInfo() {
    return VtmRedisCommand.instance().clusterInfo();
  }

  static Redis clusterKeyslot(String key) {
    return VtmRedisCommand.instance().clusterKeyslot(key);
  }

  static Redis clusterMeet(String ip, long port) {
    return VtmRedisCommand.instance().clusterMeet(ip, port);
  }

  static Redis clusterNodes() {
    return VtmRedisCommand.instance().clusterNodes();
  }

  static Redis clusterReplicate(String nodeId) {
    return VtmRedisCommand.instance().clusterReplicate(nodeId);
  }

  static Redis clusterReset() {
    return VtmRedisCommand.instance().clusterReset();
  }

  static Redis clusterResetWithOptions(ResetOptions options) {
    return VtmRedisCommand.instance().clusterResetWithOptions(options);
  }

  static Redis clusterSaveconfig() {
    return VtmRedisCommand.instance().clusterSaveconfig();
  }

  static Redis clusterSetConfigEpoch(long epoch) {
    return VtmRedisCommand.instance().clusterSetConfigEpoch(epoch);
  }

  static Redis clusterSetslot(long slot, SlotCmd subcommand) {
    return VtmRedisCommand.instance().clusterSetslot(slot, subcommand);
  }

  static Redis clusterSetslotWithNode(long slot, SlotCmd subcommand, String nodeId) {
    return VtmRedisCommand.instance().clusterSetslotWithNode(slot, subcommand, nodeId);
  }

  static Redis clusterSlaves(String nodeId) {
    return VtmRedisCommand.instance().clusterSlaves(nodeId);
  }

  static Redis clusterSlots() {
    return VtmRedisCommand.instance().clusterSlots();
  }

  static Redis command() {
    return VtmRedisCommand.instance().command();
  }

  static Redis commandCount() {
    return VtmRedisCommand.instance().commandCount();
  }

  static Redis commandGetkeys() {
    return VtmRedisCommand.instance().commandGetkeys();
  }

  static Redis commandInfo(List<String> commands) {
    return VtmRedisCommand.instance().commandInfo(commands);
  }

  static Redis configGet(String parameter) {
    return VtmRedisCommand.instance().configGet(parameter);
  }

  static Redis configRewrite() {
    return VtmRedisCommand.instance().configRewrite();
  }

  static Redis configSet(String parameter, String value) {
    return VtmRedisCommand.instance().configSet(parameter, value);
  }

  static Redis configResetstat() {
    return VtmRedisCommand.instance().configResetstat();
  }

  static Redis dbsize() {
    return VtmRedisCommand.instance().dbsize();
  }

  static Redis debugObject(String key) {
    return VtmRedisCommand.instance().debugObject(key);
  }

  static Redis debugSegfault() {
    return VtmRedisCommand.instance().debugSegfault();
  }

  static Redis decr(String key) {
    return VtmRedisCommand.instance().decr(key);
  }

  static Redis decrby(String key, long decrement) {
    return VtmRedisCommand.instance().decrby(key, decrement);
  }

  static Redis del(String key) {
    return VtmRedisCommand.instance().del(key);
  }

  static Redis delMany(List<String> keys) {
    return VtmRedisCommand.instance().delMany(keys);
  }

  static Redis dump(String key) {
    return VtmRedisCommand.instance().dump(key);
  }

  static Redis echo(String message) {
    return VtmRedisCommand.instance().echo(message);
  }

  static Redis eval(String script, List<String> keys, List<String> args) {
    return VtmRedisCommand.instance().eval(script, keys, args);
  }

  static Redis evalsha(String sha1, List<String> keys, List<String> values) {
    return VtmRedisCommand.instance().evalsha(sha1, keys, values);
  }

  static Redis evalScript(Script script, List<String> keys, List<String> args) {
    return VtmRedisCommand.instance().evalScript(script, keys, args);
  }

  static Redis exists(String key) {
    return VtmRedisCommand.instance().exists(key);
  }

  static Redis existsMany(List<String> keys) {
    return VtmRedisCommand.instance().existsMany(keys);
  }

  static Redis expire(String key, long seconds) {
    return VtmRedisCommand.instance().expire(key, seconds);
  }

  static Redis expireat(String key, long seconds) {
    return VtmRedisCommand.instance().expireat(key, seconds);
  }

  static Redis flushall() {
    return VtmRedisCommand.instance().flushall();
  }

  static Redis flushdb() {
    return VtmRedisCommand.instance().flushdb();
  }

  static Redis get(String key) {
    return VtmRedisCommand.instance().get(key);
  }

  static Redis getBinary(String key) {
    return VtmRedisCommand.instance().getBinary(key);
  }

  static Redis getbit(String key, long offset) {
    return VtmRedisCommand.instance().getbit(key, offset);
  }

  static Redis getrange(String key, long start, long end) {
    return VtmRedisCommand.instance().getrange(key, start, end);
  }

  static Redis getset(String key, String value) {
    return VtmRedisCommand.instance().getset(key, value);
  }

  static Redis hdel(String key, String field) {
    return VtmRedisCommand.instance().hdel(key, field);
  }

  static Redis hdelMany(String key, List<String> fields) {
    return VtmRedisCommand.instance().hdelMany(key, fields);
  }

  static Redis hexists(String key, String field) {
    return VtmRedisCommand.instance().hexists(key, field);
  }

  static Redis hget(String key, String field) {
    return VtmRedisCommand.instance().hget(key, field);
  }

  static Redis hgetall(String key) {
    return VtmRedisCommand.instance().hgetall(key);
  }

  static Redis hincrby(String key, String field, long increment) {
    return VtmRedisCommand.instance().hincrby(key, field, increment);
  }

  static Redis hincrbyfloat(String key, String field, double increment) {
    return VtmRedisCommand.instance().hincrbyfloat(key, field, increment);
  }

  static Redis hkeys(String key) {
    return VtmRedisCommand.instance().hkeys(key);
  }

  static Redis hlen(String key) {
    return VtmRedisCommand.instance().hlen(key);
  }

  static Redis hmget(String key, List<String> fields) {
    return VtmRedisCommand.instance().hmget(key, fields);
  }

  static Redis hmset(String key, JsonObject values) {
    return VtmRedisCommand.instance().hmset(key, values);
  }

  static Redis hset(String key, String field, String value) {
    return VtmRedisCommand.instance().hset(key, field, value);
  }

  static Redis hsetnx(String key, String field, String value) {
    return VtmRedisCommand.instance().hsetnx(key, field, value);
  }

  static Redis hvals(String key) {
    return VtmRedisCommand.instance().hvals(key);
  }

  static Redis incr(String key) {
    return VtmRedisCommand.instance().incr(key);
  }

  static Redis incrby(String key, long increment) {
    return VtmRedisCommand.instance().incrby(key, increment);
  }

  static Redis incrbyfloat(String key, double increment) {
    return VtmRedisCommand.instance().incrbyfloat(key, increment);
  }

  static Redis info() {
    return VtmRedisCommand.instance().info();
  }

  static Redis infoSection(String section) {
    return VtmRedisCommand.instance().infoSection(section);
  }

  static Redis keys(String pattern) {
    return VtmRedisCommand.instance().keys(pattern);
  }

  static Redis lastsave() {
    return VtmRedisCommand.instance().lastsave();
  }

  static Redis lindex(String key, int index) {
    return VtmRedisCommand.instance().lindex(key, index);
  }

  static Redis linsert(String key, InsertOptions option, String pivot, String value) {
    return VtmRedisCommand.instance().linsert(key, option, pivot, value);
  }

  static Redis llen(String key) {
    return VtmRedisCommand.instance().llen(key);
  }

  static Redis lpop(String key) {
    return VtmRedisCommand.instance().lpop(key);
  }

  static Redis lpushMany(String key, List<String> values) {
    return VtmRedisCommand.instance().lpushMany(key, values);
  }

  static Redis lpush(String key, String value) {
    return VtmRedisCommand.instance().lpush(key, value);
  }

  static Redis lpushx(String key, String value) {
    return VtmRedisCommand.instance().lpushx(key, value);
  }

  static Redis lrange(String key, long from, long to) {
    return VtmRedisCommand.instance().lrange(key, from, to);
  }

  static Redis lrem(String key, long count, String value) {
    return VtmRedisCommand.instance().lrem(key, count, value);
  }

  static Redis lset(String key, long index, String value) {
    return VtmRedisCommand.instance().lset(key, index, value);
  }

  static Redis ltrim(String key, long from, long to) {
    return VtmRedisCommand.instance().ltrim(key, from, to);
  }

  static Redis mget(String key) {
    return VtmRedisCommand.instance().mget(key);
  }

  static Redis mgetMany(List<String> keys) {
    return VtmRedisCommand.instance().mgetMany(keys);
  }

  static Redis migrate(String host, int port, String key, int destdb, long timeout, MigrateOptions options) {
    return VtmRedisCommand.instance().migrate(host, port, key, destdb, timeout, options);
  }

  static Redis monitor() {
    return VtmRedisCommand.instance().monitor();
  }

  static Redis move(String key, int destdb) {
    return VtmRedisCommand.instance().move(key, destdb);
  }

  static Redis mset(JsonObject keyvals) {
    return VtmRedisCommand.instance().mset(keyvals);
  }

  static Redis msetnx(JsonObject keyvals) {
    return VtmRedisCommand.instance().msetnx(keyvals);
  }

  static Redis object(String key, ObjectCmd cmd) {
    return VtmRedisCommand.instance().object(key, cmd);
  }

  static Redis persist(String key) {
    return VtmRedisCommand.instance().persist(key);
  }

  static Redis pexpire(String key, long millis) {
    return VtmRedisCommand.instance().pexpire(key, millis);
  }

  static Redis pexpireat(String key, long millis) {
    return VtmRedisCommand.instance().pexpireat(key, millis);
  }

  static Redis pfadd(String key, String element) {
    return VtmRedisCommand.instance().pfadd(key, element);
  }

  static Redis pfaddMany(String key, List<String> elements) {
    return VtmRedisCommand.instance().pfaddMany(key, elements);
  }

  static Redis pfcount(String key) {
    return VtmRedisCommand.instance().pfcount(key);
  }

  static Redis pfcountMany(List<String> keys) {
    return VtmRedisCommand.instance().pfcountMany(keys);
  }

  static Redis pfmerge(String destkey, List<String> keys) {
    return VtmRedisCommand.instance().pfmerge(destkey, keys);
  }

  static Redis ping() {
    return VtmRedisCommand.instance().ping();
  }

  static Redis psetex(String key, long millis, String value) {
    return VtmRedisCommand.instance().psetex(key, millis, value);
  }

  static Redis psubscribe(String pattern) {
    return VtmRedisCommand.instance().psubscribe(pattern);
  }

  static Redis psubscribeMany(List<String> patterns) {
    return VtmRedisCommand.instance().psubscribeMany(patterns);
  }

  static Redis pubsubChannels(String pattern) {
    return VtmRedisCommand.instance().pubsubChannels(pattern);
  }

  static Redis pubsubNumsub(List<String> channels) {
    return VtmRedisCommand.instance().pubsubNumsub(channels);
  }

  static Redis pubsubNumpat() {
    return VtmRedisCommand.instance().pubsubNumpat();
  }

  static Redis pttl(String key) {
    return VtmRedisCommand.instance().pttl(key);
  }

  static Redis publish(String channel, String message) {
    return VtmRedisCommand.instance().publish(channel, message);
  }

  static Redis punsubscribe(List<String> patterns) {
    return VtmRedisCommand.instance().punsubscribe(patterns);
  }

  static Redis randomkey() {
    return VtmRedisCommand.instance().randomkey();
  }

  static Redis rename(String key, String newkey) {
    return VtmRedisCommand.instance().rename(key, newkey);
  }

  static Redis renamenx(String key, String newkey) {
    return VtmRedisCommand.instance().renamenx(key, newkey);
  }

  static Redis restore(String key, long millis, String serialized) {
    return VtmRedisCommand.instance().restore(key, millis, serialized);
  }

  static Redis role() {
    return VtmRedisCommand.instance().role();
  }

  static Redis rpop(String key) {
    return VtmRedisCommand.instance().rpop(key);
  }

  static Redis rpoplpush(String key, String destkey) {
    return VtmRedisCommand.instance().rpoplpush(key, destkey);
  }

  static Redis rpushMany(String key, List<String> values) {
    return VtmRedisCommand.instance().rpushMany(key, values);
  }

  static Redis rpush(String key, String value) {
    return VtmRedisCommand.instance().rpush(key, value);
  }

  static Redis rpushx(String key, String value) {
    return VtmRedisCommand.instance().rpushx(key, value);
  }

  static Redis sadd(String key, String member) {
    return VtmRedisCommand.instance().sadd(key, member);
  }

  static Redis saddMany(String key, List<String> members) {
    return VtmRedisCommand.instance().saddMany(key, members);
  }

  static Redis save() {
    return VtmRedisCommand.instance().save();
  }

  static Redis scard(String key) {
    return VtmRedisCommand.instance().scard(key);
  }

  static Redis scriptExists(String script) {
    return VtmRedisCommand.instance().scriptExists(script);
  }

  static Redis scriptExistsMany(List<String> scripts) {
    return VtmRedisCommand.instance().scriptExistsMany(scripts);
  }

  static Redis scriptFlush() {
    return VtmRedisCommand.instance().scriptFlush();
  }

  static Redis scriptKill() {
    return VtmRedisCommand.instance().scriptKill();
  }

  static Redis scriptLoad(String script) {
    return VtmRedisCommand.instance().scriptLoad(script);
  }

  static Redis sdiff(String key, List<String> cmpkeys) {
    return VtmRedisCommand.instance().sdiff(key, cmpkeys);
  }

  static Redis sdiffstore(String destkey, String key, List<String> cmpkeys) {
    return VtmRedisCommand.instance().sdiffstore(destkey, key, cmpkeys);
  }

  static Redis select(int dbindex) {
    return VtmRedisCommand.instance().select(dbindex);
  }

  static Redis set(String key, String value) {
    return VtmRedisCommand.instance().set(key, value);
  }

  static Redis setWithOptions(String key, String value, SetOptions options) {
    return VtmRedisCommand.instance().setWithOptions(key, value, options);
  }

  static Redis setBinary(String key, Buffer value) {
    return VtmRedisCommand.instance().setBinary(key, value);
  }

  static Redis setBinaryWithOptions(String key, Buffer value, SetOptions options) {
    return VtmRedisCommand.instance().setBinaryWithOptions(key, value, options);
  }

  static Redis setbit(String key, long offset, int bit) {
    return VtmRedisCommand.instance().setbit(key, offset, bit);
  }

  static Redis setex(String key, long seconds, String value) {
    return VtmRedisCommand.instance().setex(key, seconds, value);
  }

  static Redis setnx(String key, String value) {
    return VtmRedisCommand.instance().setnx(key, value);
  }

  static Redis setrange(String key, int offset, String value) {
    return VtmRedisCommand.instance().setrange(key, offset, value);
  }

  static Redis sinter(List<String> keys) {
    return VtmRedisCommand.instance().sinter(keys);
  }

  static Redis sinterstore(String destkey, List<String> keys) {
    return VtmRedisCommand.instance().sinterstore(destkey, keys);
  }

  static Redis sismember(String key, String member) {
    return VtmRedisCommand.instance().sismember(key, member);
  }

  static Redis slaveof(String host, int port) {
    return VtmRedisCommand.instance().slaveof(host, port);
  }

  static Redis slaveofNoone() {
    return VtmRedisCommand.instance().slaveofNoone();
  }

  static Redis slowlogGet(int limit) {
    return VtmRedisCommand.instance().slowlogGet(limit);
  }

  static Redis slowlogLen() {
    return VtmRedisCommand.instance().slowlogLen();
  }

  static Redis slowlogReset() {
    return VtmRedisCommand.instance().slowlogReset();
  }

  static Redis smembers(String key) {
    return VtmRedisCommand.instance().smembers(key);
  }

  static Redis smove(String key, String destkey, String member) {
    return VtmRedisCommand.instance().smove(key, destkey, member);
  }

  static Redis sort(String key, SortOptions options) {
    return VtmRedisCommand.instance().sort(key, options);
  }

  static Redis spop(String key) {
    return VtmRedisCommand.instance().spop(key);
  }

  static Redis spopMany(String key, int count) {
    return VtmRedisCommand.instance().spopMany(key, count);
  }

  static Redis srandmember(String key) {
    return VtmRedisCommand.instance().srandmember(key);
  }

  static Redis srandmemberCount(String key, int count) {
    return VtmRedisCommand.instance().srandmemberCount(key, count);
  }

  static Redis srem(String key, String member) {
    return VtmRedisCommand.instance().srem(key, member);
  }

  static Redis sremMany(String key, List<String> members) {
    return VtmRedisCommand.instance().sremMany(key, members);
  }

  static Redis strlen(String key) {
    return VtmRedisCommand.instance().strlen(key);
  }

  static Redis subscribe(String channel) {
    return VtmRedisCommand.instance().subscribe(channel);
  }

  static Redis subscribeMany(List<String> channels) {
    return VtmRedisCommand.instance().subscribeMany(channels);
  }

  static Redis sunion(List<String> keys) {
    return VtmRedisCommand.instance().sunion(keys);
  }

  static Redis sunionstore(String destkey, List<String> keys) {
    return VtmRedisCommand.instance().sunionstore(destkey, keys);
  }

  static Redis sync() {
    return VtmRedisCommand.instance().sync();
  }

  static Redis time() {
    return VtmRedisCommand.instance().time();
  }

  static Redis transaction() {
    return VtmRedisCommand.instance().transaction();
  }

  static Redis ttl(String key) {
    return VtmRedisCommand.instance().ttl(key);
  }

  static Redis type(String key) {
    return VtmRedisCommand.instance().type(key);
  }

  static Redis unsubscribe(List<String> channels) {
    return VtmRedisCommand.instance().unsubscribe(channels);
  }

  static Redis wait(long numSlaves, long timeout) {
    return VtmRedisCommand.instance().wait(numSlaves, timeout);
  }

  static Redis zadd(String key, double score, String member) {
    return VtmRedisCommand.instance().zadd(key, score, member);
  }

  static Redis zaddMany(String key, Map<String, Double> members) {
    return VtmRedisCommand.instance().zaddMany(key, members);
  }

  static Redis zcard(String key) {
    return VtmRedisCommand.instance().zcard(key);
  }

  static Redis zcount(String key, double min, double max) {
    return VtmRedisCommand.instance().zcount(key, min, max);
  }

  static Redis zincrby(String key, double increment, String member) {
    return VtmRedisCommand.instance().zincrby(key, increment, member);
  }

  static Redis zinterstore(String destkey, List<String> sets, AggregateOptions options) {
    return VtmRedisCommand.instance().zinterstore(destkey, sets, options);
  }

  static Redis zinterstoreWeighed(String destkey, Map<String, Double> sets, AggregateOptions options) {
    return VtmRedisCommand.instance().zinterstoreWeighed(destkey, sets, options);
  }

  static Redis zlexcount(String key, String min, String max) {
    return VtmRedisCommand.instance().zlexcount(key, min, max);
  }

  static Redis zrange(String key, long start, long stop) {
    return VtmRedisCommand.instance().zrange(key, start, stop);
  }

  static Redis zrangeWithOptions(String key, long start, long stop, RangeOptions options) {
    return VtmRedisCommand.instance().zrangeWithOptions(key, start, stop, options);
  }

  static Redis zrangebylex(String key, String min, String max, LimitOptions options) {
    return VtmRedisCommand.instance().zrangebylex(key, min, max, options);
  }

  static Redis zrangebyscore(String key, String min, String max, RangeLimitOptions options) {
    return VtmRedisCommand.instance().zrangebyscore(key, min, max, options);
  }

  static Redis zrank(String key, String member) {
    return VtmRedisCommand.instance().zrank(key, member);
  }

  static Redis zrem(String key, String member) {
    return VtmRedisCommand.instance().zrem(key, member);
  }

  static Redis zremMany(String key, List<String> members) {
    return VtmRedisCommand.instance().zremMany(key, members);
  }

  static Redis zremrangebylex(String key, String min, String max) {
    return VtmRedisCommand.instance().zremrangebylex(key, min, max);
  }

  static Redis zremrangebyrank(String key, long start, long stop) {
    return VtmRedisCommand.instance().zremrangebyrank(key, start, stop);
  }

  static Redis zremrangebyscore(String key, String min, String max) {
    return VtmRedisCommand.instance().zremrangebyscore(key, min, max);
  }

  static Redis zrevrange(String key, long start, long stop, RangeOptions options) {
    return VtmRedisCommand.instance().zrevrange(key, start, stop, options);
  }

  static Redis zrevrangebylex(String key, String max, String min, LimitOptions options) {
    return VtmRedisCommand.instance().zrevrangebylex(key, max, min, options);
  }

  static Redis zrevrangebyscore(String key, String max, String min, RangeLimitOptions options) {
    return VtmRedisCommand.instance().zrevrangebyscore(key, max, min, options);
  }

  static Redis zrevrank(String key, String member) {
    return VtmRedisCommand.instance().zrevrank(key, member);
  }

  static Redis zscore(String key, String member) {
    return VtmRedisCommand.instance().zscore(key, member);
  }

  static Redis zunionstore(String destkey, List<String> sets, AggregateOptions options) {
    return VtmRedisCommand.instance().zunionstore(destkey, sets, options);
  }

  static Redis zunionstoreWeighed(String key, Map<String, Double> sets, AggregateOptions options) {
    return VtmRedisCommand.instance().zunionstoreWeighed(key, sets, options);
  }

  static Redis scan(String cursor, ScanOptions options) {
    return VtmRedisCommand.instance().scan(cursor, options);
  }

  static Redis sscan(String key, String cursor, ScanOptions options) {
    return VtmRedisCommand.instance().sscan(key, cursor, options);
  }

  static Redis hscan(String key, String cursor, ScanOptions options) {
    return VtmRedisCommand.instance().hscan(key, cursor, options);
  }

  static Redis zscan(String key, String cursor, ScanOptions options) {
    return VtmRedisCommand.instance().zscan(key, cursor, options);
  }

  static Redis geoadd(String key, double longitude, double latitude, String member) {
    return VtmRedisCommand.instance().geoadd(key, longitude, latitude, member);
  }

  static Redis geoaddMany(String key, List<GeoMember> members) {
    return VtmRedisCommand.instance().geoaddMany(key, members);
  }

  static Redis geohash(String key, String member) {
    return VtmRedisCommand.instance().geohash(key, member);
  }

  static Redis geohashMany(String key, List<String> members) {
    return VtmRedisCommand.instance().geohashMany(key, members);
  }

  static Redis geopos(String key, String member) {
    return VtmRedisCommand.instance().geopos(key, member);
  }

  static Redis geoposMany(String key, List<String> members) {
    return VtmRedisCommand.instance().geoposMany(key, members);
  }

  static Redis geodist(String key, String member1, String member2) {
    return VtmRedisCommand.instance().geodist(key, member1, member2);
  }

  static Redis geodistWithUnit(String key, String member1, String member2, GeoUnit unit) {
    return VtmRedisCommand.instance().geodistWithUnit(key, member1, member2, unit);
  }

  static Redis georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
    return VtmRedisCommand.instance().georadius(key, longitude, latitude, radius, unit);
  }

  static Redis georadiusWithOptions(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusOptions options) {
    return VtmRedisCommand.instance().georadiusWithOptions(key, longitude, latitude, radius, unit, options);
  }

  static Redis georadiusbymember(String key, String member, double radius, GeoUnit unit) {
    return VtmRedisCommand.instance().georadiusbymember(key, member, radius, unit);
  }

  static Redis georadiusbymemberWithOptions(String key, String member, double radius, GeoUnit unit, GeoRadiusOptions options) {
    return VtmRedisCommand.instance().georadiusbymemberWithOptions(key, member, radius, unit, options);
  }

  static Redis clientReply(ClientReplyOptions options) {
    return VtmRedisCommand.instance().clientReply(options);
  }

  static Redis hstrlen(String key, String field) {
    return VtmRedisCommand.instance().hstrlen(key, field);
  }

  static Redis touch(String key) {
    return VtmRedisCommand.instance().touch(key);
  }

  static Redis touchMany(List<String> keys) {
    return VtmRedisCommand.instance().touchMany(keys);
  }

  static Redis scriptDebug(ScriptDebugOptions scriptDebugOptions) {
    return VtmRedisCommand.instance().scriptDebug(scriptDebugOptions);
  }

  static Redis bitfield(String key, BitFieldOptions bitFieldOptions) {
    return VtmRedisCommand.instance().bitfield(key, bitFieldOptions);
  }

  static Redis bitfieldWithOverflow(String key, BitFieldOptions commands, BitFieldOverflowOptions overflow) {
    return VtmRedisCommand.instance().bitfieldWithOverflow(key, commands, overflow);
  }

  static Redis unlink(String key) {
    return VtmRedisCommand.instance().unlink(key);
  }

  static Redis unlinkMany(List<String> keys) {
    return VtmRedisCommand.instance().unlinkMany(keys);
  }

  static Redis swapdb(int index1, int index2) {
    return VtmRedisCommand.instance().swapdb(index1, index2);
  }

  @Override
  Redis skip(Handler<Skip> stepskip);

  @Override
  StepOUT out();
}
