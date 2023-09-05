package vsauko.mineplayapi.api.utility.bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


/**
 * Спизжено с интернето, лень было переписывать, ведь и так работает >_<
 */
public enum ArmorType{
	HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);

	private final int slot;

	ArmorType(int slot){
		this.slot = slot;
	}

	public static ArmorType matchType(final ItemStack itemStack) {
		if (itemStack == null || itemStack.getType().equals(Material.AIR)) return null;
		String type = itemStack.getType().name();

		if (type.equals("ELYTRA")) return CHESTPLATE;
		String[] split = type.split("_");
		if (split.length == 1) return null;
		switch (split[1]) {
			case "HELMET":
			case "SKULL":
			case "HEAD": {
				return HELMET;
			}
			case "CHESTPLATE": return CHESTPLATE;
			case "BOOTS": return BOOTS;
		}
		return null;
	}

	public int getSlot(){
		return slot;
	}
}