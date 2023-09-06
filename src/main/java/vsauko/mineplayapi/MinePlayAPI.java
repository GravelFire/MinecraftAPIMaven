package vsauko.mineplayapi;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import vsauko.mineplayapi.api.BukkitAPI;
import vsauko.mineplayapi.api.actionitem.ActionItemListener;
import vsauko.mineplayapi.api.command.impl.CrashCommand;
import vsauko.mineplayapi.api.inventory.BaseInventoryListener;
import vsauko.mineplayapi.api.listener.PlayerListener;
import vsauko.mineplayapi.api.mpuser.classes.MPClassListener;
import vsauko.mineplayapi.api.protocollib.ArmorEquip;
import vsauko.mineplayapi.api.protocollib.PlayerPotionEffectEvents;
import vsauko.mineplayapi.api.protocollib.entity.listener.FakeEntityListener;
import vsauko.mineplayapi.api.protocollib.HealthHider;
import vsauko.mineplayapi.api.protocollib.team.ProtocolTeam;
import vsauko.mineplayapi.api.scoreboard.listener.BaseScoreboardListener;

@Getter
public final class MinePlayAPI extends JavaPlugin {

    @Override
    public void onEnable() {

        // events.
        getServer().getPluginManager().registerEvents(new BaseInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new BaseScoreboardListener(), this);
        getServer().getPluginManager().registerEvents(new ActionItemListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(ProtocolTeam.TEAM_LISTENER, this);
        getServer().getPluginManager().registerEvents(new FakeEntityListener(), this);
        getServer().getPluginManager().registerEvents(new MPClassListener(), this);


        ProtocolLibrary.getProtocolManager().addPacketListener(new HealthHider(this));
        ProtocolLibrary.getProtocolManager().addPacketListener(new ArmorEquip(this));
        ProtocolLibrary.getProtocolManager().addPacketListener(new PlayerPotionEffectEvents(this));
        ProtocolLibrary.getProtocolManager().addPacketListener(new FakeEntityListener());

        BukkitAPI.registerCommand(new CrashCommand());

        // Руннейбл на обновление инвентарей
        BukkitAPI.INVENTORY_MANAGER.startInventoryUpdateTask(this);

        for (World world : getServer().getWorlds()) {
            world.setGameRuleValue("announceAdvancements", "false");
        }
    }

    @Override
    public void onDisable() {
        BukkitAPI.MP_USER_MANAGER.getRedisManager().close();
        HandlerList.unregisterAll(this);
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

}
