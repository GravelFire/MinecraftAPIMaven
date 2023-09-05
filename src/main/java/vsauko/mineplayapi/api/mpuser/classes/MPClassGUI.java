package vsauko.mineplayapi.api.mpuser.classes;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.inventory.impl.BasePaginatedInventory;
import vsauko.mineplayapi.api.inventory.markup.BaseInventoryRhombusMarkup;
import vsauko.mineplayapi.api.mpuser.MPUser;
import vsauko.mineplayapi.api.utility.bukkit.ItemUtil;

public class MPClassGUI extends BasePaginatedInventory {

   public MPClassGUI() {
      super(6, "§eВыбор класса:");
   }

   @Override
   public void drawInventory(@NonNull Player player) {
     setItemMarkup(new BaseInventoryRhombusMarkup(inventoryRows));
     for (MPClass mpClass : MpClassManager.getRegisteredClasses()) {
       addItemToMarkup(ItemUtil.newBuilder(mpClass.getMaterial()).setName(mpClass.getLocalizedName()).setLore(mpClass.lore()).build(),
           (player1, event) -> {
             MPUser user = BukkitAPI.MP_USER_MANAGER.getMPUser(player.getUniqueId());
             user.setMpClass(mpClass);
             mpClass.attributes().accept(player);
             mpClass.kit().accept(player);
             Bukkit.broadcastMessage("§7Игрок §e" + player.getName() + "§7 выбрал класс §e" + mpClass.getLocalizedName());
           });
     }
   }
}
