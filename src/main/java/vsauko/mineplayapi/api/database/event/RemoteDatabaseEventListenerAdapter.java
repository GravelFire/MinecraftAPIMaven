package vsauko.mineplayapi.api.database.event;

import lombok.NonNull;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseConnectionHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseEventListener;

public abstract class RemoteDatabaseEventListenerAdapter implements RemoteDatabaseEventListener {

    public void onDatabaseConnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler) {}
    public void onDatabaseReconnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler) {}
    public void onDatabaseDisconnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler) {}

    public void onQueryExecuted(@NonNull RemoteDatabaseConnectionHandler connectionHandler, @NonNull String query) {}

}
