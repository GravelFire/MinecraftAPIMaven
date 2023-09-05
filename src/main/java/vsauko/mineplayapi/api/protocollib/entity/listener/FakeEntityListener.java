package vsauko.mineplayapi.api.protocollib.entity.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import vsauko.mineplayapi.MinePlayAPI;
import vsauko.mineplayapi.api.protocollib.entity.FakeBaseEntity;
import vsauko.mineplayapi.api.protocollib.entity.FakeEntityLiving;
import vsauko.mineplayapi.api.protocollib.entity.FakeEntityRegistry;
import vsauko.mineplayapi.api.protocollib.entity.FakeEntityScope;
import vsauko.mineplayapi.api.utility.cooldown.PlayerCooldownUtil;

public class FakeEntityListener extends PacketAdapter implements Listener {


    private static final long ENTITY_INTERACT_COOLDOWN = 250;

    public FakeEntityListener() {
        super(MinePlayAPI.getPlugin(MinePlayAPI.class),
                PacketType.Play.Client.USE_ENTITY, PacketType.Play.Server.MAP_CHUNK, PacketType.Play.Server.UNLOAD_CHUNK);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType type = event.getPacketType();
        Player player = event.getPlayer();

        StructureModifier<Integer> integers = event.getPacket().getIntegers();

        int x = integers.read(0);
        int z = integers.read(1);

        if (type == PacketType.Play.Server.UNLOAD_CHUNK) {
            onChunkUnload(player, x, z);
            return;
        }

        onChunkLoad(player, x, z);
    }

    private void onChunkLoad(Player player, int x, int z) {
        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {

            for (FakeBaseEntity entity : FakeEntityRegistry.INSTANCE.getReceivableEntities(player)) {
                Chunk chunk = entity.getLocation().getChunk();

                if (chunk.getX() == x && chunk.getZ() == z) {
                    if (entity.hasViewer(player)) {
                        continue;
                    }

                    entity.addViewers(player);
                }
            }

        }, 10L);
    }

    private void onChunkUnload(Player player, int x, int z) {
        for (FakeBaseEntity entity : FakeEntityRegistry.INSTANCE.getReceivableEntities(player)) {
            Chunk chunk = entity.getLocation().getChunk();

            if (chunk.getX() == x && chunk.getZ() == z) {
                if (!entity.hasViewer(player)) {
                    continue;
                }

                entity.removeViewers(player);
            }
        }
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        if (PlayerCooldownUtil.hasCooldown("fake_entity_interact", player)) {
            return;
        }

        FakeBaseEntity fakeEntity = FakeEntityRegistry.INSTANCE.getEntityById(event.getPacket().getIntegers().read(0));
        if (!(fakeEntity instanceof FakeEntityLiving)) {
            return;
        }

        EnumWrappers.EntityUseAction entityUseAction = event.getPacket().getEntityUseActions().read(0);
        switch (entityUseAction) {
            case ATTACK: {
                Consumer<Player> attackAction = fakeEntity.getAttackAction();

                if (attackAction != null) {
                    Bukkit.getScheduler().runTask(getPlugin(), () -> attackAction.accept(player));
                }

            }
            case INTERACT_AT: {
                Consumer<Player> clickAction = fakeEntity.getClickAction();

                if (clickAction != null) {
                    Bukkit.getScheduler().runTask(getPlugin(), () -> clickAction.accept(player));
                }

            }
        }

        PlayerCooldownUtil.putCooldown("fake_entity_interact", player, ENTITY_INTERACT_COOLDOWN);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        for (FakeBaseEntity fakeEntity : FakeEntityRegistry.INSTANCE.getReceivableEntities(player)) {
            if (fakeEntity == null) {
                continue;
            }

            fakeEntity.removeReceivers(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> {
            int i = 1;
            for (FakeBaseEntity publicEntity : FakeEntityRegistry.INSTANCE.getEntitiesByScope(FakeEntityScope.PUBLIC)) {
                if (publicEntity.getLocation().getWorld().equals(player.getWorld())) continue;
                if (publicEntity.isLazyLoad()) {
                    i= i + 5;
                    getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> publicEntity.addReceivers(player), 10L + i);
                } else publicEntity.addReceivers(player);
            }
        }, 20);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {

            for (FakeBaseEntity fakeBaseEntity : FakeEntityRegistry.INSTANCE.getReceivableEntities(player)) {

                boolean equalsWorld = fakeBaseEntity.getLocation().getWorld().equals(player.getWorld());
                boolean hasViewer = fakeBaseEntity.hasViewer(player);

                if (!hasViewer && equalsWorld) {
                    fakeBaseEntity.addViewers(player);
                }

                if (hasViewer && !equalsWorld) {
                    fakeBaseEntity.removeViewers(player);
                }
            }
        }, 10);
    }
}