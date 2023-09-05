package vsauko.mineplayapi.api.holographic.updater;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import vsauko.mineplayapi.MinePlayAPI;
import vsauko.mineplayapi.api.holographic.ProtocolHolographic;
import vsauko.mineplayapi.api.holographic.ProtocolHolographicUpdater;

import java.util.function.Consumer;

@Getter
@Setter
@RequiredArgsConstructor
public abstract class SimpleHolographicUpdater extends BukkitRunnable
        implements ProtocolHolographicUpdater, Consumer<ProtocolHolographic> {

    private final ProtocolHolographic holographic;

    private boolean enable;
    private boolean cancelled;


    @Override
    public void run() {
        if (holographic.getViewers().isEmpty()) {
            return;
        }

        accept(holographic);
    }

    @Override
    public void startUpdater(long periodTick) {
        this.cancelled = !cancelled;

        MinePlayAPI plugin = MinePlayAPI.getPlugin(MinePlayAPI.class);

        if (isCancelled()) {
            runTaskTimerAsynchronously(plugin, 0, periodTick);
        }
    }

    @Override
    public void cancelUpdater() {
        this.cancelled = !cancelled;

        if (!isCancelled()) {
            cancel();
        }
    }

}
