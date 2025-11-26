package datasource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Utility class for managing MariaDB database connections using environment variables or .env files.
 * Supports automatic detection of Docker environment and selection of the appropriate .env file.
 * Provides methods to obtain and terminate a database connection.
 */


public class MariaDbJpaConnection {

    private static final Connection conn = null;

    private MariaDbJpaConnection() {}

    public static Connection getConnection() throws SQLException {
        String host = System.getenv("DB_HOST");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");
        String database = System.getenv("DB_NAME");

        if (host == null || user == null || password == null || database == null) {
            String envFileName = getEnvFileName();
            System.out.println("Using environment file: " + envFileName);

            Dotenv dotenv = Dotenv.configure()
                    .filename(envFileName)
                    .ignoreIfMissing()
                    .load();

            host = dotenv.get("DB_HOST");
            user = dotenv.get("DB_USER");
            password = dotenv.get("DB_PASSWORD");
            database = dotenv.get("DB_NAME");
        }

        String url = String.format("jdbc:mariadb://%s:3306/%s", host, database);
        System.out.println("Connecting to database: " + url.replace(password, "***"));

        return DriverManager.getConnection(url, user, password);
    }

    private static String getEnvFileName() {
        File dockerEnvFile = new File(".env.docker");
        File defaultEnvFile = new File(".env");

        if (isRunningInDocker() && dockerEnvFile.exists()) {
            return ".env.docker";
        } else if (defaultEnvFile.exists()) {
            return ".env";
        } else {
            throw new RuntimeException("No .env file found! Create .env or .env.docker");
        }
    }

    private static boolean isRunningInDocker() {
        return new File("/.dockerenv").exists();
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