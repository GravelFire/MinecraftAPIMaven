package vsauko.mineplayapi.api.protocollib.entity;

import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface FakeEntityClickable {

    void setClickAction(@NonNull Consumer<Player> playerConsumer);
}
