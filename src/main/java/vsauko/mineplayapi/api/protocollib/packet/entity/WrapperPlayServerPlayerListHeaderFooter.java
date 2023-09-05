package vsauko.mineplayapi.api.protocollib.packet.entity;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import vsauko.mineplayapi.api.protocollib.packet.AbstractPacket;

public class WrapperPlayServerPlayerListHeaderFooter extends AbstractPacket {
  public static final PacketType TYPE = Server.PLAYER_LIST_HEADER_FOOTER;

  public WrapperPlayServerPlayerListHeaderFooter() {
    super(new PacketContainer(TYPE), TYPE);
    handle.getModifier().writeDefaults();
  }

  public WrapperPlayServerPlayerListHeaderFooter(PacketContainer packet) {
    super(packet, TYPE);
  }

  public WrappedChatComponent getHeader() {
    return handle.getChatComponents().read(0);
  }

  public void setHeader(WrappedChatComponent value) {
    handle.getChatComponents().write(0, value);
  }

  public WrappedChatComponent getFooter() {
    return handle.getChatComponents().read(1);
  }

  public void setFooter(WrappedChatComponent value) {
    handle.getChatComponents().write(1, value);
  }

}
