package controller;

import model.entity.AppUsers;
import org.junit.jupiter.api.BeforeAll;
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

    @Test
    void setLanguage() {
        session.setLanguage("en_us");
        assertEquals("en_us",session.getLanguage());
    }

    @Test
    void getLanguage() {
        assertNull(session.getLanguage());
        session.setLanguage("en_us");
        assertEquals("en_us",session.getLanguage());
    }

    @Test
    void getInstance() {
        assertSame(session,Session.getInstance());
    }

    @Test
    void setCurrentUser() {
        assertNull(session.getCurrentUser());
        session.setCurrentUser(setUpTestStudent());
        assertSame(session.getCurrentUser(),session.getCurrentUser());
    }

    @Test
    void getCurrentUser() {
        assertNull(session.getCurrentUser());
        session.setCurrentUser(setUpTestStudent());
        assertSame(session.getCurrentUser(),session.getCurrentUser());

    }

    @Test
    void getUserId() {
        assertEquals(-1,session.getUserId());
        session.setCurrentUser(setUpTestStudent());
        assertEquals(0,session.getUserId());

    }

    @Test
    void getRole() {
        assertNull(session.getRole());
        session.setCurrentUser(setUpTestStudent());
        assertEquals("student",session.getRole());
    }

    @Test
    void isAdmin() {
        assertFalse(session.isAdmin());
        session.setCurrentUser(setUpTestStudent());
        assertFalse(session.isAdmin());
        session.setCurrentUser(setUpTestAdmin());
        assertTrue(session.isAdmin());
    }

    @Test
    void isTeacher() {
        assertFalse(session.isTeacher());
        session.setCurrentUser(setUpTestAdmin());
        assertFalse(session.isTeacher());
        session.setCurrentUser(setUpTestTeacher());
        assertTrue(session.isTeacher());
    }

    @Test
    void isStudent() {
        assertFalse(session.isStudent());
        session.setCurrentUser(setUpTestTeacher());
        assertFalse(session.isStudent());
        session.setCurrentUser(setUpTestStudent());
        assertTrue(session.isStudent());
    }
    @Test
    void clear(){
        assertNull(session.getCurrentUser());
        session.setCurrentUser(setUpTestStudent());
        assertNotNull(session.getCurrentUser());
        session.clear();
        assertNull(session.getCurrentUser());
    }
}