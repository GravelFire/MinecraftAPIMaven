package vsauko.mineplayapi.api.customevents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class PlayerArmorEquipEvent extends BaseCustomEvent {

    private final Player player;
    private final ItemStack itemStack;
    private final String slot;
}