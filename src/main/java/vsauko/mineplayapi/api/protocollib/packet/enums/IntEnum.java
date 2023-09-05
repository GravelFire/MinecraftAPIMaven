package vsauko.mineplayapi.api.protocollib.packet.enums;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Represents a traditional int field enum.
 *
 * @author Kristian
 */
public class IntEnum {

  protected BiMap<Integer, String> members = HashBiMap.create();

  public IntEnum() {
    registerAll();
  }

  protected void registerAll() {
    try {
      for (Field entry : this.getClass().getFields()) {
        if (entry.getType().equals(int.class)) {
          registerMember(entry.getInt(this), entry.getName());
        }
      }

    } catch (IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  protected void registerMember(int id, String name) {
    members.put(id, name);
  }

  public boolean hasMember(int id) {
    return members.containsKey(id);
  }

  public Integer valueOf(String name) {
    return members.inverse().get(name);
  }

  public String getDeclaredName(Integer id) {
    return members.get(id);
  }

  public Set<Integer> values() {
    return new HashSet<>(members.keySet());
  }
}
