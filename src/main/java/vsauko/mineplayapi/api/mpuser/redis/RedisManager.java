package vsauko.mineplayapi.api.mpuser.redis;

import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.*;

import com.google.gson.JsonObject;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.utility.JsonUtil;
import vsauko.mineplayapi.api.mpuser.MPUser;

public class RedisManager {

  protected static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private final RoundRobinConnectionPool<String, String> roundRobinConnectionPool;
  private final List<StatefulRedisPubSubConnection<String, String>> pubSubConnections;
  protected RedisClient lettuceRedisClient;

  public RedisManager(RedisClient lettuceRedisClient) {
    this.lettuceRedisClient = lettuceRedisClient;
    this.roundRobinConnectionPool = new RoundRobinConnectionPool<>(lettuceRedisClient::connect, 5);
    pubSubConnections = new CopyOnWriteArrayList<>();
    StatefulRedisPubSubConnection<String, String> connection = getPubSubConnection();
    connection.addListener(new RedisListener() {
           @Override
           public void message(String channel, String message) {
             JsonObject dataObject = JsonUtil.parse(message).getAsJsonObject().get("data").getAsJsonObject();
             UUID uniqueId = UUID.fromString(dataObject.get("uniqueId").getAsString());
             if (channel.equalsIgnoreCase(INIT.getKeyName())) {
               JsonObject balanceObject = dataObject.get("balance").getAsJsonObject();
               JsonObject levelObject = dataObject.get("level").getAsJsonObject();
               BukkitAPI.MP_USER_MANAGER.putMPUser(uniqueId, new MPUser(
                   uniqueId,
                   levelObject.get("exp").getAsInt(),
                   balanceObject.get("coins").getAsInt(),
                   balanceObject.get("money").getAsInt()));
               return;
             }

             if (channel.equalsIgnoreCase(QUIT.getKeyName())) {
               BukkitAPI.MP_USER_MANAGER.removeMPUser(uniqueId);
               return;
             }

             MPUser mpUser = BukkitAPI.MP_USER_MANAGER.getMPUser(uniqueId);
             int value = dataObject.get("value").getAsInt();
             if (channel.equalsIgnoreCase(ADD_EXP.getKeyName())) {
               mpUser.addExp(value, false);
               return;
             }
             if (channel.equalsIgnoreCase(DEPOSIT_COINS.getKeyName())) {
               mpUser.depositCoins(value, false);
               return;
             }
             if (channel.equalsIgnoreCase(DEPOSIT_MONEY.getKeyName())) {
               mpUser.depositMoney(value, false);
               return;
             }
             if (channel.equalsIgnoreCase(WITHDRAW_COINS.getKeyName())) {
               mpUser.withdrawCoins(value, false);
               return;
             }
             if (channel.equalsIgnoreCase(WITHDRAW_MONEY.getKeyName())) {
               mpUser.withdrawMoney(value, false);
             }
           }
    });
    connection.async().subscribe(
        INIT.getKeyName(),
        QUIT.getKeyName(),
        WITHDRAW_MONEY.getKeyName(),
        WITHDRAW_COINS.getKeyName(),
        DEPOSIT_MONEY.getKeyName(),
        DEPOSIT_COINS.getKeyName(),
        ADD_EXP.getKeyName());
  }

  public <T> CompletionStage<T> getConnectionAsync(Function<RedisAsyncCommands<String, String>, CompletionStage<T>> redisCallBack) {
    return redisCallBack.apply(roundRobinConnectionPool.get().async());
  }
  public <T> CompletionStage<T> getConnectionPipeline(Function<RedisAsyncCommands<String, String>, CompletionStage<T>> redisCallBack) {
    StatefulRedisConnection<String, String> connection = this.roundRobinConnectionPool.get();
    connection.setAutoFlushCommands(false);
    CompletionStage<T> completionStage = redisCallBack.apply(connection.async());
    connection.flushCommands();
    connection.setAutoFlushCommands(true);
    return completionStage;
  }

  public StatefulRedisPubSubConnection<String, String> getPubSubConnection() {
    StatefulRedisPubSubConnection<String, String> pubSubConnection = lettuceRedisClient.connectPubSub();
    pubSubConnections.add(pubSubConnection);
    return pubSubConnection;
  }

  public void close() {
    pubSubConnections.forEach(StatefulRedisPubSubConnection::close);
    lettuceRedisClient.shutdown(Duration.ofSeconds(1), Duration.ofSeconds(1));
    executorService.shutdown();
  }

  public RedisFuture<String> isConnected() {
    return roundRobinConnectionPool.get().async().get("test");
  }

}
