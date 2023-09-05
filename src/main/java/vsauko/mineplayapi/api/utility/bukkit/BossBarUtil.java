package vsauko.mineplayapi.api.utility.bukkit;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.utility.PercentUtil;

@UtilityClass
public class BossBarUtil {

  private static final ConcurrentHashMap<UUID, Object> MAP = new ConcurrentHashMap<>();


  /**
   * Создать босс бар игроку новый или изменить уже существующий
   *
   * @param player - Игрок
   * @param message - Ваше сообщение
   * @param percentage - Процент хп босс бара
   * @param barStyle - Стиль босс бара (Это если что про количество ячеек идёт речь)
   * @param barColor - Цвет босс бара
   */
  public void createBossBar(Player player, String message, BarStyle barStyle, BarColor barColor, float percentage) {
    BossBar bossBar = (BossBar) MAP.computeIfAbsent(player.getUniqueId(), k -> Bukkit.createBossBar(message, barColor, barStyle));

    bossBar.removePlayer(player);
    bossBar.addPlayer(player);
    bossBar.setProgress((float) PercentUtil.getPercent(percentage,100));
    bossBar.setTitle(message);
    bossBar.setVisible(true);
  }

  public void createBossBar(Player player, String message, float percentage) {
    createBossBar(player, message, BarStyle.SOLID, BarColor.PURPLE, percentage);
  }
  /**
   * Убрать у игрока босс бар
   *
   * @param player - Игрок
   */
  public void removeBossBar(Player player) {
    BossBar bossBar = (BossBar) MAP.get(player.getUniqueId());
    if (bossBar == null) return;
    bossBar.removePlayer(player);
    MAP.remove(player.getUniqueId());
  }
}
