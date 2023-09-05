package vsauko.mineplayapi.api.messaging;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

@Getter(AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public abstract class BukkitMessagingIncomingAdapter
        implements PluginMessageListener {

    @NonNull String channel;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (channel.equals(this.channel))
            onReceive(player, ByteStreams.newDataInput(bytes));
    }

    /**
     * Основной процесс обработки сообщения
     *
     * @param player - игрок, от которого пришло сообщение (если от его имени оно было отправлено)
     * @param input  - обработчик байтов сообщения
     */
    protected abstract void onReceive(Player player, @NonNull ByteArrayDataInput input);

}
