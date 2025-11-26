package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    void hashPassword() {
        assertNotEquals("test123", PasswordUtil.hashPassword("test123"));
    }

    @Test
    void checkPassword() {
        String testString = "test123";
        String hashedPassword = PasswordUtil.hashPassword(testString);
        assertTrue(PasswordUtil.checkPassword(testString, hashedPassword));
    }
}