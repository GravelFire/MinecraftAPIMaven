package vsauko.mineplayapi.api.database.connection;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseConnectionHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseExecuteHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseEventListener;
import vsauko.mineplayapi.api.database.execute.DataSourceExecuteHandler;
import vsauko.mineplayapi.api.utility.AsyncUtil;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SqliteDatabaseConnection implements RemoteDatabaseConnectionHandler {

    @NonNull File file;

    Connection connection;

    RemoteDatabaseExecuteHandler executeHandler;

    @Setter
    RemoteDatabaseEventListener eventHandler;


    @Override
    @SneakyThrows
    public void handleConnection() {
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        executeHandler = new DataSourceExecuteHandler(this);

        // Handle event.
        if (eventHandler != null) {
            eventHandler.onDatabaseConnected(this);
        }
    }

    @Override
    @SneakyThrows
    public void handleDisconnect() {
        if (eventHandler != null) {
            eventHandler.onDatabaseDisconnected(this);
        }

        AsyncUtil.submitThrowsAsync(connection::close);
    }

    @Override
    @SneakyThrows
    public void reconnect() {
        if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
            return;
        }

        connection = null;
        handleConnection();

        if (eventHandler != null) {
            eventHandler.onDatabaseReconnected(this);
        }
    }

}
