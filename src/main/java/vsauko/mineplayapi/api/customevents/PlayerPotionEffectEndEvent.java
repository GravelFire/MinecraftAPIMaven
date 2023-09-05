package vsauko.mineplayapi.api.customevents;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
@Getter
public class PlayerPotionEffectEndEvent extends BaseCustomEvent {

  private final Player player;
  private final PotionEffectType potionEffectType;
}
