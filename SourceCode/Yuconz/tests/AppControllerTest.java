import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {

    private AuthServer a = new AuthServer("DatabasesTest/LoginRecordsTest.csv", "jdbc:sqlite:DatabasesTest/YuconzTest.db");
    private HRDatabase hr = new HRDatabase("DatabasesTest/AuthorisationRecordsTest", "jdbc:sqlite:DatabasesTest/YuconzTest.db");
    private AppController app;

    @BeforeEach
    void setUp() {
        a.insertLogin("cfi000", "admin", "hremployee");
        hr.addUser("cfi000", "human resources", null, "hremployee");
        a.insertLogin("aaa000", "password", "employee");
        hr.addUser("aaa000", "services delivery", null, "employee");
        a.insertLogin("mro000", "pa33word", "director");
        hr.addUser("mro000", "administration", null, "director");
        a.insertLogin("man000", "pa55word", "manager");
        hr.addUser("man000", "administration", null, "manager");
        app = new AppController(hr, a);
    }

    @AfterEach
    void tearDown() {
        try {
            Connection con = this.connect("jdbc:sqlite:DatabasesTest/YuconzTest.db");
            Statement s = con.createStatement();
            String sql = "DELETE FROM users;";
            s.executeUpdate(sql);
        } catch (SQLException e) {
            e.getMessage();
        }
        try {
            Connection con = this.connect("jdbc:sqlite:DatabasesTest/YuconzTest.db");
            Statement s = con.createStatement();
            String sql = "DELETE FROM employees;";
            s.executeUpdate(sql);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Test
    void rightLogin() {
        assertTrue(app.login("cfi000", "admin"), "HR Employee login failed.");
        assertTrue(app.login("aaa000", "password"), "Employee login failed.");
        assertTrue(app.login("mro000", "pa33word"), "Director login failed.");
        assertTrue(app.login("man000", "pa55word"), "Manager login failed.");
    }

    @Test
    void wrongLogin() {
        assertFalse(app.login("cfi001", "admin"), "HR Employee login failed.");
        assertFalse(app.login("aaa001", "password"), "Employee login failed.");
        assertFalse(app.login("mro001", "pa33word"), "Director login failed.");
        assertFalse(app.login("man001", "pa55word"), "Manager login failed.");
    }

    @Test
    void wrongPassword() {
        assertFalse(app.login("cfi001", "admin1"), "HR Employee login failed.");
        assertFalse(app.login("aaa001", "password1"), "Employee login failed.");
        assertFalse(app.login("mro001", "pa33word1"), "Director login failed.");
        assertFalse(app.login("man001", "pa55word1"), "Manager login failed.");
    }

    @Test
    void logout() {
        assertFalse(app.isLoggedIn(), "System starts logged in.");
        app.login("cfi000", "admin");
        assertTrue(app.isLoggedIn(), "System does not note a user is logged in before command.");
        app.logout();
        assertFalse(app.isLoggedIn(), "System does not note no user logged in after command. ");

    }

    @Test
    void setBasicAccess() {
        app.login("cfi000", "admin");
        assertEquals(app.getAccessLevel(), AccessLevel.HREMPLOYEE, "Access Level not initially set to HR employee.");
        app.setBasicAccess();
        assertEquals(app.getAccessLevel(), AccessLevel.EMPLOYEE, "Access Level not set to employee.");
        app.logout();
        app.login("aaa000", "password");
        assertEquals(app.getAccessLevel(), AccessLevel.EMPLOYEE, "Access Level not initially set to Employee.");
        app.setBasicAccess();
        assertEquals(app.getAccessLevel(), AccessLevel.EMPLOYEE, "Access Level did not remain at employee.");
        app.logout();
    }

    @Test
    void checkAccessLevels() {
        app.login("cfi000", "admin");
        assertEquals(app.getAccessLevel(), AccessLevel.HREMPLOYEE, "HR Employee did not match.");
        app.logout();
        app.login("aaa000", "password");
        assertEquals(app.getAccessLevel(), AccessLevel.EMPLOYEE, "Employee did not match.");
        app.logout();
        app.login("mro000", "pa33word");
        assertEquals(app.getAccessLevel(), AccessLevel.DIRECTOR, "Director did not match.");
        app.logout();
        app.login("man000", "pa55word");
        assertEquals(app.getAccessLevel(), AccessLevel.MANAGER, "Manager did not match.");
        app.logout();
    }

    @Test
    void rightUserObject() {
        app.login("cfi000", "admin");
        assertEquals("cfi000", app.getLoggedInUser().getEmpNo(), "HR Employee did not match.");
        app.logout();
        app.login("aaa000", "password");
        assertEquals(app.getLoggedInUser().getEmpNo(), "aaa000", "Employee did not match.");
        app.logout();
        app.login("mro000", "pa33word");
        assertEquals(app.getLoggedInUser().getEmpNo(), "mro000", "Director did not match.");
        app.logout();
        app.login("man000", "pa55word");
        assertEquals(app.getLoggedInUser().getEmpNo(), "man000", "Manager did not match.");
        app.logout();
    }

    private Connection connect(String url) {
        Connection con = null;
        try{
            con = DriverManager.getConnection(url);
        } catch (Exception ex) {
            System.exit(0);
        }
        return con;
    }

}