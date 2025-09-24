package datasource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class MariaDbJpaConnection {

    private static Connection conn = null;

       private MariaDbJpaConnection() {}

    public static Connection getConnection() throws SQLException {
        Dotenv dotenv = Dotenv.load();
        String host = dotenv.get("DB_HOST");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        String database = dotenv.get("DB_DATABASE");

        String url = String.format("jdbc:mariadb://%s:3306/%s", host, database);
        return DriverManager.getConnection(url, user, password);
    }

    public static void terminate() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
