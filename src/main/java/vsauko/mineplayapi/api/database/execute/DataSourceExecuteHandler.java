package vsauko.mineplayapi.api.database.execute;

import lombok.*;
import lombok.experimental.FieldDefaults;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseConnectionHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseExecuteHandler;
import vsauko.mineplayapi.api.database.query.RemoteDatabaseQueryResult;
import vsauko.mineplayapi.api.database.query.RemoteDatabaseQueryStatement;
import vsauko.mineplayapi.api.utility.ThrowableSupplier;
import vsauko.mineplayapi.api.utility.AsyncUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class DataSourceExecuteHandler implements RemoteDatabaseExecuteHandler {

    @NonNull RemoteDatabaseConnectionHandler databaseConnection;

    @Override
    @SneakyThrows
    public @NonNull Connection getConnection() {
        return databaseConnection.getConnection();
    }

    @Override
    public void refreshConnection() {
        databaseConnection.reconnect();
    }


    @Override
    public void executeUpdate(boolean sync, @NonNull String query, Object... values) {
        Runnable command = () -> {
            refreshConnection();

            try (RemoteDatabaseQueryStatement queryStatement = new RemoteDatabaseQueryStatement(sync, getConnection(), query, values)) {
                queryStatement.executeUpdate();

                if (databaseConnection.getEventHandler() != null) {
                    databaseConnection.getEventHandler().onQueryExecuted(databaseConnection, query);
                }
            }

            catch (SQLException exception) {
                exception.printStackTrace();
            }
        };

        if (!sync) {
            AsyncUtil.submitAsync(command);
            return;
        }

        command.run();
    }

    @Override
    public @NonNull CompletableFuture<RemoteDatabaseQueryResult> executeQuery(boolean sync, @NonNull String query, Object... values) {
        refreshConnection();

        try (RemoteDatabaseQueryStatement queryStatement = new RemoteDatabaseQueryStatement(false, getConnection(), query, values)) {
            if (databaseConnection.getEventHandler() != null) {
                databaseConnection.getEventHandler().onQueryExecuted(databaseConnection, query);
            }

            return sync
                    ? CompletableFuture.completedFuture(queryStatement.executeQuery())
                    : CompletableFuture.supplyAsync((ThrowableSupplier<RemoteDatabaseQueryResult, SQLException>) queryStatement::executeQuery);
        }

        catch (SQLException exception) {
            exception.printStackTrace();

            return null;
        }
    }

}
