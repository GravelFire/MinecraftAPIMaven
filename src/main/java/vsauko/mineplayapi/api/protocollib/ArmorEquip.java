package vsauko.mineplayapi.api.protocollib;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import vsauko.mineplayapi.api.customevents.PlayerArmorEquipEvent;
import vsauko.mineplayapi.api.protocollib.packet.entity.WrapperPlayServerEntityEquipment;

public class ArmorEquip extends PacketAdapter {

  public ArmorEquip(Plugin plugin) {
    super(plugin, Server.ENTITY_EQUIPMENT);
  }

  public void onPacketSending(PacketEvent event) {
    WrapperPlayServerEntityEquipment packet = new WrapperPlayServerEntityEquipment(event.getPacket());

    PlayerArmorEquipEvent equipEvent = new PlayerArmorEquipEvent(event.getPlayer(), packet.getItem(), packet.getSlot().name());
    Bukkit.getServer().getPluginManager().callEvent(equipEvent);
  }
}
