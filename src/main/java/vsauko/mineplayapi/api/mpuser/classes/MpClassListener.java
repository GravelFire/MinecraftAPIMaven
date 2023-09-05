package vsauko.mineplayapi.api.mpuser.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import vsauko.mineplayapi.api.mpuser.MPUser;
import vsauko.mineplayapi.api.mpuser.MPUserManager;

/**
 * Класс отвечающий за ивенты для классов
 */
public class MpClassListener implements Listener {
   /**
    * Менеджер юзеров АПИ
    */
   private final MPUserManager mpUserManager;

   public MpClassListener(MPUserManager mpUserManager) {
      this.mpUserManager = mpUserManager;
   }

   /**
    * Если игрок получил и/или нанес урон
    *
    * @param event
    */
   @EventHandler
   public void onDamageByEntity(EntityDamageByEntityEvent event) {
      proceedDamageTaken(event);
      if (event.getDamager() instanceof Player player) {
         MPUser user = mpUserManager.getMPUser(player.getUniqueId());
         if (user != null && user.getMpClass() != null) {
            user.getMpClass().onDealingDamage(event, player);
         }
      }
   }

   /**
    * Если игрок получил урон от блока
    *
    * @param event
    */
   @EventHandler
   public void onDamageByBlock(EntityDamageByBlockEvent event) {
      proceedDamageTaken(event);
   }

   /**
    * Выстрел из лука
    *
    * @param event
    */
   @EventHandler
   public void onShoot(EntityShootBowEvent event) {
      if (event.getEntity() instanceof Player player) {
         MPUser user = mpUserManager.getMPUser(player.getUniqueId());
         if (user != null && user.getMpClass() != null) {
            user.getMpClass().onShoot(event, player);
         }
      }
   }

   /**
    * Поставил блок
    *
    * @param event
    */
   @EventHandler
   public void onPlaceBlock(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      if (player != null) {
         MPUser user = mpUserManager.getMPUser(player.getUniqueId());
         if (user != null && user.getMpClass() != null) {
            user.getMpClass().onPlaceBlock(event, player);
         }
      }
   }

   /**
    * Сломал блок
    *
    * @param event
    */
   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (player != null) {
         MPUser user = mpUserManager.getMPUser(player.getUniqueId());
         if (user != null && user.getMpClass() != null) {
            user.getMpClass().onBreakBlock(event, player);
         }
      }
   }

   /**
    * Утилити метод для "получения урона"
    * @param event
    */
   private void proceedDamageTaken(EntityDamageEvent event) {
      if (event.getEntity() instanceof Player player) {
         MPUser user = mpUserManager.getMPUser(player.getUniqueId());
         if (user != null && user.getMpClass() != null) {
            user.getMpClass().onDamageTaken(event, player);
         }
      }
   }

}
