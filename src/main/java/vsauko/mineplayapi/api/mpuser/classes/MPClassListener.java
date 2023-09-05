package vsauko.mineplayapi.api.mpuser.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.customevents.EntityDamageByPlayerEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageByBlockEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageByEntityEvent;
import vsauko.mineplayapi.api.mpuser.MPUser;

public class MPClassListener implements Listener {

  /**
   * Нанес урон сущности
   */
  @EventHandler
  public void onDamageByEntity(EntityDamageByPlayerEvent event) {
    Player player = event.getDamager();
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.attackHandler() != null) {
      mpClass.attackHandler().accept(event);
    }
  }
  /**
   * Получил урон от сущности
   */
  @EventHandler
  public void onDamageEvent(PlayerDamageByEntityEvent event) {
    Player player = event.getPlayer();
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.takeDamageHandler() != null) {
      mpClass.takeDamageHandler().accept(event);
    }
  }


  /**
   * Получил урон от блока
   */
  @EventHandler
  public void onDamageByBlock(PlayerDamageByBlockEvent event) {
    Player player = event.getPlayer();
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.takeDamageHandler() != null) {
      mpClass.takeDamageHandler().accept(event);
    }
  }

  /**
   * Выстрел из лука
   */
  @EventHandler
  public void onShoot(EntityShootBowEvent event) {
    if (!(event.getEntity() instanceof Player player)) return;
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.takeDamageHandler() != null) {
      mpClass.shootHandler().accept(event);
    }
  }

  /**
   * Поставил блок
   */
  @EventHandler
  public void onPlaceBlock(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.takeDamageHandler() != null) {
      mpClass.placeHandler().accept(event);
    }
  }

  /**
   * Сломал блок
   */
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
    MPClass mpClass = user.getMpClass();

    if (mpClass != null && mpClass.takeDamageHandler() != null) {
      mpClass.breakHandler().accept(event);
    }
  }
}
