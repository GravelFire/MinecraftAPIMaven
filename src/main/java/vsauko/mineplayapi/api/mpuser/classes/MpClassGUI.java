package vsauko.mineplayapi.api.mpuser.classes;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.inventory.impl.BaseSimpleInventory;
import vsauko.mineplayapi.api.mpuser.MPUser;
import vsauko.mineplayapi.api.mpuser.MPUserManager;
import vsauko.mineplayapi.api.utility.bukkit.ItemUtil;

public class MpClassGUI extends BaseSimpleInventory {
   private final MPUserManager mpUserManager;

   public MpClassGUI(MPUserManager userManager) {
      super(((MpClassManager.countClasses()) / 9) + 1, "Выбор класса");
      this.mpUserManager = userManager;
   }

   @Override
   public void drawInventory(@NonNull Player player) {
      int slot = 0;
      for (MPClass mpClass : MpClassManager.getRegisteredClasses()) {
         addItem(slot++,
                 ItemUtil.newBuilder(Material.BOOK).setName(mpClass.getName()).setLore(mpClass.lore()).build(),
                 (player1, event) -> {
                    MPUser user = mpUserManager.getMPUser(player.getUniqueId());
                    if(user != null)
                    {
                       user.setMpClass(mpClass);
                       mpClass.applyAttributes(player);
                       mpClass.giveItemsKit(player);
                    }
                 });
      }
   }
}
