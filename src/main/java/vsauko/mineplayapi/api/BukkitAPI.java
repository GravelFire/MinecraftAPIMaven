package vsauko.mineplayapi.api;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;
import vsauko.mineplayapi.api.command.BaseCommand;
import vsauko.mineplayapi.api.command.manager.CommandManager;
import vsauko.mineplayapi.MinePlayAPI;
import vsauko.mineplayapi.api.holographic.ProtocolHolographic;
import vsauko.mineplayapi.api.holographic.impl.SimpleHolographic;
import vsauko.mineplayapi.api.holographic.manager.ProtocolHolographicManager;
import vsauko.mineplayapi.api.inventory.BaseInventory;
import vsauko.mineplayapi.api.inventory.BaseInventoryManager;
import vsauko.mineplayapi.api.inventory.impl.BasePaginatedInventory;
import vsauko.mineplayapi.api.inventory.impl.BaseSimpleInventory;
import vsauko.mineplayapi.api.messaging.BukkitMessagingManager;
import vsauko.mineplayapi.api.scoreboard.BaseScoreboardBuilder;
import vsauko.mineplayapi.api.utility.bukkit.ItemUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import vsauko.mineplayapi.api.mpuser.MPUserManager;

public interface BukkitAPI {

    CommandManager COMMAND_MANAGER                  = (CommandManager.INSTANCE);
    ProtocolHolographicManager HOLOGRAPHIC_MANAGER  = (ProtocolHolographicManager.INSTANCE);
    BaseInventoryManager INVENTORY_MANAGER          = new BaseInventoryManager();
    BukkitMessagingManager MESSAGING_MANAGER        = new BukkitMessagingManager();
    MPUserManager MP_USER_MANAGER                   = (MPUserManager.INSTANCE);

    Map<String, Integer> SERVERS_ONLINE_MAP         = new HashMap<>();


    /**
     * Получить онлайн сервера, имя которого
     * указано в аргументе
     *
     * @param serverName - имя сервера
     */
    static int getServerOnline(@NonNull String serverName) {
        return BukkitAPI.SERVERS_ONLINE_MAP.getOrDefault(serverName.toLowerCase(), -1);
    }

    /**
     * Получить общую сумму онлайна всех
     * подключенных серверов по Bungee
     *
     * @return - Общий онлайн Bungee
     */
    static int getGlobalOnline() {
        return getServerOnline("GLOBAL");
    }


    /**
     * Создать обыкновенную голограмму
     *
     * @param location - начальная локация голограммы
     */
    static ProtocolHolographic createSimpleHolographic(@NonNull Location location) {
        return new SimpleHolographic(location);
    }

    /**
     * Создание {@link ItemStack} по Builder паттерну
     *
     * @param material - начальный тип предмета
     */
    static ItemUtil.ItemBuilder newItemBuilder(@NonNull Material material) {
        return ItemUtil.newBuilder(material);
    }

    /**
     * Создание {@link ItemStack} по Builder паттерну
     *
     * @param materialData - начальная дата предмета
     */
    static ItemUtil.ItemBuilder newItemBuilder(@NonNull MaterialData materialData) {
        return ItemUtil.newBuilder(materialData);
    }

    /**
     * Создание {@link ItemStack} по Builder паттерну
     *
     * @param itemStack - готовый {@link ItemStack} на клонирование и переработку
     */
    static ItemUtil.ItemBuilder newItemBuilder(@NonNull ItemStack itemStack) {
        return ItemUtil.newBuilder(itemStack);
    }


    /**
     * Создать обыкновенный инвентарь без абстракции
     *
     * @param inventoryRows     - количество строк со слотами
     * @param inventoryTitle    - название инвентаря
     * @param inventoryConsumer - обработчик отрисовки предметов
     */
    static BaseInventory createSimpleInventory(int inventoryRows, @NonNull String inventoryTitle,
                                                @NonNull BiConsumer<Player, BaseInventory> inventoryConsumer) {

        return new BaseSimpleInventory(inventoryRows, inventoryTitle) {

            @Override
            public void drawInventory(@NonNull Player player) {
                inventoryConsumer.accept(player, this);
            }
        };
    }

    /**
     * Создать страничный инвентарь без абстракции
     *
     * @param inventoryRows     - количество строк со слотами
     * @param inventoryTitle    - название инвентаря
     * @param inventoryConsumer - обработчик отрисовки предметов
     */
    static BasePaginatedInventory createPaginatedInventory(int inventoryRows, @NonNull String inventoryTitle,
                                                            @NonNull BiConsumer<Player, BasePaginatedInventory> inventoryConsumer) {

        return new BasePaginatedInventory(inventoryRows, inventoryTitle) {

            @Override
            public void drawInventory(@NonNull Player player) {
                inventoryConsumer.accept(player, this);
            }
        };
    }


    /**
     * Зарегистрировать наследник {@link BaseCommand}
     * как bukkit команду на {@link MinePlayAPI}
     *
     * @param baseCommand - команда
     */
    static void registerCommand(@NonNull BaseCommand<?> baseCommand) {
        COMMAND_MANAGER.registerCommand(baseCommand, baseCommand.getName(), baseCommand.getAliases().toArray(new String[0]));
    }

    /**
     * Зарегистрировать наследник {@link BaseCommand}
     * как bukkit команду на {@link MinePlayAPI}
     *
     * @param baseCommand    - команда
     * @param commandName    - основная команда
     * @param commandAliases - дополнительные команды, обрабатывающие тот же класс
     */
    static void registerCommand(@NonNull BaseCommand<?> baseCommand, @NonNull String commandName, @NonNull String... commandAliases) {
        COMMAND_MANAGER.registerCommand(baseCommand, commandName, commandAliases);
    }

    /**
     * Зарегистрировать наследник {@link BaseCommand}
     * как bukkit команду
     *
     * @param plugin      - плагин, на который регистрировать команду
     * @param baseCommand - команда
     */
    static void registerCommand(@NonNull Plugin plugin, @NonNull BaseCommand<?> baseCommand) {
        COMMAND_MANAGER.registerCommand(plugin, baseCommand, baseCommand.getName(), baseCommand.getAliases().toArray(new String[0]));
    }

    /**
     * Зарегистрировать наследник {@link BaseCommand}
     * как bukkit команду
     *
     * @param plugin         - плагин, на который регистрировать команду
     * @param baseCommand    - команда
     * @param commandName    - основная команда
     * @param commandAliases - дополнительные команды, обрабатывающие тот же класс (алиасы)
     */
    static void registerCommand(@NonNull Plugin plugin, @NonNull BaseCommand<?> baseCommand, @NonNull String commandName, @NonNull String... commandAliases) {
        COMMAND_MANAGER.registerCommand(plugin, baseCommand, commandName, commandAliases);
    }


    /**
     * Создание {@link vsauko.mineplayapi.api.scoreboard.BaseScoreboard} по Builder паттерну
     * с рандомным названием objective
     */
    static BaseScoreboardBuilder newScoreboardBuilder() {
        return BaseScoreboardBuilder.newScoreboardBuilder();
    }

    /**
     * Создание {@link vsauko.mineplayapi.api.scoreboard.BaseScoreboard} по Builder паттерну
     *
     * @param objectiveName - название scoreboard objective
     */
    static BaseScoreboardBuilder newScoreboardBuilder(@NonNull String objectiveName) {
        return BaseScoreboardBuilder.newScoreboardBuilder(objectiveName);
    }

}
