package vsauko.mineplayapi.api.actionitem;

import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import vsauko.mineplayapi.api.customevents.EntityDamageByPlayerEvent;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
public final class ActionItem {

    public static final Map<ItemStack, ActionItem> ACTION_ITEM_MAP
            = new HashMap<>();

    public static boolean hasActionItem(@NonNull ItemStack itemStack) {
        return ACTION_ITEM_MAP.containsKey(itemStack);
    }

    public static ActionItem fromItem(@NonNull ItemStack itemStack) {
        if (!ActionItem.hasActionItem(itemStack))
            return null;

        return ACTION_ITEM_MAP.get(itemStack);
    }

    public static ActionItem create(@NonNull ItemStack itemStack, Player... playersForGive) {
        for (Player player : playersForGive) {
            player.getInventory().addItem(itemStack);
        }

        ActionItem actionItem = ActionItem.hasActionItem(itemStack) ? ActionItem.fromItem(itemStack) : new ActionItem(itemStack);

        if (!ActionItem.hasActionItem(itemStack)) {
            ACTION_ITEM_MAP.put(itemStack, actionItem);
        }

        return actionItem;
    }

    public static ActionItem create(@NonNull ItemStack itemStack, int slot, Player... playersForGive) {
        for (Player player : playersForGive) {
            player.getInventory().setItem(slot, itemStack);
        }

        ActionItem actionItem = ActionItem.hasActionItem(itemStack) ? ActionItem.fromItem(itemStack) : new ActionItem(itemStack);

        if (!ActionItem.hasActionItem(itemStack)) {
            ACTION_ITEM_MAP.put(itemStack, actionItem);
        }

        return actionItem;
    }


    private final ItemStack itemStack;


    private Consumer<PlayerDropItemEvent> dropHandler;

    private Consumer<EntityPickupItemEvent> pickupHandler;

    private Consumer<EntityDamageByPlayerEvent> attackHandler;

    private Consumer<PlayerInteractEvent> interactHandler;

    private Consumer<BlockPlaceEvent> placeHandler;

    private Consumer<BlockBreakEvent> breakHandler;

    private Consumer<EntityShootBowEvent> shootBowHandler;

    private Consumer<PlayerChangedWorldEvent> worldChangedHandler;


    public ActionItem setDropHandler(Consumer<PlayerDropItemEvent> dropHandler) {
        this.dropHandler = dropHandler;
        return this;
    }

    public ActionItem setPickupHandler(Consumer<EntityPickupItemEvent> pickupHandler) {
        this.pickupHandler = pickupHandler;
        return this;
    }

    public ActionItem setAttackHandler(Consumer<EntityDamageByPlayerEvent> attackHandler) {
        this.attackHandler = attackHandler;
        return this;
    }

    public ActionItem setInteractHandler(Consumer<PlayerInteractEvent> interactHandler) {
        this.interactHandler = interactHandler;
        return this;
    }

    public ActionItem setPlaceHandler(Consumer<BlockPlaceEvent> placeHandler) {
        this.placeHandler = placeHandler;
        return this;
    }

    public ActionItem setBreakHandler(Consumer<BlockBreakEvent> breakHandler) {
        this.breakHandler = breakHandler;
        return this;
    }

    public ActionItem setShootBowHandler(Consumer<EntityShootBowEvent> shootBowHandler) {
        this.shootBowHandler = shootBowHandler;
        return this;
    }

    public ActionItem setWorldChangedHandler(Consumer<PlayerChangedWorldEvent> worldChangedHandler) {
        this.worldChangedHandler = worldChangedHandler;
        return this;
    }
}
