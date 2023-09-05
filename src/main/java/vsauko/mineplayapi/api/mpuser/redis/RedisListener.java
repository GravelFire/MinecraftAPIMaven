package vsauko.mineplayapi.api.mpuser.redis;

import io.lettuce.core.pubsub.RedisPubSubListener;

public abstract class RedisListener implements RedisPubSubListener<String, String> {

  @Override
  public abstract void message(String channel, String message);

  @Override
  public void message(String pattern, String channel, String message) {
  }

  @Override
  public void subscribed(String channel, long count) {
  }

  @Override
  public void psubscribed(String pattern, long count) {
  }

  @Override
  public void unsubscribed(String channel, long count) {
  }

  @Override
  public void punsubscribed(String pattern, long count) {
  }
}