package vsauko.mineplayapi.api.customevents;

import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@Getter
public class PlayerDamageByBlockEvent extends PlayerDamageEvent{
  private final Block damager;

  public PlayerDamageByBlockEvent(Block damager, Player player, DamageCause damageCause, double damage) {
    super(player, damageCause, damage);
    this.damager = damager;
  }
}
