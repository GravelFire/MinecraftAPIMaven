package vsauko.mineplayapi.api.protocollib.entity.impl;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import vsauko.mineplayapi.api.protocollib.entity.FakeBaseMob;

public class FakeGiant extends FakeBaseMob {

    public FakeGiant(@NonNull Location location) {
        super(EntityType.GIANT, location);
    }

}
