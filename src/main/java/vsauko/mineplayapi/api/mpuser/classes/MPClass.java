package vsauko.mineplayapi.api.mpuser.classes;

import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import vsauko.mineplayapi.api.customevents.EntityDamageByPlayerEvent;
import vsauko.mineplayapi.api.customevents.PlayerDamageEvent;

/**
 * Классы игроков внутри игр
 */
@RequiredArgsConstructor
public abstract class MPClass {
   /**
    * Статы класса для "кластеризации"
    */

   @Getter
   protected final Map<Stats, Float> stats;

   public abstract Material getMaterial();

   /**
    * Машинное имя класса. Используется для конифгов, прав и сравнений в предметах
    *
    * @return Машинное имя класса
    */
   public abstract String getName();

   /**
    * Право на выбор этого класса
    *
    * @return право
    */

   public String getPermission() {
      return "mineplay.classes." + getName();
   }

   /**
    * Локализованное имя. Выводится игрокам
    * @return локализованное имя
    */
   public abstract String getLocalizedName();


   public abstract String[] lore();
   public abstract Consumer<Player> attributes();
   public abstract Consumer<Player> kit();

   public abstract Consumer<EntityShootBowEvent> shootHandler();
   public abstract Consumer<PlayerDamageEvent> takeDamageHandler();
   public abstract Consumer<EntityDamageByPlayerEvent> attackHandler();
   public abstract Consumer<BlockPlaceEvent> placeHandler();
   public abstract Consumer<BlockBreakEvent> breakHandler();

   /**
    * Статы-координаты для кластеризации
    */
   public enum Stats {
      DAMAGE,
      STRENGTH,
      AGILITY,
      MAGIC,
      DISTANCE
   }
}
