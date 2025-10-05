package model.entity;

/**
 * Represents a localization entity for multi-language support.
 * Stores translated text for various entities in the system.
 *
 * <p>Currently not used in the application. Will be added in OTP2.</p>
 */
public class Localization {
    private int translation_id;
    private String entity_type;
    private int entity_id;
    private String language_code;
    private String translated_text;

    /**
     * Constructs a new Localization entity.
     *
     * @param entity_type the type of entity being translated (e.g., "card", "deck")
     * @param entity_id the ID of the specific entity being translated
     * @param language_code the language code for the translation (e.g., "en", "es")
     * @param translated_text the translated text content
     */
    public Localization(String entity_type, int entity_id, String language_code, String translated_text) {
        this.entity_type = entity_type;
        this.entity_id = entity_id;
        this.language_code = language_code;
        this.translated_text = translated_text;
    }

    // Getters and Setters

    /**
     * @return the translation ID
     */
    public int getTranslation_id() {
        return translation_id;
    }

    /**
     * @param translation_id the translation ID to set
     */
    public void setTranslation_id(int translation_id) {
        this.translation_id = translation_id;
    }

    /**
     * @return the entity type
     */
    public String getEntity_type() {
        return entity_type;
    }

    /**
     * @param entity_type the entity type to set
     */
    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }

    /**
     * @return the entity ID
     */
    public int getEntity_id() {
        return entity_id;
    }

    /**
     * @param entity_id the entity ID to set
     */
    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    /**
     * @return the language code
     */
    public String getLanguage_code() {
        return language_code;
    }

    /**
     * @param language_code the language code to set
     */
    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    /**
     * @return the translated text
     */
    public String getTranslated_text() {
        return translated_text;
    }

    /**
     * @param translated_text the translated text to set
     */
    public void setTranslated_text(String translated_text) {
        this.translated_text = translated_text;
    }

    /**
     * @return string representation of the Localization entity
     */
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