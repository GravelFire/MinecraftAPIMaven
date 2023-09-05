package vsauko.mineplayapi.api.inventory.update;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vsauko.mineplayapi.api.inventory.BaseInventory;

@RequiredArgsConstructor
@Getter
public class BaseInventoryUpdateTask {

    private final BaseInventory baseInventory;

    private final long updateTaskDelay;
    private final Runnable inventoryUpdateTask;
}
