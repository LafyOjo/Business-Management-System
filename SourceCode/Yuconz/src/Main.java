
/**
 * creates all needed objects to start the program
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/16
 */
public class Main {
    /**
     * Starts the app
     */
    public static void main(String[] args) {

        AuthServer authServer = new AuthServer("Databases/LoginRecords.csv", "jdbc:sqlite:Databases/Yuconz.db");
        HRDatabase hrDatabase = new HRDatabase("Databases/AuthorisationRecords.csv", "jdbc:sqlite:Databases/Yuconz.db");


        AppController appController = new AppController(hrDatabase, authServer);
        appController.runController();
        }
    }
