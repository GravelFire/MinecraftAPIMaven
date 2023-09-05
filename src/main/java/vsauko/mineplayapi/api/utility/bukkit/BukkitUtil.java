package vsauko.mineplayapi.api.utility.bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class BukkitUtil {
  @Deprecated
  public static OfflinePlayer getOfflinePlayer(String player) {
    return Optional.ofNullable(Bukkit.getPlayer(player)).orElseGet(() -> (Player) Bukkit.getOfflinePlayer(player));
  }

  public static OfflinePlayer getOfflinePlayer(UUID uuid) {
    return Optional.ofNullable(Bukkit.getPlayer(uuid)).orElseGet(() -> (Player) Bukkit.getOfflinePlayer(uuid));
  }

  public static UUID getUniqueId(OfflinePlayer offlinePlayer) {
    return Optional.ofNullable(offlinePlayer.getPlayer()).map(Entity::getUniqueId).orElseGet(offlinePlayer::getUniqueId);
  }

  public static UUID getUniqueId(String player) {
    return getUniqueId(getOfflinePlayer(player));
  }

  public void sendActionBar(Player player, String message) {
    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
  }

  public static void dropItems(Location location, ItemStack[] items) {
    for (ItemStack item : items) {
      if (item != null && item.getType() != Material.AIR)
        location.getWorld().dropItemNaturally(location, item);
    }
  }

  public static void dropInventory(Player player) {
    Location location = player.getLocation();
    PlayerInventory inventory = player.getInventory();
    dropItems(location, inventory.getContents());
  }

  public static Player getDamager(EntityDamageEvent e) {
    if (e instanceof EntityDamageByEntityEvent) {
      EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)e;
      if (event.getDamager() instanceof Player) {
        return (Player)event.getDamager();
      }

      if (event.getDamager() instanceof Projectile) {
        Projectile projectile = (Projectile)event.getDamager();
        if (projectile.getShooter() instanceof Player) {
          return (Player)projectile.getShooter();
        }
      } else if (event.getDamager() instanceof TNTPrimed) {
        Entity source = ((TNTPrimed)event.getDamager()).getSource();
        if (source instanceof Player) {
          return (Player)source;
        }
      }
    }

    return null;
  }

  public static String getDamageName(DamageCause cause) {
    String name = "умер";
    switch(cause) {
      case ENTITY_ATTACK:
        name = "убит";
        break;
      case PROJECTILE:
        name = "застрелен";
        break;
      case SUFFOCATION:
        name = "задохнулся";
        break;
      case FALL:
        name = "упал с высоты";
        break;
      case FIRE:
        name = "сгорел";
        break;
      case FIRE_TICK:
        name = "сгорел";
        break;
      case LAVA:
        name = "утонул в лаве";
        break;
      case DROWNING:
        name = "утонул";
        break;
      case BLOCK_EXPLOSION:
        name = "взорвался";
        break;
      case ENTITY_EXPLOSION:
        name = "взорвался";
        break;
      case VOID:
        name = "упал в бездну";
        break;
      case LIGHTNING:
        name = "ударен молнией";
        break;
      case STARVATION:
        name = "помер с голоду";
        break;
      case POISON:
        name = "отравился";
        break;
      case MAGIC:
        name = "убит магией";
        break;
      case WITHER:
        name = "иссушён";
        break;
      case FALLING_BLOCK:
        name = "раздавлен";
        break;
      case THORNS:
        name = "напоролся на шипы";
    }

    return name;
  }



  public static void hidePlayer(Player player) {
    Bukkit.getOnlinePlayers().stream().filter(p -> (p != player)).forEach(p -> p.hidePlayer(player));
  }

  public static void resetPlayer(Player player) {
    player.setGameMode(GameMode.SURVIVAL);
    player.setMaxHealth(20.0D);
    player.setHealth(20.0D);
    player.setFoodLevel(20);
    player.setTotalExperience(0);
    player.setLevel(0);
    player.setExp(0.0F);
    for (PotionEffectType effect : PotionEffectType.values()) {
      try {
        player.removePotionEffect(effect);
      } catch (NullPointerException nullPointerException) {}
    }
    player.getInventory().clear();
  }

  @NonNull
  public static List<UUID> getAllUniqueIds() {
    return Arrays.stream(Bukkit.getOfflinePlayers())
        .parallel()
        .map(OfflinePlayer::getUniqueId)
        .collect(Collectors.toList());
  }

  @NonNull
  public static List<String> getAllPlayerNames() {
    return Arrays.stream(Bukkit.getOfflinePlayers())
        .parallel()
        .map(OfflinePlayer::getName)
        .collect(Collectors.toList());
  }

  public static int getPing(@NonNull final Player player) {
    Object entityPlayer;
    int ping = -9;
    try {
      entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
    } catch (Exception e) {
      Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting handle", e);
      return ping;
    }
    try {
      ping = entityPlayer.getClass().getField("ping").getInt(entityPlayer);
    } catch (Exception e) {
      try {
        ping = entityPlayer.getClass().getField("e").getInt(entityPlayer);
      } catch (Exception e1) {
        Bukkit.getServer().getLogger().log(Level.WARNING, "Unexpected error when getting ping", e1);
      }
    }
    return ping;
  }

}
