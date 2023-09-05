package vsauko.mineplayapi.api.protocollib.entity;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import vsauko.mineplayapi.api.protocollib.entity.animation.FakeEntityAnimation;
import vsauko.mineplayapi.api.protocollib.packet.AbstractPacket;

import java.util.Collection;

public interface FakeEntityLiving
        extends FakeEntityClickable {

    int getEntityId();

    EntityType getEntityType();

    Collection<AbstractPacket> getSpawnPackets();
    Collection<AbstractPacket> getDestroyPackets();

    void playAnimationAll(@NonNull FakeEntityAnimation fakeEntityAnimation);
    void playAnimation(@NonNull FakeEntityAnimation fakeEntityAnimation, @NonNull Player player);

    void setArrowCount(int arrowCount);
    void setHealthScale(float healthScale);
    void setAmbientPotionEffect(boolean ambientPotionEffect);

    void setPotionEffectColor(@NonNull ChatColor potionEffectColor);


    ChatColor getPotionEffectColor();

    boolean isAmbientPotionEffect();
    float getHealthScale();
    int getArrowCount();
}
