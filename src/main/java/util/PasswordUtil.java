package util;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 */
public class PasswordUtil {


    /**
     * Hashes a raw password using BCrypt.
     *
     * @param rawPassword the plain text password to hash
     * @return the hashed password
     */
    public static String hashPassword(String rawPassword){
        return BCrypt.hashpw(rawPassword,BCrypt.gensalt());
    }

    /**
     * Verifies a plain text password against a hashed password.
     *
     * @param inputPassword the plain text password to verify
     * @param dbPassword the hashed password from the database
     * @return {@code true} if the password matches, {@code false} otherwise
     */
    public static boolean checkPassword(String inputPassword, String dbPassword){
        return BCrypt.checkpw(inputPassword,dbPassword);
    }
}
