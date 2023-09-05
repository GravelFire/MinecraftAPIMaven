package vsauko.mineplayapi.api.utility.bukkit;

import org.bukkit.entity.Player;

public class XPUtil {
  public static int getExp(Player paramPlayer) {
    return getExpFromLevel(paramPlayer.getLevel()) +
        Math.round(getExpToNext(paramPlayer.getLevel()) * paramPlayer.getExp());
  }

  public static int getExpFromLevel(int paramInt) {
    if (paramInt > 30)
      return (int)(4.5D * paramInt * paramInt - 162.5D * paramInt + 2220.0D);
    if (paramInt > 15)
      return (int)(2.5D * paramInt * paramInt - 40.5D * paramInt + 360.0D);
    return paramInt * paramInt + 6 * paramInt;
  }

  public static double getLevelFromExp(long paramLong) {
    if (paramLong > 1395L)
      return (Math.sqrt((72L * paramLong - 54215L)) + 325.0D) / 18.0D;
    if (paramLong > 315L)
      return Math.sqrt((40L * paramLong - 7839L)) / 10.0D + 8.1D;
    if (paramLong > 0L)
      return Math.sqrt((paramLong + 9L)) - 3.0D;
    return 0.0D;
  }

  private static int getExpToNext(int paramInt) {
    if (paramInt > 30)
      return 9 * paramInt - 158;
    if (paramInt > 15)
      return 5 * paramInt - 38;
    return 2 * paramInt + 7;
  }

  public static void giveExp(Player paramPlayer, int paramInt) {
    int b = getExp(paramPlayer) + paramInt;
    if (b < 0)
      b = 0;
    double d = getLevelFromExp(b);
    int i = (int)d;
    paramPlayer.setLevel(i);
    paramPlayer.setExp((float)(d - i));
  }

  public static void consumeExp(Player paramPlayer, int paramInt) {
    int b = getExp(paramPlayer) - paramInt;
    if (b < 0)
      b = 0;
    double d = getLevelFromExp(b);
    int i = (int)d;
    paramPlayer.setLevel(i);
    paramPlayer.setExp((float)(d - i));
  }
}
