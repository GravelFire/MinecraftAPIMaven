package vsauko.mineplayapi.api.actionitem;

import java.util.function.BiConsumer;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import vsauko.mineplayapi.api.customevents.EntityDamageByPlayerEvent;

public final class ActionItemListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        handleItem(event.getPlayer(), event.getItemDrop().getItemStack(), (player, actionItem) -> {

            if (actionItem.getDropHandler() != null) {
                actionItem.getDropHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) return;
        handleItem((Player) event.getEntity(), event.getItem().getItemStack(), (player, actionItem) -> {

            if (actionItem.getPickupHandler() != null) {
                actionItem.getPickupHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onAttack(EntityDamageByPlayerEvent event) {

        handleMainHand(event.getDamager(), (player, actionItem) -> {

            if (actionItem.getAttackHandler() != null) {
                actionItem.getAttackHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.hasItem()) {
            return;
        }

        handleItem(event.getPlayer(), event.getItem(), (player, actionItem) -> {

            if (actionItem.getInteractHandler() != null) {
                actionItem.getInteractHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        handleMainHand(event.getPlayer(), (player, actionItem) -> {

            if (actionItem.getPlaceHandler() != null) {
                actionItem.getPlaceHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        handleMainHand(event.getPlayer(), (player, actionItem) -> {

            if (actionItem.getBreakHandler() != null) {
                actionItem.getBreakHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onWorldChanged(PlayerChangedWorldEvent event) {
        handleMainHand(event.getPlayer(), (player, actionItem) -> {

            if (actionItem.getWorldChangedHandler() != null) {
                actionItem.getWorldChangedHandler().accept(event);
            }
        });
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        handleItem((Player) event.getEntity(), event.getBow(), (player, actionItem) -> {

            if (actionItem.getShootBowHandler() != null) {
                actionItem.getShootBowHandler().accept(event);
            }
        });
    }

    private void handleItem(@NonNull Player player, @NonNull ItemStack itemStack,
                            @NonNull BiConsumer<Player, ActionItem> itemConsumer) {

        if (ActionItem.hasActionItem(itemStack)) {
            itemConsumer.accept(player, ActionItem.fromItem(itemStack));
        }
    }

    private void handleMainHand(@NonNull Player player, @NonNull BiConsumer<Player, ActionItem> itemConsumer) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if (mainHandItem == null) {
            return;
        }

        if (ActionItem.hasActionItem(mainHandItem)) {
            itemConsumer.accept(player, ActionItem.fromItem(mainHandItem));
        }
    }

}
