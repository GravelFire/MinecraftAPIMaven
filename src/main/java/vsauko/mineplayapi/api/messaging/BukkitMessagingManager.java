package vsauko.mineplayapi.api.messaging;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public final class BukkitMessagingManager {

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    public static class OutgoingRequest {

        @NonNull String channel;
        byte @NonNull [] bytes;

        /**
         * Отправление сообщения на сторону сервера
         * @param plugin - плагин, к которому пренадлежит сообщение
         */
        public void sendData(@NonNull Plugin plugin) {
            if (!plugin.getServer().getMessenger().getOutgoingChannels().contains(channel)) {
                plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
            }

            plugin.getServer().sendPluginMessage(plugin, channel, bytes);
        }

        /**
         * Отправление сообщения на сторону игрока
         * @param plugin - плагин, к которому пренадлежит сообщение
         * @param player - игрок
         */
        public void sendData(@NonNull Plugin plugin, @NonNull Player player) {
            if (!plugin.getServer().getMessenger().getOutgoingChannels().contains(channel)) {
                plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
            }

            player.sendPluginMessage(plugin, channel, bytes);
        }
    }


    /**
     * Создание нового Output сообщения
     * для отправления его на подключенный
     * BungeeCord сервер на обработку.
     *
     * @param channel        - канал
     * @param outputConsumer - обработчик данных на вход
     */
    public OutgoingRequest newOutputRequest(@NonNull String channel,
                                            @NonNull Consumer<ByteArrayDataOutput> outputConsumer) {

        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        outputConsumer.accept(output);

        return new OutgoingRequest(channel, output.toByteArray());
    }

    /**
     * Регистрация Input слушателя сообщений
     * с BungeeCord сервера
     *
     * @param plugin          - плагин, на который регистрируем слушатель
     * @param incomingAdapter - адаптер слушателя
     */
    public void registerIncomingListener(@NonNull Plugin plugin, @NonNull BukkitMessagingIncomingAdapter incomingAdapter) {
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, incomingAdapter.channel, incomingAdapter);
    }

}
