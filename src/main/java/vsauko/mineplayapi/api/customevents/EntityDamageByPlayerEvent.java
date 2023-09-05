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

@Getter
public class EntityDamageByPlayerEvent extends EntityDamageEvent {

    private final Player damager;

    public EntityDamageByPlayerEvent(Player damager, Entity target, DamageCause damageCause, double damage) {
        super(target, damageCause, damage);
        this.damager = damager;
    }
}
