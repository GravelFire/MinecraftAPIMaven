package vsauko.mineplayapi.api.database.interfaces;

import lombok.NonNull;
import vsauko.mineplayapi.api.database.query.RemoteDatabaseQueryResult;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

public interface RemoteDatabaseExecuteHandler {

    @NonNull Connection getConnection();

    @NonNull CompletableFuture<RemoteDatabaseQueryResult> executeQuery(boolean sync, @NonNull String query, Object... values);

    void executeUpdate(boolean sync, @NonNull String query, Object... values);

    void refreshConnection();

}
