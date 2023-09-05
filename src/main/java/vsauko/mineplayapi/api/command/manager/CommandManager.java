package vsauko.mineplayapi.api.command.manager;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import vsauko.mineplayapi.MinePlayAPI;
import vsauko.mineplayapi.api.command.BaseCommand;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandManager {

    public static final CommandManager INSTANCE = new CommandManager();

    @Getter
    private final Collection<BaseCommand<?>> commandCollection = new ArrayList<>();
    public static CommandMap COMMAND_MAP;


    /**
     * Регистрация комманд при помощи org.bukkit.command.CommandMap
     *
     * @param baseCommand - команда
     * @param command     - главная команда
     * @param aliases     - ее алиасы
     */
    public void registerCommand(BaseCommand<?> baseCommand,
                                String command, String... aliases) {

        registerCommand(MinePlayAPI.getPlugin(MinePlayAPI.class), baseCommand, command, aliases);
    }

    /**
     * Регистрация комманд при помощи org.bukkit.command.CommandMap
     *
     * @param plugin      - плагин, от имени которого регистрируется команда
     * @param baseCommand - команда
     * @param command     - главная команда
     * @param aliases     - ее алиасы
     */
    public void registerCommand(Plugin plugin, BaseCommand<?> baseCommand,
                                String command, String... aliases) {

        commandCollection.add(baseCommand);

        baseCommand.setLabel(command);
        baseCommand.setName(command);
        baseCommand.setAliases(Lists.newArrayList(aliases));

        try {
            if (COMMAND_MAP == null) {
                String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

                Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                Object craftServerObject = craftServerClass.cast(Bukkit.getServer());
                Field commandMapField = craftServerClass.getDeclaredField("commandMap");

                commandMapField.setAccessible(true);

                COMMAND_MAP = (SimpleCommandMap) commandMapField.get(craftServerObject);
            }

            COMMAND_MAP.register(plugin.getName(), baseCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
