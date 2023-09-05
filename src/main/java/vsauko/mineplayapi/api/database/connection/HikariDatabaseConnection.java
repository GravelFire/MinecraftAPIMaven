package vsauko.mineplayapi.api.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import vsauko.mineplayapi.api.database.RemoteDatabaseConnectionFields;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseConnectionHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseExecuteHandler;
import vsauko.mineplayapi.api.database.interfaces.RemoteDatabaseEventListener;
import vsauko.mineplayapi.api.database.execute.DataSourceExecuteHandler;
import vsauko.mineplayapi.api.utility.AsyncUtil;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class HikariDatabaseConnection implements RemoteDatabaseConnectionHandler {

    @NonNull final RemoteDatabaseConnectionFields connectionFields;

    Connection connection;

    RemoteDatabaseExecuteHandler executeHandler;

    @Setter
    RemoteDatabaseEventListener eventHandler;


    @Override
    @SneakyThrows
    public void handleConnection() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s",
                connectionFields.getHost(), connectionFields.getPort(), connectionFields.getScheme()));

        hikariConfig.setUsername(connectionFields.getUsername());
        hikariConfig.setPassword(connectionFields.getPassword());

        hikariConfig.addDataSourceProperty("cachePrepStmts" , "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");

        connection = new HikariDataSource(hikariConfig).getConnection();
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
