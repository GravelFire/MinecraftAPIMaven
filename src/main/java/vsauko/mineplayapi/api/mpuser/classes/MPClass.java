package vsauko.mineplayapi.api.mpuser.classes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;

/**
 * Классы игроков внутри игр
 */
public abstract class MPClass {
   /**
    * Статы класса для "кластеризации"
    */

   protected final Map<Stats, Float> stats;

   protected MPClass(Map<Stats, Float> stats) {
      this.stats = stats;
   }

   public Map<Stats, Float> getStats() {
      return Collections.unmodifiableMap(stats);
   }

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

   /**
    * Применяет аттрибуты класса к игроку. Макс ХП, урон и т.д. и т.п.
    * @param player Игрок
    */

   public abstract void applyAttributes(Player player);

   public abstract void giveItemsKit(Player player);
   public abstract String[] lore();
   public abstract void onDamageTaken(EntityDamageEvent event, Player player);

   public abstract void onShoot(EntityShootBowEvent event, Player player);

   public abstract void onPlaceBlock(BlockPlaceEvent event, Player player);

   public abstract void onDealingDamage(EntityDamageEvent event, Player player);

   public abstract void onBreakBlock(BlockBreakEvent event, Player player);

   public Material preferredItemMaterial()
   {
      return Material.BOOK;
   }
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
