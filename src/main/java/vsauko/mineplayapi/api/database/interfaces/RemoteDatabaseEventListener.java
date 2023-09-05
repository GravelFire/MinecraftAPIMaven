package vsauko.mineplayapi.api.database.interfaces;

import lombok.NonNull;

public interface RemoteDatabaseEventListener {

    void onDatabaseConnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler);
    void onDatabaseReconnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler);
    void onDatabaseDisconnected(@NonNull RemoteDatabaseConnectionHandler connectionHandler);

    void onQueryExecuted(@NonNull RemoteDatabaseConnectionHandler connectionHandler, @NonNull String query);

}
