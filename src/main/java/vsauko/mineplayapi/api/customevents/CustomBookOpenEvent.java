package vsauko.mineplayapi.api.customevents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class CustomBookOpenEvent extends BaseCustomEvent {

    private final Player player;
    private final Hand hand;
    private final ItemStack book;

    public enum Hand {
        MAIN_HAND, OFF_HAND
    }
}
