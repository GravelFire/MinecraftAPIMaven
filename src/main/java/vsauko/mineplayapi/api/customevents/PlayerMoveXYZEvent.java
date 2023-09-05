package vsauko.mineplayapi.api.customevents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerMoveXYZEvent extends BaseCustomEvent {

  private final Player player;
  private final Location from;
  private final Location to;
}
