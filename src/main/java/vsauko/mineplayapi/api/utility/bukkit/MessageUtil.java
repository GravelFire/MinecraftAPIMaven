package vsauko.mineplayapi.api.utility.bukkit;

import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

@UtilityClass
public class MessageUtil {

  public static String translate(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  public static List<String> translate(List<String> text) {
    return text.stream().map(MessageUtil::translate).collect(Collectors.toList());
  }
}