package vsauko.mineplayapi.api.protocollib.entity;

import java.lang.reflect.Field;
import vsauko.mineplayapi.api.utility.bukkit.WrapperUtil;

public class EntityIdProvider {

  private static Field entityCountField;

  static {
    try {
      entityCountField = WrapperUtil.getNmsClass("Entity").getDeclaredField("entityCount");
      entityCountField.setAccessible(true);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
  }

  public int nextAndIncrement() {
    if (entityCountField == null) {
      return -1;
    }

    try {
      int nextId = this.getCurrentId() + 1;

      entityCountField.set(null, nextId);

      return nextId;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return -1;
  }

  public int getCurrentId() {
    if (entityCountField == null) {
      return -1;
    }

    try {
      return entityCountField.getInt(null);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return -1;
  }
}