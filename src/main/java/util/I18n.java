package util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple internationalization helper.
 * Holds an observable Locale and provides convenient access to ResourceBundle strings.
 */
public final class I18n {
    private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(Locale.ENGLISH);
    private static final String BUNDLE_BASE = "languages.messages";

    private I18n() {}

    public static Locale getLocale() { return locale.get(); }
    public static void setLocale(Locale l) { locale.set(l); }
    public static ObjectProperty<Locale> localeProperty() { return locale; }

    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle(BUNDLE_BASE, getLocale());
    }

    public static String get(String key) {
        try {
            return getBundle().getString(key);
        } catch (Exception e) {
            return key;
        }
    }
}
