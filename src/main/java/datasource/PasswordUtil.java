package datasource;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String rawPassword){
        return BCrypt.hashpw(rawPassword,BCrypt.gensalt());
    }
    public static boolean checkPassword(String inputPassword, String dbPassword){
        return BCrypt.checkpw(inputPassword,dbPassword);
    }
}
