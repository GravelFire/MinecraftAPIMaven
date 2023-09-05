package vsauko.mineplayapi.api.mpuser;

import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.ADD_EXP;
import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.DEPOSIT_COINS;
import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.DEPOSIT_MONEY;
import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.WITHDRAW_COINS;
import static vsauko.mineplayapi.api.mpuser.redis.RedisKeys.WITHDRAW_MONEY;

import java.util.UUID;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.utility.JsonUtil;
import vsauko.mineplayapi.api.mpuser.utility.LevelingUtil;
import vsauko.mineplayapi.api.mpuser.utility.Transactions;
import vsauko.mineplayapi.api.mpuser.utility.Transactions.Transaction;

@AllArgsConstructor
public class MPUser {

  @Getter private final UUID uniqueId;
  @Getter private int exp;
  @Getter private int coins;
  @Getter private int money;

  @Getter
  private final Supplier<Double> level = () -> LevelingUtil.getExactLevel(exp);


  public int getExpToLevelUP() {
    return (int) LevelingUtil.getExpFromLevelToNext(level.get());
  }


  public int getRequiredExp() {
    return (getLevel().get() > 1 ? exp - (int) LevelingUtil.getTotalExpToLevel(getLevel().get()) : exp);
  }

  public void addExp(int value, boolean sendData) {

    exp = exp + value;
    if (sendData) sendData(ADD_EXP.getKeyName(), value);

  }

  public void depositCoins(int value, boolean sendData) {
    if (value <= 0) return;
    coins = coins + value;
    if (sendData) sendData(DEPOSIT_COINS.getKeyName(), value);
  }
  public void depositMoney(int value, boolean sendData) {
    if (value <= 0) return;
    money = money + value;
    if (sendData) sendData(DEPOSIT_MONEY.getKeyName(), value);
  }

  public void withdrawCoins(int value, boolean sendData) {
    if (value <= 0) return;
    if (value > coins) {
      coins = 0;
      if (sendData) sendData(WITHDRAW_COINS.getKeyName(), coins);
      return;
    }
    coins = coins - value;
    if (sendData) sendData(WITHDRAW_COINS.getKeyName(), value);
  }

  public void withdrawMoney(int value, boolean sendData) {
    if (value <= 0) return;
    if (value > money) {
      money = 0;
      if (sendData) sendData(WITHDRAW_MONEY.getKeyName(), money);
      return;
    }
    money = money - value;
    if (sendData) sendData(WITHDRAW_MONEY.getKeyName(), value);
  }
  private void sendData(String type, int value) {
    BukkitAPI.MP_USER_MANAGER.getRedisManager().getConnectionPipeline(connection -> connection.publish(type, JsonUtil.toJson(new Transactions(new Transaction(uniqueId.toString(), value)))));

  }
}
