package vsauko.mineplayapi.api.inventory.handler.impl;

import lombok.NonNull;
import org.bukkit.event.inventory.InventoryClickEvent;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.inventory.handler.BaseInventoryHandler;
import vsauko.mineplayapi.api.utility.WeakObjectCache;

public interface BaseInventoryClickHandler extends BaseInventoryHandler {

    void onClick(@NonNull BaseInventory baseInventory, @NonNull InventoryClickEvent inventoryClickEvent);

    @Override
    default void handle(@NonNull BaseInventory baseInventory,
                        WeakObjectCache objectCache) {

        onClick(baseInventory, objectCache.getObject(InventoryClickEvent.class, "event"));
    }
}
