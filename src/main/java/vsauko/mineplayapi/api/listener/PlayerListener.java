package vsauko.mineplayapi.api.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import vsauko.mineplayapi.api.customevents.EntityDamageByPlayerEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageByBlockEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageByEntityEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageByPlayerEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageEvent;
import vsauko.mineplayapi.api.customevents.PlayerMoveBlockXYZEvent;
import vsauko.mineplayapi.api.customevents.PlayerMoveXYZEvent;

public class PlayerListener implements Listener {

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Entity damager = event.getDamager();
    Entity target = event.getEntity();

    if (damager.getType() == EntityType.PLAYER) {

      if (target.getType() == EntityType.PLAYER) {
        PlayerDamageByPlayerEvent playerDamageByPlayerEvent = new PlayerDamageByPlayerEvent(
            (Player) damager, (Player) target, event.getCause(), event.getDamage());
        Bukkit.getPluginManager().callEvent(playerDamageByPlayerEvent);
        event.setCancelled(playerDamageByPlayerEvent.isCancelled());
        return;
      }

      EntityDamageByPlayerEvent entityDamageByPlayerEvent = new EntityDamageByPlayerEvent(
          (Player) damager, target, event.getCause(), event.getDamage());
      Bukkit.getPluginManager().callEvent(entityDamageByPlayerEvent);
      event.setCancelled(entityDamageByPlayerEvent.isCancelled());
      return;
    }
    if (target.getType() == EntityType.PLAYER) {
      PlayerDamageByEntityEvent playerDamageByEntityEvent = new PlayerDamageByEntityEvent(damager,
          (Player) target, event.getCause(), event.getDamage());
      Bukkit.getPluginManager().callEvent(playerDamageByEntityEvent);
      event.setCancelled(playerDamageByEntityEvent.isCancelled());
    }
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onEntityDamage(EntityDamageEvent event) {
    if (event.getEntity().getType() != EntityType.PLAYER) return;
    PlayerDamageEvent playerDamageEvent = new PlayerDamageEvent((Player) event.getEntity(), event.getCause(),
        event.getDamage());
    Bukkit.getPluginManager().callEvent(playerDamageEvent);
    event.setCancelled(playerDamageEvent.isCancelled());
  }
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
    if (event.getEntity().getType() != EntityType.PLAYER) return;
    PlayerDamageByBlockEvent playerDamageEvent = new PlayerDamageByBlockEvent(event.getDamager(), (Player) event.getEntity(), event.getCause(), event.getDamage());
    Bukkit.getPluginManager().callEvent(playerDamageEvent);
    event.setCancelled(playerDamageEvent.isCancelled());
  }
  @EventHandler
  public void onMove(PlayerMoveEvent event) {
    // Вызов кастомного ивента PlayerMoveFullXYZEvent
    if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
        event.getFrom().getBlockY() != event.getTo().getBlockY() ||
        event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
      PlayerMoveBlockXYZEvent playerMoveBlockXYZEvent = new PlayerMoveBlockXYZEvent(
          event.getPlayer(), event.getFrom(), event.getTo());
      playerMoveBlockXYZEvent.setCancelled(event.isCancelled());
      Bukkit.getPluginManager().callEvent(playerMoveBlockXYZEvent);

      if (playerMoveBlockXYZEvent.getTo() != event.getTo()) {
        event.setTo(playerMoveBlockXYZEvent.getTo());
      }
      event.setCancelled(playerMoveBlockXYZEvent.isCancelled());

    }
    if (event.getFrom().getX() != event.getTo().getX() ||
        event.getFrom().getY() != event.getTo().getY() ||
        event.getFrom().getZ() != event.getTo().getZ()) {

      PlayerMoveXYZEvent playerMoveXYZEvent = new PlayerMoveXYZEvent(event.getPlayer(),
          event.getFrom(), event.getTo());
      playerMoveXYZEvent.setCancelled(event.isCancelled());
      Bukkit.getPluginManager().callEvent(playerMoveXYZEvent);

      if (playerMoveXYZEvent.getTo() != event.getTo()) {
        event.setTo(playerMoveXYZEvent.getTo());
      }
      event.setCancelled(playerMoveXYZEvent.isCancelled());
    }
  }
}
