import java.sql.SQLException;
import view.View;

/**
 * The main entry point of the application.
 * Initializes DAOs for users, cards, and decks, retrieves all records from the
 * database,
 * and prints them to the console. If the application is not running in headless
 * mode,
 * launches the JavaFX view.
 */

public class Main {

    public static void main(String[] args) throws SQLException {
        boolean headless = Boolean.getBoolean("javafx.headless");
        if (!headless) {
            View.launch(View.class);
        }
    }

}
