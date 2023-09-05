package vsauko.mineplayapi.api.inventory.impl;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.inventory.BaseInventoryItem;
import vsauko.mineplayapi.api.inventory.BaseInventoryMarkup;
import vsauko.mineplayapi.api.inventory.handler.impl.BaseInventoryClickHandler;
import vsauko.mineplayapi.api.inventory.handler.impl.BaseInventoryDisplayableHandler;
import vsauko.mineplayapi.api.inventory.handler.impl.BaseInventoryUpdateHandler;
import vsauko.mineplayapi.api.inventory.item.BaseInventoryClickItem;
import vsauko.mineplayapi.api.inventory.item.BaseInventorySelectItem;
import vsauko.mineplayapi.api.inventory.item.BaseInventoryStackItem;
import vsauko.mineplayapi.api.inventory.update.BaseInventoryUpdateTask;

@Getter
public abstract class BaseSimpleInventory implements BaseInventory {

    protected final int inventoryRows;
    protected final String inventoryTitle;

    protected BaseInventoryInfo inventoryInfo;
    protected Inventory inventory;

    protected BaseInventoryMarkup inventoryMarkup;

    protected final BaseInventorySettings inventorySettings = new BaseInventorySettings();


    public BaseSimpleInventory(int inventoryRows, @NonNull String inventoryTitle) {
        this.inventoryRows = inventoryRows;
        this.inventoryTitle = inventoryTitle;

        this.inventoryInfo = new BaseInventoryInfo(this, inventoryTitle, inventoryRows * 9, inventoryRows);

        this.inventory = Bukkit.createInventory(null, inventoryInfo.getInventorySize(), inventoryTitle);
    }

    @Override
    public void openInventory(@NonNull Player player) {
        closeInventory(player);
        drawInventory(player);

        for (BaseInventoryItem baseInventoryItem : inventoryInfo.getInventoryItemMap().valueCollection()) {
            inventory.setItem(baseInventoryItem.getSlot(), baseInventoryItem.getItemStack());
        }

        BukkitAPI.INVENTORY_MANAGER
                .createInventory(player, this);

        player.openInventory(inventory);
    }

    @Override
    public void openInventory(@NonNull Player player, @NonNull BaseInventoryDisplayableHandler inventoryDisplayableHandler) {
        addHandler(BaseInventoryDisplayableHandler.class, inventoryDisplayableHandler);

        openInventory(player);
    }

    @Override
    public void clearInventory() {
        inventoryInfo.getInventoryItemMap().clear();
        inventory.clear();
    }

    @Override
    public void updateInventory(@NonNull Player player) {
        clearInventory();
        drawInventory(player);

        for (BaseInventoryItem baseInventoryItem : inventoryInfo.getInventoryItemMap().valueCollection()) {
            inventory.setItem(baseInventoryItem.getSlot(), baseInventoryItem.getItemStack());
        }
    }

    @Override
    public void updateInventory(@NonNull Player player, @NonNull BaseInventoryUpdateHandler inventoryUpdateHandler) {
        inventoryUpdateHandler.onUpdate(this, player);
        updateInventory(player);
    }

    @Override
    public void enableAutoUpdate(@NonNull Player player, BaseInventoryUpdateHandler inventoryUpdateHandler, long secondDelay) {
        BukkitAPI.INVENTORY_MANAGER.addInventoryUpdateTask(this, new BaseInventoryUpdateTask(this, secondDelay, () -> {

            if (inventoryUpdateHandler != null)
                updateInventory(player, inventoryUpdateHandler);
            else
                updateInventory(player);
        }));
    }

    @Override
    public void closeInventory(@NonNull Player player) {
        if (player.getOpenInventory() == null) {
            return;
        }

        player.closeInventory();

        BukkitAPI.INVENTORY_MANAGER.removeInventory(player, this);
        BukkitAPI.INVENTORY_MANAGER.removeInventoryUpdateTask(this);
    }

    @Override
    public void addItem(@NonNull BaseInventoryItem baseInventoryItem) {
        if (inventoryMarkup != null && !inventoryMarkup.hasInventorySlot(baseInventoryItem.getSlot())) {
            return;
        }

        baseInventoryItem.onDraw(this);

        inventoryInfo.addItem(baseInventoryItem.getSlot() - 1, baseInventoryItem);
    }

    @Override
    public void setItemMarkup(@NonNull BaseInventoryMarkup baseInventoryMarkup) {
        this.inventoryMarkup = baseInventoryMarkup;
    }

    public void addItem(int itemSlot, @NonNull ItemStack itemStack) {
        addItem(new BaseInventoryStackItem(itemSlot - 1, itemStack));
    }

    public void addItem(int itemSlot, @NonNull ItemStack itemStack, @NonNull BaseInventoryClickHandler inventoryClickHandler) {
        addItem(new BaseInventoryClickItem(itemSlot - 1, itemStack, inventoryClickHandler));
    }

    public void addItemSelect(int itemSlot, @NonNull ItemStack itemStack, @NonNull BaseInventoryClickHandler inventoryClickHandler, boolean isEnchanting) {
        addItem(new BaseInventorySelectItem(itemSlot - 1, itemStack, inventoryClickHandler, isEnchanting, false));
    }

    public void addItemSelect(int itemSlot, @NonNull ItemStack itemStack, @NonNull BaseInventoryClickHandler inventoryClickHandler) {
        addItemSelect(itemSlot, itemStack, inventoryClickHandler, true);
    }

    @Override
    public abstract void drawInventory(@NonNull Player player);

}
