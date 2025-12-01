package controller;

import model.entity.AppUsers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    static Session session;

    @BeforeAll
    static void setUp() {
        session = Session.getInstance();
    }
    static AppUsers setUpTestStudent() {
        return new AppUsers("testUser","test@email.com", "test1","student",1,new Timestamp(System.currentTimeMillis()));
    }
    static AppUsers setUpTestAdmin() {
        return new AppUsers("testUser","test@email.com", "test1","admin",1,new Timestamp(System.currentTimeMillis()));
    }
    static AppUsers setUpTestTeacher() {
        return new AppUsers("testUser","test@email.com", "test1","teacher",1,new Timestamp(System.currentTimeMillis()));

    }
    @BeforeEach
    void clearBeforeEach() {
        Session.getInstance().clear();
    }

    @Test
    void languageSessionTest() {
        session.setLanguage("en_us");
        assertEquals("en_us",session.getLanguage());

        session.setLanguage("fi_fi");
        assertEquals("fi_fi", session.getLanguage());
    }

    @Test
    void getInstance() {
        assertSame(session,Session.getInstance());
    }

    @Test
    void userToSessionTest() {

        session.setCurrentUser(setUpTestStudent());
        assertEquals(session.getCurrentUser(),session.getCurrentUser());
    }

    @Test
    void getUserId() {
        assertNull(session.getCurrentUser());
        session.setCurrentUser(setUpTestStudent());
        assertEquals(0,session.getUserId());
        session.clear();
        assertEquals(-1,session.getUserId());

    }

    @Test
    void getRole() {

        session.setCurrentUser(setUpTestStudent());
        assertEquals("student",session.getRole());
    }

    @Test
    void isAdmin() {

        session.setCurrentUser(setUpTestStudent());
        assertFalse(session.isAdmin());
        session.setCurrentUser(setUpTestAdmin());
        assertTrue(session.isAdmin());
    }

    @Test
    void isTeacher() {
        session.setCurrentUser(setUpTestAdmin());
        assertFalse(session.isTeacher());
        session.setCurrentUser(setUpTestTeacher());
        assertTrue(session.isTeacher());
    }

    @Test
    void isStudent() {

        session.setCurrentUser(setUpTestTeacher());
        assertFalse(session.isStudent());
        session.setCurrentUser(setUpTestStudent());
        assertTrue(session.isStudent());
    }
    @Test
    void clear(){

        session.setCurrentUser(setUpTestStudent());
        assertNotNull(session.getCurrentUser());
        session.clear();
        assertNull(session.getCurrentUser());
    }
    @AfterAll
    static void tearDown(){
        session.clear();
    }
}