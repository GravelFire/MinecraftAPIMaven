package vsauko.mineplayapi.api.mpuser;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import org.bukkit.Bukkit;
import vsauko.mineplayapi.api.mpuser.redis.RedisManager;

public class MPUserManager {
  public static MPUserManager INSTANCE = new MPUserManager();
  private static final HashMap<UUID, MPUser> playerHashMap = new HashMap<>();
  @Getter
  private final RedisManager redisManager;

  public MPUserManager() {
    RedisURI.Builder redisURIBuilder = RedisURI.builder()
        .withHost("localhost")
        .withPort(6379)
        .withDatabase(0)
        .withTimeout(Duration.ofMillis(2000))
        .withClientName(Bukkit.getServer().getServerName())
        .withPassword("QgAbGWmW".toCharArray());
    Bukkit.getLogger().info("Connecting to redis server " + redisURIBuilder.build().toString() + "...");
    this.redisManager = new RedisManager(RedisClient.create(redisURIBuilder.build()));
    try {
      redisManager.isConnected().get(1, TimeUnit.SECONDS);
      Bukkit.getLogger().info("The connection to the Redis server was completed successfully! @Gerfesto");
    } catch (InterruptedException | ExecutionException | TimeoutException e) {
      throw new RuntimeException(e);
    }
  }

  public MPUser getMPUser(UUID uniqueId) {
    return playerHashMap.get(uniqueId);
  }
  public void putMPUser(UUID uniqueId, MPUser mpUser) {
    if (playerHashMap.containsKey(uniqueId)) return;
    playerHashMap.put(uniqueId, mpUser);
  }
  public void removeMPUser(UUID uniqueId) {
    playerHashMap.remove(uniqueId);
  }
}
