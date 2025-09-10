import java.sql.SQLException;
import java.util.List;

import model.dao.AppUsersDao;
import model.dao.CardsDao;
import model.dao.DecksDao;
import model.entity.AppUsers;
import model.entity.Cards;
import model.entity.Decks;

public class Main {

        public static void main(String[] args) throws SQLException {
        AppUsersDao usersDao = new AppUsersDao();
        CardsDao cardsDao = new CardsDao();
        DecksDao decksDao = new DecksDao();
        //usersDao.persist(new AppUsers("TestUser","emailo","testi", "student", 1, null));
        //cardsDao.persist(new Cards(1, "testi", "testi", null, null, false));
        //decksDao.persist(new Decks(2, "testi", "testi", 1, true, false, null));

        List<Cards> cards = cardsDao.getAllCards();
        List<AppUsers> users = usersDao.getAllUsers();
        List<Decks> decks = decksDao.getAllDecks();
    
        
        

        for (AppUsers user : users) {
            System.out.println(user);
        }
        for (Cards card : cards) {
            System.out.println(card);
        }
        for (Decks deck : decks) {
            System.out.println(deck);
        }
    }
    
}
