package vsauko.mineplayapi.api.protocollib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

public class HardcoreHealth extends PacketAdapter {

  public HardcoreHealth(Plugin plugin) {
    super(plugin, PacketType.Play.Server.LOGIN);
  }

  public void onPacketSending(PacketEvent event) {
    if (event.getPacketType().equals(PacketType.Play.Server.LOGIN))
      event.getPacket().getBooleans().write(0, Boolean.TRUE);
  }
}
