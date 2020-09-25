import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.security.SecureRandom;
import java.security.MessageDigest;
/**
 * stores when a successful login takes place
 * mock of server that holds login information.
 * @author Alice Jaffray and Kieran D'Arcy and Isaiah Ojo
 * @version 2019/02/16
 */
public class AuthServer {
    private final String FILENAME;
    private final String DATABASE;

    /**
     * constructor
     * @param loginLogs file path for log file.
     * @param database file path for SQLite database.
     */
    public AuthServer(String loginLogs, String database) {
        DATABASE = database;
        FILENAME = loginLogs;
    }


    /**
     * Connects to the Authentication database.
     * @return A connection to the database.
     */
    private Connection connect() {
        Connection con = null;
        try{
            String url = DATABASE;
            con = DriverManager.getConnection(url);
        } catch (Exception ex) {
            System.err.println( ex.getClass().getName() + ": " + ex.getMessage() );
            System.exit(0);
        }
        return con;
    }

    /**
     * authenticates the user for login purposes
     * logs the details of the login attempt
     *
     * @param user employee number of the user
     * @param password password of user
     * @return the user who is trying to be authenticated or null if attempt failed
     */
     String authenticate(String user, String password) {
        // SQL Query
        String sql = "select access from users where empID = ? and password = ?;";

         // Check salt is in database;
        byte[] salt = getSalt(user);
        if(salt == null) {writeToFile(user, false);return "denied";}

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            // Fill placeholders (? characters).
            prep.setString(1, user);
            prep.setBytes(2, generateHash(password, salt));

            ResultSet results = prep.executeQuery();
            // Analyse Results
            if(results.getString(1) == null)  {
                writeToFile(user, false);
                return "denied";
            } else {
                writeToFile(user, true);
                return results.getString(1);
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
            writeToFile(user, false);
            return "denied";
        }
    }

    /**
     * Get the salt for a user from the database.
     * @param empNo The user to get the salt for.
     * @return The returned salt, or null if no salt was made.
     */

    private byte[] getSalt(String empNo) {
         String sql = "select salt from users where empID = ?";
         try (Connection con = this.connect();
         PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);
            ResultSet r = prep.executeQuery();
            if(r.getBytes(1) != null) {
                return r.getBytes(1);
            }
         } catch (SQLException ex) {
             System.err.println(ex.getMessage());
         }
         return null;
    }

    /**
     * Insert a new login into the system.
     * @param empNo The new user's employee number.
     * @param password The new user's password.
     * @param access The new user's access level for the system.
     * @return true if succeeded.
     */
     boolean insertLogin(String empNo, String password, String access) {
        //Query
        String sql = "INSERT INTO users (empID, password, salt, access) values (?, ?, ?, ?);";
        // Hash and salt.
        byte[] salt = generateSalt();
        byte[] hashedPass = generateHash(password, salt);

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);
            prep.setBytes(2, hashedPass);
            prep.setBytes(3, salt);
            prep.setString(4, access);
            prep.executeUpdate();
            return true;
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
            return false;
        }
    }

    /**
     * Delete a user from the database by their login.
     * @param empNo The new user's employee number.
     * @return true if succeeded.
     */
    boolean deleteLogin(String empNo) {
        //Query
        String sql = "DELETE FROM users WHERE empID = ?;";

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);
            prep.executeUpdate();
            return true;
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
            return false;
        }
    }

    /**
     * Hash and salt a password using SHA-512.
     * @param password Password to hash.
     * @param salt Salt to use;
     * @return Hashed password.
     */
    private byte[] generateHash(String password, byte[] salt) {
        byte[] hashed = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            hashed = md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e){
            System.err.println(e.getMessage());
        }
        return hashed;
    }

    /**
     * Generate a 16 byte salt for passwords.
     * @return 16 byte secure random salt.
     */
    private byte[] generateSalt() {
        SecureRandom rand = new SecureRandom();
        byte[] salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }

    /**
     * Writes to the login records file.
     * @param empNo User attempting to log in.
     * @param success If the login was successful.
     */
    private void writeToFile(String empNo, boolean success){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try{
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String content = empNo + "," + dateTime + "," + success + "\n";
            fw = new FileWriter(FILENAME,true);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            try {
                if (bw!= null)
                    bw.close();

                if(fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Read from the LoginRecords.csv file and print to the terminal.
     */
    void readFromFile() {
        try {
            Scanner scanner = new Scanner(new File(FILENAME));
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                System.out.print(scanner.next()+" | ");
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}

