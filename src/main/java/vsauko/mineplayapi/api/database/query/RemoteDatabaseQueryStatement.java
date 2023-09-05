package vsauko.mineplayapi.api.database.query;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import vsauko.mineplayapi.api.database.RemoteDatabasesApi;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoteDatabaseQueryStatement
        implements Closeable {

    final boolean sync;

    @NonNull final PreparedStatement preparedStatement;

    private RemoteDatabaseQueryResult result;


    /**
     * Инициализация статемента
     */
    public RemoteDatabaseQueryStatement(boolean sync,
                                        @NonNull Connection connection,
                                        @NonNull String mysqlQuery,
                                        @NonNull Object... values)
            throws SQLException {

        this.sync = sync;
        this.preparedStatement = connection.prepareStatement(mysqlQuery);

        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
        }
    }

    public void executeUpdate() throws SQLException {
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public RemoteDatabaseQueryResult executeQuery() {
        return (this.result = new RemoteDatabaseQueryResult(preparedStatement.executeQuery()));
    }

    @Override
    public void close() {
        Runnable command = () -> RemoteDatabasesApi.INSTANCE.submitSqlExceptions(() -> {

            if (result != null && !result.isClosed()) {
                result.close();
            }

            if (!preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        });

        if (!sync) {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1500);

                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

                command.run();
            });

            return;
        }

        command.run();
    }

}
