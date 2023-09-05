package vsauko.mineplayapi.api.mpuser.classes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.event.Listener;

/**
 * Класс отвечающий за ивенты для классов
 */
public class MpClassManager implements Listener {
   private static final Set<MPClass> REGISTERED_CLASSES = new HashSet<>();
   public static Set<MPClass> getRegisteredClasses()
   {
      return Collections.unmodifiableSet(REGISTERED_CLASSES);
   }

   public static int countClasses()
   {
      return REGISTERED_CLASSES.size();
   }

   public static void registerClass(MPClass mpClass)
   {
      REGISTERED_CLASSES.add(mpClass);
   }

}
