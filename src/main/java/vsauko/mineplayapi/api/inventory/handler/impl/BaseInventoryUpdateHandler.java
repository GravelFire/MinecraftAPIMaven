package vsauko.mineplayapi.api.inventory.handler.impl;

import lombok.NonNull;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.inventory.handler.BaseInventoryHandler;
import vsauko.mineplayapi.api.utility.WeakObjectCache;

public interface BaseInventoryUpdateHandler extends BaseInventoryHandler {

    void onUpdate(@NonNull BaseInventory baseInventory, @NonNull Player player);

    @Override
    default void handle(@NonNull BaseInventory baseInventory,
                        WeakObjectCache objectCache) {

        onUpdate(baseInventory, objectCache.getObject(Player.class, "player"));
    }
}
