package vsauko.mineplayapi.api.inventory.handler;

import lombok.NonNull;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.utility.WeakObjectCache;

public interface BaseInventoryHandler {

    void handle(@NonNull BaseInventory baseInventory, WeakObjectCache objectCache);
}
