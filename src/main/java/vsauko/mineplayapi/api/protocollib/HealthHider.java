package vsauko.mineplayapi.api.protocollib;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import java.util.List;
import lombok.SneakyThrows;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.plugin.Plugin;

public class HealthHider extends PacketAdapter {


  public HealthHider(Plugin plugin) {
    super(plugin, Server.ENTITY_METADATA);
  }

  @SneakyThrows
  public void onPacketSending(PacketEvent event) {
    PacketContainer packet = event.getPacket();
    Player player = event.getPlayer();

    if (packet.getWatchableCollectionModifier().size() < 1) return;

    List<WrappedWatchableObject> list = packet.getWatchableCollectionModifier().read(0);
    WrappedDataWatcher watcher = new WrappedDataWatcher(list);
    Entity entity = packet.getEntityModifier(player.getWorld()).read(0);

    if (entity == null) return; // Если entity это нпс
    if (player == entity) return; // Чтобы у самого себя не менялись цифры
    if (!(entity instanceof LivingEntity)) return; // На предметы или EXP не действовало
    if (entity instanceof EnderDragon || entity instanceof Wither) return; // Не меняло дракону и визеру
    if (entity.getPassengers().contains(player)) return; // Не меняло сущностям на которых сидит игрок
    if (!watcher.hasIndex(7)) return;

    Float hp = watcher.getFloat(7);
    watcher.setObject(7, hp <= 0.0F ? hp : Float.NaN, false); // Если у сущности нету хп, то не меняем
    packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects());
  }
}