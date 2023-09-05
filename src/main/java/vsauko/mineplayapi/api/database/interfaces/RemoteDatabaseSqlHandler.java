package vsauko.mineplayapi.api.database.interfaces;

import java.sql.SQLException;

public interface RemoteDatabaseSqlHandler {

    void handle() throws SQLException;
}
