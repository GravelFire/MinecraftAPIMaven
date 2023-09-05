package vsauko.mineplayapi.api.holographic.manager;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.holographic.ProtocolHolographic;

import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProtocolHolographicManager {

    public static final ProtocolHolographicManager INSTANCE = new ProtocolHolographicManager();

    private final Multimap<Player, ProtocolHolographic> playerHolographics = HashMultimap.create();


    /**
     * Получить список голограмм, которые были показаны
     * для указанного игрока
     *
     * @param player - игрок
     */
    public Collection<ProtocolHolographic> getProtocolHolographics(Player player) {
        return playerHolographics.get(player);
    }

    /**
     * Кешировать голограмму для указанного игрока
     *
     * @param player - игрок
     * @param protocolHolographic - голограмма
     */
    public void addProtocolHolographic(Player player, ProtocolHolographic protocolHolographic) {
        playerHolographics.put(player, protocolHolographic);
    }

    /**
     * Удалить кеш голограммы для указанного игрока
     *
     * @param player - игрок
     * @param protocolHolographic - голограмма
     */
    public void removeProtocolHolographic(Player player, ProtocolHolographic protocolHolographic) {
        playerHolographics.remove(player, protocolHolographic);
    }

}
