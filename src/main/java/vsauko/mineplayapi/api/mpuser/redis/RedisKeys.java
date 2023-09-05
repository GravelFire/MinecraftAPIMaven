package vsauko.mineplayapi.api.mpuser.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor@Getter
public enum RedisKeys {
  INIT("mineplaycore:init"),
  QUIT("mineplaycore:quit"),
  WITHDRAW_MONEY("mineplaycore:withdrawmoney"),
  DEPOSIT_MONEY("mineplaycore:depositmoney"),
  DEPOSIT_COINS("mineplaycore:depositcoins"),
  WITHDRAW_COINS("mineplaycore:withdrawcoins"),
  ADD_EXP("mineplaycore:addexp");

  private final String keyName;

}
