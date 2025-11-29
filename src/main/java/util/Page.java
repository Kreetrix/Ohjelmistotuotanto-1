package util;

public enum Page {
    LOGIN("/fxml/loginView.fxml", "Login"),
    REGISTER("/fxml/registerView.fxml", "register.title"),
    MAIN("/fxml/main.fxml", "app.title"),
    DECKS("/fxml/decks.fxml", "Card Memo - Decks"),
    EDIT("/fxml/cardDialog.fxml", "Card Memo - Edit card/deck"),
    EDITOR("/fxml/creation.fxml", "Card Memo - Editor")

    ;

    private final String path;
    private final String title;


    Page(String path, String title) {
        this.path = path;
        this.title = title;
    }

    public String getPath(){
        return path;
    }
    String getTitle() {
        return title;
    }
    public static Page pageByPath(String path) {
        for (Page page : Page.values()) {
            if (page.getPath().equals(path)) {
                return page;
            }
        }
        return null;
    }
}
