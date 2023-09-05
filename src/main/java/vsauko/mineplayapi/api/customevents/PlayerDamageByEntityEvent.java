package vsauko.mineplayapi.api.customevents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

@Getter
public class PlayerDamageByEntityEvent extends PlayerDamageEvent {
    private final Entity damager;

    public PlayerDamageByEntityEvent(Entity damager, Player target, DamageCause damageCause, double damage) {
        super(target, damageCause, damage);
        this.damager = damager;
    }
}
