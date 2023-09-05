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

@RequiredArgsConstructor
@Getter
public class EntityDamageByPlayerEvent extends BaseCustomEvent {

    private final Player damager;
    private final Entity target;
    private final EntityDamageEvent.DamageCause damageCause;
    private final double damage;
}
