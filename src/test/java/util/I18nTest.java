package util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class I18nTest {

    @BeforeEach
    void setUp() {
        I18n.setLocale(Locale.ENGLISH);
    }

    @Test
    void getLocale() {
        assertSame(Locale.ENGLISH,I18n.getLocale());

    }

    @Test
    void setLocale() {
        Locale testLocale = Locale.GERMANY;
        I18n.setLocale(testLocale);
        assertSame(testLocale,I18n.getLocale());
    }
    //TODO: localePropertyTest
    @Test
    void localeProperty() {
    }
    //TODO: getBundleTest
    @Test
    void getBundle() {
    }

    @Test
    void get() {
        assertEquals("EN bundle",I18n.get("test.bundleTest"));
    }
}