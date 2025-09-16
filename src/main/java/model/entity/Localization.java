package model.entity;

public class Localization {
    private int translation_id;
    private String entity_type;
    private int entity_id;
    private String language_code;
    private String translated_text;

    public Localization(String entity_type, int entity_id, String language_code, String translated_text) {
        this.entity_type = entity_type;
        this.entity_id = entity_id;
        this.language_code = language_code;
        this.translated_text = translated_text;
    }

    // Getters and Setters
    public int getTranslation_id() {
        return translation_id;
    }

    public void setTranslation_id(int translation_id) {
        this.translation_id = translation_id;
    }

    public String getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getTranslated_text() {
        return translated_text;
    }

    public void setTranslated_text(String translated_text) {
        this.translated_text = translated_text;
    }

    @Override
    public String toString() {
        return "Localization{" +
                "translation_id=" + translation_id +
                ", entity_type='" + entity_type + '\'' +
                ", entity_id=" + entity_id +
                ", language_code='" + language_code + '\'' +
                ", translated_text='" + translated_text + '\'' +
                '}';
    }
}