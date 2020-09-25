import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class AuthServerTest {

    private final String  DATABASE = "jdbc:sqlite:DatabasesTest/YuconzTest.db";
    private AuthServer a = new AuthServer("DatabasesTest/LoginRecordsTest.csv", "jdbc:sqlite:DatabasesTest/YuconzTest.db");

    @BeforeEach
    void setUp() {
        a.insertLogin("cfi000", "admin", "hremployee");
        a.insertLogin("aaa000", "password", "employee");
        a.insertLogin("mro000", "pa33word", "director");
        a.insertLogin("man000", "pa55word", "manager");
    }

    @AfterEach
    void tearDown() {
        try {
            Connection con = this.connect();
            Statement s = con.createStatement();
            String sql = "DELETE FROM users;";
            s.executeUpdate(sql);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Test
    void authenticateRight() {
        assertEquals("hremployee", a.authenticate("cfi000", "admin"));
        assertEquals("employee", a.authenticate("aaa000", "password"));
        assertEquals("director", a.authenticate("mro000", "pa33word"));
        assertEquals("manager", a.authenticate("man000", "pa55word"));
    }

    @Test
    void authenticateWrong() {
        assertEquals("denied", a.authenticate("cfi001", "admin"));
        assertEquals("denied", a.authenticate("aaa001", "employee"));
        assertEquals("denied", a.authenticate("mro001", "director"));
        assertEquals("denied", a.authenticate("man001", "manager"));
    }

    @Test
    void authenticateCase() {
        assertEquals("denied", a.authenticate("cfi000", "AdmIn"));
        assertEquals("denied", a.authenticate("aaa000", "emPloYee"));
        assertEquals("denied", a.authenticate("mro000", "diRectOr"));
        assertEquals("denied", a.authenticate("man000", "Manager"));
    }

    @Test
    void insertLogin() {
        assertEquals("denied", a.authenticate("tes999", "testPass"));
        a.insertLogin("tes999", "testPass", "employee");
        assertEquals("employee", a.authenticate("tes999", "testPass"));
    }

    @Test
    void deleteLogin() {
        assertEquals("employee", a.authenticate("aaa000", "password"));
        a.deleteLogin("aaa000");
        assertEquals("denied", a.authenticate("aaa000", "password"));
    }

    private Connection connect() {
        Connection con = null;
        try{
            String url = DATABASE;
            con = DriverManager.getConnection(url);
        } catch (Exception ex) {
            System.exit(0);
        }
        return con;
    }
}