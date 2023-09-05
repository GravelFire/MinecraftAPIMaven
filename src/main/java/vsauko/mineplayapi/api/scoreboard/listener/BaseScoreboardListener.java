package vsauko.mineplayapi.api.scoreboard.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import vsauko.mineplayapi.api.scoreboard.BaseScoreboard;
import vsauko.mineplayapi.api.scoreboard.BaseScoreboardScope;

public class BaseScoreboardListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    for (BaseScoreboard baseScoreboard : BaseScoreboardScope.SCOPING_SCOREBOARD_MAP.get(BaseScoreboardScope.PUBLIC)) {
      baseScoreboard.setScoreboardToPlayer(player);
    }
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    for (BaseScoreboard baseScoreboard
        : BaseScoreboardScope.SCOPING_SCOREBOARD_MAP.values()) {

      baseScoreboard.getPlayerReceiverCollection().remove(player);
      baseScoreboard.getScoreboardQueueMap().remove(player);
    }
  }
}
