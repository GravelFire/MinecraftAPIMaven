package vsauko.mineplayapi.api.inventory.handler.impl;

import lombok.NonNull;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.inventory.handler.BaseInventoryHandler;
import vsauko.mineplayapi.api.utility.WeakObjectCache;

public interface BaseInventoryDisplayableHandler extends BaseInventoryHandler {

    void onOpen(@NonNull Player player);
    void onClose(@NonNull Player player);

    @Override
    default void handle(@NonNull BaseInventory baseInventory,
                        WeakObjectCache objectCache) {

        Player player = objectCache.getObject(Player.class, "player");
        boolean isOpen = objectCache.getObject(boolean.class, "isOpen");

        if (isOpen) {
            onOpen(player);
        } else {
            onClose(player);
        }
    }
}
