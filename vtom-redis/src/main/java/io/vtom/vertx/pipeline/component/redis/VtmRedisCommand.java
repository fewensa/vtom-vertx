package io.vtom.vertx.pipeline.component.redis;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.Script;
import io.vertx.redis.op.*;
import io.vtom.vertx.pipeline.tk.Pvtk;

import java.util.List;
import java.util.Map;

class VtmRedisCommand {

  private static class Holder {
    private static final VtmRedisCommand INSTANCE = new VtmRedisCommand();
  }

  static VtmRedisCommand instance() {
    return Holder.INSTANCE;
  }

  public Redis close() {
    return VtmRedisIn.with((client, handler) -> client.close(Pvtk.handleTo(handler)));
  }

  public Redis append(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.append(key, value, Pvtk.handleTo(handler)));
  }

  public Redis auth(String password) {
    return VtmRedisIn.with((client, handler) -> client.auth(password, Pvtk.handleTo(handler)));
  }

  public Redis bgrewriteaof() {
    return VtmRedisIn.with((client, handler) -> client.bgrewriteaof(Pvtk.handleTo(handler)));
  }

  public Redis bgsave() {
    return VtmRedisIn.with((client, handler) -> client.bgsave(Pvtk.handleTo(handler)));
  }

  public Redis bitcount(String key) {
    return VtmRedisIn.with((client, handler) -> client.bitcount(key, Pvtk.handleTo(handler)));
  }

  public Redis bitcountRange(String key, long start, long end) {
    return VtmRedisIn.with((client, handler) -> client.bitcountRange(key, start, end, Pvtk.handleTo(handler)));
  }

  public Redis bitop(BitOperation operation, String destkey, List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.bitop(operation, destkey, keys, Pvtk.handleTo(handler)));
  }

  public Redis bitpos(String key, int bit) {
    return VtmRedisIn.with((client, handler) -> client.bitpos(key, bit, Pvtk.handleTo(handler)));
  }

  public Redis bitposFrom(String key, int bit, int start) {
    return VtmRedisIn.with((client, handler) -> client.bitposFrom(key, bit, start, Pvtk.handleTo(handler)));
  }

  public Redis bitposRange(String key, int bit, int start, int stop) {
    return VtmRedisIn.with((client, handler) -> client.bitposRange(key, bit, start, stop, Pvtk.handleTo(handler)));
  }

  public Redis blpop(String key, int seconds) {
    return VtmRedisIn.with((client, handler) -> client.blpop(key, seconds, Pvtk.handleTo(handler)));
  }

  public Redis blpopMany(List<String> keys, int seconds) {
    return VtmRedisIn.with((client, handler) -> client.blpopMany(keys, seconds, Pvtk.handleTo(handler)));
  }

  public Redis brpop(String key, int seconds) {
    return VtmRedisIn.with((client, handler) -> client.brpop(key, seconds, Pvtk.handleTo(handler)));
  }

  public Redis brpopMany(List<String> keys, int seconds) {
    return VtmRedisIn.with((client, handler) -> client.brpopMany(keys, seconds, Pvtk.handleTo(handler)));
  }

  public Redis brpoplpush(String key, String destkey, int seconds) {
    return VtmRedisIn.with((client, handler) -> client.brpoplpush(key, destkey, seconds, Pvtk.handleTo(handler)));
  }

  public Redis clientKill(KillFilter filter) {
    return VtmRedisIn.with((client, handler) -> client.clientKill(filter, Pvtk.handleTo(handler)));
  }

  public Redis clientList() {
    return VtmRedisIn.with((client, handler) -> client.clientList(Pvtk.handleTo(handler)));
  }

  public Redis clientGetname() {
    return VtmRedisIn.with((client, handler) -> client.clientGetname(Pvtk.handleTo(handler)));
  }

  public Redis clientPause(long millis) {
    return VtmRedisIn.with((client, handler) -> client.clientPause(millis, Pvtk.handleTo(handler)));
  }

  public Redis clientSetname(String name) {
    return VtmRedisIn.with((client, handler) -> client.clientSetname(name, Pvtk.handleTo(handler)));
  }

  public Redis clusterAddslots(List<Long> slots) {
    return VtmRedisIn.with((client, handler) -> client.clusterAddslots(slots, Pvtk.handleTo(handler)));
  }

  public Redis clusterCountFailureReports(String nodeId) {
    return VtmRedisIn.with((client, handler) -> client.clusterCountFailureReports(nodeId, Pvtk.handleTo(handler)));
  }

  public Redis clusterCountkeysinslot(long slot) {
    return VtmRedisIn.with((client, handler) -> client.clusterCountkeysinslot(slot, Pvtk.handleTo(handler)));
  }

  public Redis clusterDelslots(long slot) {
    return VtmRedisIn.with((client, handler) -> client.clusterDelslots(slot, Pvtk.handleTo(handler)));
  }

  public Redis clusterDelslotsMany(List<Long> slots) {
    return VtmRedisIn.with((client, handler) -> client.clusterDelslotsMany(slots, Pvtk.handleTo(handler)));
  }

  public Redis clusterFailover() {
    return VtmRedisIn.with((client, handler) -> client.clusterFailover(Pvtk.handleTo(handler)));
  }

  public Redis clusterFailOverWithOptions(FailoverOptions options) {
    return VtmRedisIn.with((client, handler) -> client.clusterFailOverWithOptions(options, Pvtk.handleTo(handler)));
  }

  public Redis clusterForget(String nodeId) {
    return VtmRedisIn.with((client, handler) -> client.clusterForget(nodeId, Pvtk.handleTo(handler)));
  }

  public Redis clusterGetkeysinslot(long slot, long count) {
    return VtmRedisIn.with((client, handler) -> client.clusterGetkeysinslot(slot, count, Pvtk.handleTo(handler)));
  }

  public Redis clusterInfo() {
    return VtmRedisIn.with((client, handler) -> client.clusterInfo(Pvtk.handleTo(handler)));
  }

  public Redis clusterKeyslot(String key) {
    return VtmRedisIn.with((client, handler) -> client.clusterKeyslot(key, Pvtk.handleTo(handler)));
  }

  public Redis clusterMeet(String ip, long port) {
    return VtmRedisIn.with((client, handler) -> client.clusterMeet(ip, port, Pvtk.handleTo(handler)));
  }

  public Redis clusterNodes() {
    return VtmRedisIn.with((client, handler) -> client.clusterNodes(Pvtk.handleTo(handler)));
  }

  public Redis clusterReplicate(String nodeId) {
    return VtmRedisIn.with((client, handler) -> client.clusterReplicate(nodeId, Pvtk.handleTo(handler)));
  }

  public Redis clusterReset() {
    return VtmRedisIn.with((client, handler) -> client.clusterReset(Pvtk.handleTo(handler)));
  }

  public Redis clusterResetWithOptions(ResetOptions options) {
    return VtmRedisIn.with((client, handler) -> client.clusterResetWithOptions(options, Pvtk.handleTo(handler)));
  }

  public Redis clusterSaveconfig() {
    return VtmRedisIn.with((client, handler) -> client.clusterSaveconfig(Pvtk.handleTo(handler)));
  }

  public Redis clusterSetConfigEpoch(long epoch) {
    return VtmRedisIn.with((client, handler) -> client.clusterSetConfigEpoch(epoch, Pvtk.handleTo(handler)));
  }

  public Redis clusterSetslot(long slot, SlotCmd subcommand) {
    return VtmRedisIn.with((client, handler) -> client.clusterSetslot(slot, subcommand, Pvtk.handleTo(handler)));
  }

  public Redis clusterSetslotWithNode(long slot, SlotCmd subcommand, String nodeId) {
    return VtmRedisIn.with((client, handler) -> client.clusterSetslotWithNode(slot, subcommand, nodeId, Pvtk.handleTo(handler)));
  }

  public Redis clusterSlaves(String nodeId) {
    return VtmRedisIn.with((client, handler) -> client.clusterSlaves(nodeId, Pvtk.handleTo(handler)));
  }

  public Redis clusterSlots() {
    return VtmRedisIn.with((client, handler) -> client.clusterSlots(Pvtk.handleTo(handler)));
  }

  public Redis command() {
    return VtmRedisIn.with((client, handler) -> client.command(Pvtk.handleTo(handler)));
  }

  public Redis commandCount() {
    return VtmRedisIn.with((client, handler) -> client.commandCount(Pvtk.handleTo(handler)));
  }

  public Redis commandGetkeys() {
    return VtmRedisIn.with((client, handler) -> client.commandGetkeys(Pvtk.handleTo(handler)));
  }

  public Redis commandInfo(List<String> commands) {
    return VtmRedisIn.with((client, handler) -> client.commandInfo(commands, Pvtk.handleTo(handler)));
  }

  public Redis configGet(String parameter) {
    return VtmRedisIn.with((client, handler) -> client.configGet(parameter, Pvtk.handleTo(handler)));
  }

  public Redis configRewrite() {
    return VtmRedisIn.with((client, handler) -> client.configRewrite(Pvtk.handleTo(handler)));
  }

  public Redis configSet(String parameter, String value) {
    return VtmRedisIn.with((client, handler) -> client.configSet(parameter, value, Pvtk.handleTo(handler)));
  }

  public Redis configResetstat() {
    return VtmRedisIn.with((client, handler) -> client.configResetstat(Pvtk.handleTo(handler)));
  }

  public Redis dbsize() {
    return VtmRedisIn.with((client, handler) -> client.dbsize(Pvtk.handleTo(handler)));
  }

  public Redis debugObject(String key) {
    return VtmRedisIn.with((client, handler) -> client.debugObject(key, Pvtk.handleTo(handler)));
  }

  public Redis debugSegfault() {
    return VtmRedisIn.with((client, handler) -> client.debugSegfault(Pvtk.handleTo(handler)));
  }

  public Redis decr(String key) {
    return VtmRedisIn.with((client, handler) -> client.decr(key, Pvtk.handleTo(handler)));
  }

  public Redis decrby(String key, long decrement) {
    return VtmRedisIn.with((client, handler) -> client.decrby(key, decrement, Pvtk.handleTo(handler)));
  }

  public Redis del(String key) {
    return VtmRedisIn.with((client, handler) -> client.del(key, Pvtk.handleTo(handler)));
  }

  public Redis delMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.delMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis dump(String key) {
    return VtmRedisIn.with((client, handler) -> client.dump(key, Pvtk.handleTo(handler)));
  }

  public Redis echo(String message) {
    return VtmRedisIn.with((client, handler) -> client.echo(message, Pvtk.handleTo(handler)));
  }

  public Redis eval(String script, List<String> keys, List<String> args) {
    return VtmRedisIn.with((client, handler) -> client.eval(script, keys, args, Pvtk.handleTo(handler)));
  }

  public Redis evalsha(String sha1, List<String> keys, List<String> values) {
    return VtmRedisIn.with((client, handler) -> client.evalsha(sha1, keys, values, Pvtk.handleTo(handler)));
  }

  public Redis evalScript(Script script, List<String> keys, List<String> args) {
    return VtmRedisIn.with((client, handler) -> client.evalScript(script, keys, args, Pvtk.handleTo(handler)));
  }

  public Redis exists(String key) {
    return VtmRedisIn.with((client, handler) -> client.exists(key, Pvtk.handleTo(handler)));
  }

  public Redis existsMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.existsMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis expire(String key, long seconds) {
    return VtmRedisIn.with((client, handler) -> client.expire(key, seconds, Pvtk.handleTo(handler)));
  }

  public Redis expireat(String key, long seconds) {
    return VtmRedisIn.with((client, handler) -> client.expireat(key, seconds, Pvtk.handleTo(handler)));
  }

  public Redis flushall() {
    return VtmRedisIn.with((client, handler) -> client.flushall(Pvtk.handleTo(handler)));
  }

  public Redis flushdb() {
    return VtmRedisIn.with((client, handler) -> client.flushdb(Pvtk.handleTo(handler)));
  }

  public Redis get(String key) {
    return VtmRedisIn.with((client, handler) -> client.get(key, Pvtk.handleTo(handler)));
  }

  public Redis getBinary(String key) {
    return VtmRedisIn.with((client, handler) -> client.getBinary(key, Pvtk.handleTo(handler)));
  }

  public Redis getbit(String key, long offset) {
    return VtmRedisIn.with((client, handler) -> client.getbit(key, offset, Pvtk.handleTo(handler)));
  }

  public Redis getrange(String key, long start, long end) {
    return VtmRedisIn.with((client, handler) -> client.getrange(key, start, end, Pvtk.handleTo(handler)));
  }

  public Redis getset(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.getset(key, value, Pvtk.handleTo(handler)));
  }

  public Redis hdel(String key, String field) {
    return VtmRedisIn.with((client, handler) -> client.hdel(key, field, Pvtk.handleTo(handler)));
  }

  public Redis hdelMany(String key, List<String> fields) {
    return VtmRedisIn.with((client, handler) -> client.hdelMany(key, fields, Pvtk.handleTo(handler)));
  }

  public Redis hexists(String key, String field) {
    return VtmRedisIn.with((client, handler) -> client.hexists(key, field, Pvtk.handleTo(handler)));
  }

  public Redis hget(String key, String field) {
    return VtmRedisIn.with((client, handler) -> client.hget(key, field, Pvtk.handleTo(handler)));
  }

  public Redis hgetall(String key) {
    return VtmRedisIn.with((client, handler) -> client.hgetall(key, Pvtk.handleTo(handler)));
  }

  public Redis hincrby(String key, String field, long increment) {
    return VtmRedisIn.with((client, handler) -> client.hincrby(key, field, increment, Pvtk.handleTo(handler)));
  }

  public Redis hincrbyfloat(String key, String field, double increment) {
    return VtmRedisIn.with((client, handler) -> client.hincrbyfloat(key, field, increment, Pvtk.handleTo(handler)));
  }

  public Redis hkeys(String key) {
    return VtmRedisIn.with((client, handler) -> client.hkeys(key, Pvtk.handleTo(handler)));
  }

  public Redis hlen(String key) {
    return VtmRedisIn.with((client, handler) -> client.hlen(key, Pvtk.handleTo(handler)));
  }

  public Redis hmget(String key, List<String> fields) {
    return VtmRedisIn.with((client, handler) -> client.hmget(key, fields, Pvtk.handleTo(handler)));
  }

  public Redis hmset(String key, JsonObject values) {
    return VtmRedisIn.with((client, handler) -> client.hmset(key, values, Pvtk.handleTo(handler)));
  }

  public Redis hset(String key, String field, String value) {
    return VtmRedisIn.with((client, handler) -> client.hset(key, field, value, Pvtk.handleTo(handler)));
  }

  public Redis hsetnx(String key, String field, String value) {
    return VtmRedisIn.with((client, handler) -> client.hsetnx(key, field, value, Pvtk.handleTo(handler)));
  }

  public Redis hvals(String key) {
    return VtmRedisIn.with((client, handler) -> client.hvals(key, Pvtk.handleTo(handler)));
  }

  public Redis incr(String key) {
    return VtmRedisIn.with((client, handler) -> client.incr(key, Pvtk.handleTo(handler)));
  }

  public Redis incrby(String key, long increment) {
    return VtmRedisIn.with((client, handler) -> client.incrby(key, increment, Pvtk.handleTo(handler)));
  }

  public Redis incrbyfloat(String key, double increment) {
    return VtmRedisIn.with((client, handler) -> client.incrbyfloat(key, increment, Pvtk.handleTo(handler)));
  }

  public Redis info() {
    return VtmRedisIn.with((client, handler) -> client.info(Pvtk.handleTo(handler)));
  }

  public Redis infoSection(String section) {
    return VtmRedisIn.with((client, handler) -> client.infoSection(section, Pvtk.handleTo(handler)));
  }

  public Redis keys(String pattern) {
    return VtmRedisIn.with((client, handler) -> client.keys(pattern, Pvtk.handleTo(handler)));
  }

  public Redis lastsave() {
    return VtmRedisIn.with((client, handler) -> client.lastsave(Pvtk.handleTo(handler)));
  }

  public Redis lindex(String key, int index) {
    return VtmRedisIn.with((client, handler) -> client.lindex(key, index, Pvtk.handleTo(handler)));
  }

  public Redis linsert(String key, InsertOptions option, String pivot, String value) {
    return VtmRedisIn.with((client, handler) -> client.linsert(key, option, pivot, value, Pvtk.handleTo(handler)));
  }

  public Redis llen(String key) {
    return VtmRedisIn.with((client, handler) -> client.llen(key, Pvtk.handleTo(handler)));
  }

  public Redis lpop(String key) {
    return VtmRedisIn.with((client, handler) -> client.lpop(key, Pvtk.handleTo(handler)));
  }

  public Redis lpushMany(String key, List<String> values) {
    return VtmRedisIn.with((client, handler) -> client.lpushMany(key, values, Pvtk.handleTo(handler)));
  }

  public Redis lpush(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.lpush(key, value, Pvtk.handleTo(handler)));
  }

  public Redis lpushx(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.lpushx(key, value, Pvtk.handleTo(handler)));
  }

  public Redis lrange(String key, long from, long to) {
    return VtmRedisIn.with((client, handler) -> client.lrange(key, from, to, Pvtk.handleTo(handler)));
  }

  public Redis lrem(String key, long count, String value) {
    return VtmRedisIn.with((client, handler) -> client.lrem(key, count, value, Pvtk.handleTo(handler)));
  }

  public Redis lset(String key, long index, String value) {
    return VtmRedisIn.with((client, handler) -> client.lset(key, index, value, Pvtk.handleTo(handler)));
  }

  public Redis ltrim(String key, long from, long to) {
    return VtmRedisIn.with((client, handler) -> client.ltrim(key, from, to, Pvtk.handleTo(handler)));
  }

  public Redis mget(String key) {
    return VtmRedisIn.with((client, handler) -> client.mget(key, Pvtk.handleTo(handler)));
  }

  public Redis mgetMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.mgetMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis migrate(String host, int port, String key, int destdb, long timeout, MigrateOptions options) {
    return VtmRedisIn.with((client, handler) -> client.migrate(host, port, key, destdb, timeout, options, Pvtk.handleTo(handler)));
  }

  public Redis monitor() {
    return VtmRedisIn.with((client, handler) -> client.monitor(Pvtk.handleTo(handler)));
  }

  public Redis move(String key, int destdb) {
    return VtmRedisIn.with((client, handler) -> client.move(key, destdb, Pvtk.handleTo(handler)));
  }

  public Redis mset(JsonObject keyvals) {
    return VtmRedisIn.with((client, handler) -> client.mset(keyvals, Pvtk.handleTo(handler)));
  }

  public Redis msetnx(JsonObject keyvals) {
    return VtmRedisIn.with((client, handler) -> client.msetnx(keyvals, Pvtk.handleTo(handler)));
  }

  public Redis object(String key, ObjectCmd cmd) {
    return VtmRedisIn.with((client, handler) -> client.object(key, cmd, Pvtk.handleTo(handler)));
  }

  public Redis persist(String key) {
    return VtmRedisIn.with((client, handler) -> client.persist(key, Pvtk.handleTo(handler)));
  }

  public Redis pexpire(String key, long millis) {
    return VtmRedisIn.with((client, handler) -> client.pexpire(key, millis, Pvtk.handleTo(handler)));
  }

  public Redis pexpireat(String key, long millis) {
    return VtmRedisIn.with((client, handler) -> client.pexpireat(key, millis, Pvtk.handleTo(handler)));
  }

  public Redis pfadd(String key, String element) {
    return VtmRedisIn.with((client, handler) -> client.pfadd(key, element, Pvtk.handleTo(handler)));
  }

  public Redis pfaddMany(String key, List<String> elements) {
    return VtmRedisIn.with((client, handler) -> client.pfaddMany(key, elements, Pvtk.handleTo(handler)));
  }

  public Redis pfcount(String key) {
    return VtmRedisIn.with((client, handler) -> client.pfcount(key, Pvtk.handleTo(handler)));
  }

  public Redis pfcountMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.pfcountMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis pfmerge(String destkey, List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.pfmerge(destkey, keys, Pvtk.handleTo(handler)));
  }

  public Redis ping() {
    return VtmRedisIn.with((client, handler) -> client.ping(Pvtk.handleTo(handler)));
  }

  public Redis psetex(String key, long millis, String value) {
    return VtmRedisIn.with((client, handler) -> client.psetex(key, millis, value, Pvtk.handleTo(handler)));
  }

  public Redis psubscribe(String pattern) {
    return VtmRedisIn.with((client, handler) -> client.psubscribe(pattern, Pvtk.handleTo(handler)));
  }

  public Redis psubscribeMany(List<String> patterns) {
    return VtmRedisIn.with((client, handler) -> client.psubscribeMany(patterns, Pvtk.handleTo(handler)));
  }

  public Redis pubsubChannels(String pattern) {
    return VtmRedisIn.with((client, handler) -> client.pubsubChannels(pattern, Pvtk.handleTo(handler)));
  }

  public Redis pubsubNumsub(List<String> channels) {
    return VtmRedisIn.with((client, handler) -> client.pubsubNumsub(channels, Pvtk.handleTo(handler)));
  }

  public Redis pubsubNumpat() {
    return VtmRedisIn.with((client, handler) -> client.pubsubNumpat(Pvtk.handleTo(handler)));
  }

  public Redis pttl(String key) {
    return VtmRedisIn.with((client, handler) -> client.pttl(key, Pvtk.handleTo(handler)));
  }

  public Redis publish(String channel, String message) {
    return VtmRedisIn.with((client, handler) -> client.publish(channel, message, Pvtk.handleTo(handler)));
  }

  public Redis punsubscribe(List<String> patterns) {
    return VtmRedisIn.with((client, handler) -> client.punsubscribe(patterns, Pvtk.handleTo(handler)));
  }

  public Redis randomkey() {
    return VtmRedisIn.with((client, handler) -> client.randomkey(Pvtk.handleTo(handler)));
  }

  public Redis rename(String key, String newkey) {
    return VtmRedisIn.with((client, handler) -> client.rename(key, newkey, Pvtk.handleTo(handler)));
  }

  public Redis renamenx(String key, String newkey) {
    return VtmRedisIn.with((client, handler) -> client.renamenx(key, newkey, Pvtk.handleTo(handler)));
  }

  public Redis restore(String key, long millis, String serialized) {
    return VtmRedisIn.with((client, handler) -> client.restore(key, millis, serialized, Pvtk.handleTo(handler)));
  }

  public Redis role() {
    return VtmRedisIn.with((client, handler) -> client.role(Pvtk.handleTo(handler)));
  }

  public Redis rpop(String key) {
    return VtmRedisIn.with((client, handler) -> client.rpop(key, Pvtk.handleTo(handler)));
  }

  public Redis rpoplpush(String key, String destkey) {
    return VtmRedisIn.with((client, handler) -> client.rpoplpush(key, destkey, Pvtk.handleTo(handler)));
  }

  public Redis rpushMany(String key, List<String> values) {
    return VtmRedisIn.with((client, handler) -> client.rpushMany(key, values, Pvtk.handleTo(handler)));
  }

  public Redis rpush(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.rpush(key, value, Pvtk.handleTo(handler)));
  }

  public Redis rpushx(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.rpushx(key, value, Pvtk.handleTo(handler)));
  }

  public Redis sadd(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.sadd(key, member, Pvtk.handleTo(handler)));
  }

  public Redis saddMany(String key, List<String> members) {
    return VtmRedisIn.with((client, handler) -> client.saddMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis save() {
    return VtmRedisIn.with((client, handler) -> client.save(Pvtk.handleTo(handler)));
  }

  public Redis scard(String key) {
    return VtmRedisIn.with((client, handler) -> client.scard(key, Pvtk.handleTo(handler)));
  }

  public Redis scriptExists(String script) {
    return VtmRedisIn.with((client, handler) -> client.scriptExists(script, Pvtk.handleTo(handler)));
  }

  public Redis scriptExistsMany(List<String> scripts) {
    return VtmRedisIn.with((client, handler) -> client.scriptExistsMany(scripts, Pvtk.handleTo(handler)));
  }

  public Redis scriptFlush() {
    return VtmRedisIn.with((client, handler) -> client.scriptFlush(Pvtk.handleTo(handler)));
  }

  public Redis scriptKill() {
    return VtmRedisIn.with((client, handler) -> client.scriptKill(Pvtk.handleTo(handler)));
  }

  public Redis scriptLoad(String script) {
    return VtmRedisIn.with((client, handler) -> client.scriptLoad(script, Pvtk.handleTo(handler)));
  }

  public Redis sdiff(String key, List<String> cmpkeys) {
    return VtmRedisIn.with((client, handler) -> client.sdiff(key, cmpkeys, Pvtk.handleTo(handler)));
  }

  public Redis sdiffstore(String destkey, String key, List<String> cmpkeys) {
    return VtmRedisIn.with((client, handler) -> client.sdiffstore(destkey, key, cmpkeys, Pvtk.handleTo(handler)));
  }

  public Redis select(int dbindex) {
    return VtmRedisIn.with((client, handler) -> client.select(dbindex, Pvtk.handleTo(handler)));
  }

  public Redis set(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.set(key, value, Pvtk.handleTo(handler)));
  }

  public Redis setWithOptions(String key, String value, SetOptions options) {
    return VtmRedisIn.with((client, handler) -> client.setWithOptions(key, value, options, Pvtk.handleTo(handler)));
  }

  public Redis setBinary(String key, Buffer value) {
    return VtmRedisIn.with((client, handler) -> client.setBinary(key, value, Pvtk.handleTo(handler)));
  }

  public Redis setBinaryWithOptions(String key, Buffer value, SetOptions options) {
    return VtmRedisIn.with((client, handler) -> client.setBinaryWithOptions(key, value, options, Pvtk.handleTo(handler)));
  }

  public Redis setbit(String key, long offset, int bit) {
    return VtmRedisIn.with((client, handler) -> client.setbit(key, offset, bit, Pvtk.handleTo(handler)));
  }

  public Redis setex(String key, long seconds, String value) {
    return VtmRedisIn.with((client, handler) -> client.setex(key, seconds, value, Pvtk.handleTo(handler)));
  }

  public Redis setnx(String key, String value) {
    return VtmRedisIn.with((client, handler) -> client.setnx(key, value, Pvtk.handleTo(handler)));
  }

  public Redis setrange(String key, int offset, String value) {
    return VtmRedisIn.with((client, handler) -> client.setrange(key, offset, value, Pvtk.handleTo(handler)));
  }

  public Redis sinter(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.sinter(keys, Pvtk.handleTo(handler)));
  }

  public Redis sinterstore(String destkey, List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.sinterstore(destkey, keys, Pvtk.handleTo(handler)));
  }

  public Redis sismember(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.sismember(key, member, Pvtk.handleTo(handler)));
  }

  public Redis slaveof(String host, int port) {
    return VtmRedisIn.with((client, handler) -> client.slaveof(host, port, Pvtk.handleTo(handler)));
  }

  public Redis slaveofNoone() {
    return VtmRedisIn.with((client, handler) -> client.slaveofNoone(Pvtk.handleTo(handler)));
  }

  public Redis slowlogGet(int limit) {
    return VtmRedisIn.with((client, handler) -> client.slowlogGet(limit, Pvtk.handleTo(handler)));
  }

  public Redis slowlogLen() {
    return VtmRedisIn.with((client, handler) -> client.slowlogLen(Pvtk.handleTo(handler)));
  }

  public Redis slowlogReset() {
    return VtmRedisIn.with((client, handler) -> client.slowlogReset(Pvtk.handleTo(handler)));
  }

  public Redis smembers(String key) {
    return VtmRedisIn.with((client, handler) -> client.smembers(key, Pvtk.handleTo(handler)));
  }

  public Redis smove(String key, String destkey, String member) {
    return VtmRedisIn.with((client, handler) -> client.smove(key, destkey, member, Pvtk.handleTo(handler)));
  }

  public Redis sort(String key, SortOptions options) {
    return VtmRedisIn.with((client, handler) -> client.sort(key, options, Pvtk.handleTo(handler)));
  }

  public Redis spop(String key) {
    return VtmRedisIn.with((client, handler) -> client.spop(key, Pvtk.handleTo(handler)));
  }

  public Redis spopMany(String key, int count) {
    return VtmRedisIn.with((client, handler) -> client.spopMany(key, count, Pvtk.handleTo(handler)));
  }

  public Redis srandmember(String key) {
    return VtmRedisIn.with((client, handler) -> client.srandmember(key, Pvtk.handleTo(handler)));
  }

  public Redis srandmemberCount(String key, int count) {
    return VtmRedisIn.with((client, handler) -> client.srandmemberCount(key, count, Pvtk.handleTo(handler)));
  }

  public Redis srem(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.srem(key, member, Pvtk.handleTo(handler)));
  }

  public Redis sremMany(String key, List<String> members) {
    return VtmRedisIn.with((client, handler) -> client.sremMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis strlen(String key) {
    return VtmRedisIn.with((client, handler) -> client.strlen(key, Pvtk.handleTo(handler)));
  }

  public Redis subscribe(String channel) {
    return VtmRedisIn.with((client, handler) -> client.subscribe(channel, Pvtk.handleTo(handler)));
  }

  public Redis subscribeMany(List<String> channels) {
    return VtmRedisIn.with((client, handler) -> client.subscribeMany(channels, Pvtk.handleTo(handler)));
  }

  public Redis sunion(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.sunion(keys, Pvtk.handleTo(handler)));
  }

  public Redis sunionstore(String destkey, List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.sunionstore(destkey, keys, Pvtk.handleTo(handler)));
  }

  public Redis sync() {
    return VtmRedisIn.with((client, handler) -> client.sync(Pvtk.handleTo(handler)));
  }

  public Redis time() {
    return VtmRedisIn.with((client, handler) -> client.time(Pvtk.handleTo(handler)));
  }

  public Redis transaction() {
    return VtmRedisIn.with((client, handler) -> {
      client.transaction();
      handler.handle(Future.succeededFuture());
    });
  }

  public Redis ttl(String key) {
    return VtmRedisIn.with((client, handler) -> client.ttl(key, Pvtk.handleTo(handler)));
  }

  public Redis type(String key) {
    return VtmRedisIn.with((client, handler) -> client.type(key, Pvtk.handleTo(handler)));
  }

  public Redis unsubscribe(List<String> channels) {
    return VtmRedisIn.with((client, handler) -> client.unsubscribe(channels, Pvtk.handleTo(handler)));
  }

  public Redis wait(long numSlaves, long timeout) {
    return VtmRedisIn.with((client, handler) -> client.wait(numSlaves, timeout, Pvtk.handleTo(handler)));
  }

  public Redis zadd(String key, double score, String member) {
    return VtmRedisIn.with((client, handler) -> client.zadd(key, score, member, Pvtk.handleTo(handler)));
  }

  public Redis zaddMany(String key, Map<String, Double> members) {
    return VtmRedisIn.with((client, handler) -> client.zaddMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis zcard(String key) {
    return VtmRedisIn.with((client, handler) -> client.zcard(key, Pvtk.handleTo(handler)));
  }

  public Redis zcount(String key, double min, double max) {
    return VtmRedisIn.with((client, handler) -> client.zcount(key, min, max, Pvtk.handleTo(handler)));
  }

  public Redis zincrby(String key, double increment, String member) {
    return VtmRedisIn.with((client, handler) -> client.zincrby(key, increment, member, Pvtk.handleTo(handler)));
  }

  public Redis zinterstore(String destkey, List<String> sets, AggregateOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zinterstore(destkey, sets, options, Pvtk.handleTo(handler)));
  }

  public Redis zinterstoreWeighed(String destkey, Map<String, Double> sets, AggregateOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zinterstoreWeighed(destkey, sets, options, Pvtk.handleTo(handler)));
  }

  public Redis zlexcount(String key, String min, String max) {
    return VtmRedisIn.with((client, handler) -> client.zlexcount(key, min, max, Pvtk.handleTo(handler)));
  }

  public Redis zrange(String key, long start, long stop) {
    return VtmRedisIn.with((client, handler) -> client.zrange(key, start, stop, Pvtk.handleTo(handler)));
  }

  public Redis zrangeWithOptions(String key, long start, long stop, RangeOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrangeWithOptions(key, start, stop, options, Pvtk.handleTo(handler)));
  }

  public Redis zrangebylex(String key, String min, String max, LimitOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrangebylex(key, min, max, options, Pvtk.handleTo(handler)));
  }

  public Redis zrangebyscore(String key, String min, String max, RangeLimitOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrangebyscore(key, min, max, options, Pvtk.handleTo(handler)));
  }

  public Redis zrank(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.zrank(key, member, Pvtk.handleTo(handler)));
  }

  public Redis zrem(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.zrem(key, member, Pvtk.handleTo(handler)));
  }

  public Redis zremMany(String key, List<String> members) {
    return VtmRedisIn.with((client, handler) -> client.zremMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis zremrangebylex(String key, String min, String max) {
    return VtmRedisIn.with((client, handler) -> client.zremrangebylex(key, min, max, Pvtk.handleTo(handler)));
  }

  public Redis zremrangebyrank(String key, long start, long stop) {
    return VtmRedisIn.with((client, handler) -> client.zremrangebyrank(key, start, stop, Pvtk.handleTo(handler)));
  }

  public Redis zremrangebyscore(String key, String min, String max) {
    return VtmRedisIn.with((client, handler) -> client.zremrangebyscore(key, min, max, Pvtk.handleTo(handler)));
  }

  public Redis zrevrange(String key, long start, long stop, RangeOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrevrange(key, start, stop, options, Pvtk.handleTo(handler)));
  }

  public Redis zrevrangebylex(String key, String max, String min, LimitOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrevrangebylex(key, max, min, options, Pvtk.handleTo(handler)));
  }

  public Redis zrevrangebyscore(String key, String max, String min, RangeLimitOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zrevrangebyscore(key, max, min, options, Pvtk.handleTo(handler)));
  }

  public Redis zrevrank(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.zrevrank(key, member, Pvtk.handleTo(handler)));
  }

  public Redis zscore(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.zscore(key, member, Pvtk.handleTo(handler)));
  }

  public Redis zunionstore(String destkey, List<String> sets, AggregateOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zunionstore(destkey, sets, options, Pvtk.handleTo(handler)));
  }

  public Redis zunionstoreWeighed(String key, Map<String, Double> sets, AggregateOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zunionstoreWeighed(key, sets, options, Pvtk.handleTo(handler)));
  }

  public Redis scan(String cursor, ScanOptions options) {
    return VtmRedisIn.with((client, handler) -> client.scan(cursor, options, Pvtk.handleTo(handler)));
  }

  public Redis sscan(String key, String cursor, ScanOptions options) {
    return VtmRedisIn.with((client, handler) -> client.sscan(key, cursor, options, Pvtk.handleTo(handler)));
  }

  public Redis hscan(String key, String cursor, ScanOptions options) {
    return VtmRedisIn.with((client, handler) -> client.hscan(key, cursor, options, Pvtk.handleTo(handler)));
  }

  public Redis zscan(String key, String cursor, ScanOptions options) {
    return VtmRedisIn.with((client, handler) -> client.zscan(key, cursor, options, Pvtk.handleTo(handler)));
  }

  public Redis geoadd(String key, double longitude, double latitude, String member) {
    return VtmRedisIn.with((client, handler) -> client.geoadd(key, longitude, latitude, member, Pvtk.handleTo(handler)));
  }

  public Redis geoaddMany(String key, List<GeoMember> members) {
    return VtmRedisIn.with((client, handler) -> client.geoaddMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis geohash(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.geohash(key, member, Pvtk.handleTo(handler)));
  }

  public Redis geohashMany(String key, List<String> members) {
    return VtmRedisIn.with((client, handler) -> client.geohashMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis geopos(String key, String member) {
    return VtmRedisIn.with((client, handler) -> client.geopos(key, member, Pvtk.handleTo(handler)));
  }

  public Redis geoposMany(String key, List<String> members) {
    return VtmRedisIn.with((client, handler) -> client.geoposMany(key, members, Pvtk.handleTo(handler)));
  }

  public Redis geodist(String key, String member1, String member2) {
    return VtmRedisIn.with((client, handler) -> client.geodist(key, member1, member2, Pvtk.handleTo(handler)));
  }

  public Redis geodistWithUnit(String key, String member1, String member2, GeoUnit unit) {
    return VtmRedisIn.with((client, handler) -> client.geodistWithUnit(key, member1, member2, unit, Pvtk.handleTo(handler)));
  }

  public Redis georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
    return VtmRedisIn.with((client, handler) -> client.georadius(key, longitude, latitude, radius, unit, Pvtk.handleTo(handler)));
  }

  public Redis georadiusWithOptions(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusOptions options) {
    return VtmRedisIn.with((client, handler) -> client.georadiusWithOptions(key, longitude, latitude, radius, unit, options, Pvtk.handleTo(handler)));
  }

  public Redis georadiusbymember(String key, String member, double radius, GeoUnit unit) {
    return VtmRedisIn.with((client, handler) -> client.georadiusbymember(key, member, radius, unit, Pvtk.handleTo(handler)));
  }

  public Redis georadiusbymemberWithOptions(String key, String member, double radius, GeoUnit unit, GeoRadiusOptions options) {
    return VtmRedisIn.with((client, handler) -> client.georadiusbymemberWithOptions(key, member, radius, unit, options, Pvtk.handleTo(handler)));
  }

  public Redis clientReply(ClientReplyOptions options) {
    return VtmRedisIn.with((client, handler) -> client.clientReply(options, Pvtk.handleTo(handler)));
  }

  public Redis hstrlen(String key, String field) {
    return VtmRedisIn.with((client, handler) -> client.hstrlen(key, field, Pvtk.handleTo(handler)));
  }

  public Redis touch(String key) {
    return VtmRedisIn.with((client, handler) -> client.touch(key, Pvtk.handleTo(handler)));
  }

  public Redis touchMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.touchMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis scriptDebug(ScriptDebugOptions scriptDebugOptions) {
    return VtmRedisIn.with((client, handler) -> client.scriptDebug(scriptDebugOptions, Pvtk.handleTo(handler)));
  }

  public Redis bitfield(String key, BitFieldOptions bitFieldOptions) {
    return VtmRedisIn.with((client, handler) -> client.bitfield(key, bitFieldOptions, Pvtk.handleTo(handler)));
  }

  public Redis bitfieldWithOverflow(String key, BitFieldOptions commands, BitFieldOverflowOptions overflow) {
    return VtmRedisIn.with((client, handler) -> client.bitfieldWithOverflow(key, commands, overflow, Pvtk.handleTo(handler)));
  }

  public Redis unlink(String key) {
    return VtmRedisIn.with((client, handler) -> client.unlink(key, Pvtk.handleTo(handler)));
  }

  public Redis unlinkMany(List<String> keys) {
    return VtmRedisIn.with((client, handler) -> client.unlinkMany(keys, Pvtk.handleTo(handler)));
  }

  public Redis swapdb(int index1, int index2) {
    return VtmRedisIn.with((client, handler) -> client.swapdb(index1, index2, Pvtk.handleTo(handler)));
  }

}
