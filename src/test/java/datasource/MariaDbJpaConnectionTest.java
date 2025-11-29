package datasource;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class MariaDbJpaConnectionTest {
    //tests connection
    @Test
    void testGetConnection() {
        try {
            Connection connection = MariaDbJpaConnection.getConnection();

            assertNotNull(connection, "Connection should not be null");

            assertTrue(connection.isValid(3), "Connection should be valid");

            MariaDbJpaConnection.terminate();
        } catch (Exception e) {
            fail("Exception occurred while testing the connection: " + e.getMessage());
        }
    }
    @Test
    void testTerminateWithoutConnection() {
        try {
            MariaDbJpaConnection.terminate();
        } catch (Exception e) {
            fail("Exception occurred while terminating without connection: " + e.getMessage());
        }
    }
}





