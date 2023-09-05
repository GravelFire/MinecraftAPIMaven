package vsauko.mineplayapi.api.protocollib;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import vsauko.mineplayapi.api.customevents.PlayerPotionEffectEndEvent;
import vsauko.mineplayapi.api.customevents.PlayerPotionEffectStartEvent;
import vsauko.mineplayapi.api.protocollib.packet.entity.WrapperPlayServerEntityEffect;
import vsauko.mineplayapi.api.protocollib.packet.entity.WrapperPlayServerRemoveEntityEffect;

public class PlayerPotionEffectEvents extends PacketAdapter {

  public PlayerPotionEffectEvents(Plugin plugin) {
    super(plugin, Server.ENTITY_EFFECT, Server.REMOVE_ENTITY_EFFECT);
  }

  @Override
  public void onPacketSending(PacketEvent event) {
    Entity entity = event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0);
    if (!(entity instanceof Player)) return;
    Player player = (Player) entity;
    if (event.getPacketType() == Server.ENTITY_EFFECT) {
      WrapperPlayServerEntityEffect wrapperPlayServerEntityEffect = new WrapperPlayServerEntityEffect(event.getPacket());
      PlayerPotionEffectStartEvent potionEffectStartEvent = new PlayerPotionEffectStartEvent(
          player,
          wrapperPlayServerEntityEffect.getEffectByID(),
          wrapperPlayServerEntityEffect.getDuration(),
          wrapperPlayServerEntityEffect.getAmplifier());
      Bukkit.getPluginManager().callEvent(potionEffectStartEvent);
    }
    if (event.getPacketType() == Server.REMOVE_ENTITY_EFFECT) {
      WrapperPlayServerRemoveEntityEffect wrapperPlayServerRemoveEntityEffect = new WrapperPlayServerRemoveEntityEffect(event.getPacket());
      PlayerPotionEffectEndEvent potionEffectEndEvent = new PlayerPotionEffectEndEvent(
          player,
          wrapperPlayServerRemoveEntityEffect.getEffect());
      Bukkit.getPluginManager().callEvent(potionEffectEndEvent);
    }
  }
}
