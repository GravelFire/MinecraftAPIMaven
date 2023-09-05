package vsauko.mineplayapi.api.database.interfaces;

import java.sql.Connection;
import lombok.NonNull;

public interface RemoteDatabaseConnectionHandler {

    @NonNull RemoteDatabaseExecuteHandler getExecuteHandler();

    @NonNull Connection getConnection();

    @NonNull
    RemoteDatabaseEventListener getEventHandler();

    void setEventHandler(@NonNull RemoteDatabaseEventListener eventListener);

    void handleConnection();

    void handleDisconnect();

    void reconnect();

}
