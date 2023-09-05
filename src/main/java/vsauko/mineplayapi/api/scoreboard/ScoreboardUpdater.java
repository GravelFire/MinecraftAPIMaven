package vsauko.mineplayapi.api.scoreboard;

import lombok.NonNull;
import org.bukkit.entity.Player;

public interface ScoreboardUpdater {

    void onUpdate(@NonNull BaseScoreboard baseScoreboard, @NonNull Player player);
}
