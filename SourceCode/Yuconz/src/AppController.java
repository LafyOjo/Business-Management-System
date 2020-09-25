import java.util.Scanner;
/**
 * * controls the actions to everything
 *  *
 *  * @author Alice Jaffray and Kieran D'Arcy
 *  * @version 2019/02/28
 */
public class AppController {
    private Scanner scan;
    private User loggedInUser;
    private AccessLevel accessLevel;
    private HRDatabase hrDatabase;
    private AuthServer authServer;
    private boolean loggedIn;

    /**
     * constructor
     * @param hr HR Database object to use.
     * @param a AuthServer object to use.
     */
    AppController(HRDatabase hr, AuthServer a) {
        scan = new Scanner(System.in);
        hrDatabase = hr;
        authServer = a;
        loggedIn = false;
    }

    /**
     * Calls display methods for the user.
     */
    void runController() {
        while(!loggedIn) {
            loginPrompt();
        }
        baseMainMenu();
        switch(accessLevel) {
            case EMPLOYEE: employeeMainMenu(); break;
            case HREMPLOYEE: hREmployeeMainMenu(); break;
            case DIRECTOR: directorMainMenu(); break;
            case MANAGER: managerMainMenu(); break;
            case REVIEWER: reviewerMainMenu(); break;
            default: employeeMainMenu(); break;
        }
    }

    /**
     * Logs the user into the system
     *
     * @param empNo employee number of the user
     * @param password password of the user
     * @return true if the login was successful and false otherwise
     */
    boolean login(String empNo, String password){
        String access = authServer.authenticate(empNo, password);
        if(access.equals("denied")) {
            loggedIn = false;
            return false;
        } else {
            switch(access) {
                case "employee": accessLevel = AccessLevel.EMPLOYEE; break;
                case "hremployee": accessLevel = AccessLevel.HREMPLOYEE; break;
                case "manager": accessLevel = AccessLevel.MANAGER; break;
                case "director": accessLevel = AccessLevel.DIRECTOR; break;
                default: return false;
            }
            loggedInUser = hrDatabase.getUser(empNo);
            loggedIn = true;
            System.out.println(accessLevel.toString());
            return true;
        }
    }

    /**
     * logs the user out.
     */
    void logout(){
        System.out.println("Logged Out");
        loggedInUser = null;
        loggedIn = false;
    }

    /**
     * Get the personal details document for a different employee.
     * @param empNo The owner of the document
     * @return the document associated with empNo.
     */
    private PersonalDetails readPersonalDetails(String empNo) {
        return hrDatabase.readPersonalDetails(empNo, loggedInUser);
    }

    /**
     * Create a new blank document for the employee associated with an employee number.
     * @param empNo the employee the document is for.
     * @return true if successful.
     */
    private boolean createPersonalDetails(String empNo) {
        return hrDatabase.createPersonalDetails(empNo, loggedInUser);
    }

    /**
     * Amend a personal details document associated with an employee number.
     * @param empNo employee number
     * @param field field to change
     * @param newVal new value for the field.
     */
    private void amendPersonalDetails(String empNo, String field, String newVal) {
        printPersonalDetails(hrDatabase.amendPersonalDetails(empNo, field, newVal, loggedInUser));
    }

    /**
     * Read an annual review for a year associated with an employee number.
     * @param empNo The employee number of the reviewee.
     * @param year The year of the review.
     * @return The annual review document from the HRDatabase.
     */
    private AnnualReview readAnnualReview(String empNo, String year) {
        return hrDatabase.readAnnualReview(empNo, year, loggedInUser);
    }

    /**
     * Create an annual review document for the logged in user.
     */
    private void createAnnualReview() {
        if(hrDatabase.createAnnualReview(loggedInUser.getEmpNo(), loggedInUser)) {
            System.out.println("Annual Review document created, please amend the document using your employee number and the current year.");
        } else {
            System.out.println("Document not created, check that document does not already exist for current year.");
        }

    }

    /**
     * Amend an unsigned annual review.
     * @param empNo Employee number of reviewee.
     * @param year year the review was created.
     * @param field The field to change.
     * @param newVal The new value for the field.
     */
    private void amendAnnualReview(String empNo, String year, String field, String newVal) {
        if(hrDatabase.amendAnnualReview(empNo, year, field,newVal, loggedInUser) == null) {
            System.out.println("No document found.");
        }
    }

    /**
     * Add a second reviewer to an employee, for their next annual review.
     * For HR Employees only.
     * @param empNo The reviewee.
     * @param reviewer The second reviewer.
     */
    private void assignSecondReviewer(String empNo, String reviewer) {
        hrDatabase.assignSecondReviewer(empNo, reviewer);
    }

    /**
     * Set the manager for an employee in the HR Database.
     * @param empNo The employee number of the subordinate.
     * @param manager The employee number of the manager.
     * @return If the change was successful.
     */
    private boolean setManager(String empNo, String manager) {
        return hrDatabase.setManager(empNo, manager);
    }

    /**
     * sets the user to have an employee access level
     */
    void setBasicAccess(){
        accessLevel = AccessLevel.EMPLOYEE;
    }

    /**
     * Getter for access level.
     * @return access level for the logged in user.
     */
    AccessLevel getAccessLevel() {
        return accessLevel;
    }

    /**
     * Getter for logged in user.
     * @return the logged in user.
     */
    User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Getter for if anyone is logged into the system.
     * @return true if there is a user logged in.
     */
    boolean isLoggedIn() {
        return loggedIn;
    }

    // Methods for displaying menus and such. Untestable, so pushed lower in the class.

    /**
     * Displays the login screen functionality.
     */
    private void loginPrompt() {
        System.out.println("Welcome to Yuconz Document System. Please enter your employee number and password:");
        System.out.println();
        System.out.print("Employee Number: ");
        String empNo = scan.next();
        System.out.println();
        System.out.print("Password: ");
        String password = scan.next();
        if(login(empNo, password)) {
            System.out.println();
            System.out.println("Logged In");
            // Higher level users get the option to login as a base level employee.
            try {
                if (accessLevel != AccessLevel.EMPLOYEE) {
                    basicAccessPrompt();
                }
            } catch (NullPointerException ex) {
                System.out.println("User not present in HR database, contact system administrator.");
            }
        } else {
            System.out.println();
            System.out.println("Invalid employee number or password.");
        }
    }

    /**
     * User access menu.
     */
    private void basicAccessPrompt() {
        while (true) {
            System.out.print("Would you like to login with base employee access? (y/n): ");
            String option = scan.next().toLowerCase();
            if (option.equals("y")) {
                setBasicAccess();
                return;
            } else if (option.equals("n")) {
                return;
            } else {
                System.out.println("That is not a valid option. Please enter either 'y' or 'n' (no quotes).");
            }
        }
    }

    /**
     * Basic user main menu.
     */
    private void baseMainMenu() {
        System.out.println("Welcome to the Yuconz document system.");
        System.out.println("Please select an option.");
        System.out.println();

        System.out.println("1. Logout");
        System.out.println("2. Read Own Personal Details");
    }

    /**
     * Options for employee.
     */
    private void employeeMainMenu() {
        System.out.println("3. Amend own personal details.");
        System.out.println("4. Create Annual review Document");
        System.out.println("5. Amend own annual review document.");
        String option = scan.next();
        switch (option) {
            case "1": logout(); break;
            case "2": readOwnPersonalDetails(); break;
            case "3": amendOwnPersonalDetails(); break;
            case "4": createAnnualReview(); break;
            case "5": amendOwnReview(); break;
            default: System.out.println("That is not a valid option.");
        }
        runController();
    }

    /**
     * Options for HR Employee.
     */
    private void hREmployeeMainMenu() {
        System.out.println("3. Add new login");
        System.out.println("4. Read other personal details.");
        System.out.println("5. Create new personal details");
        System.out.println("6. Amend existing personal details.");
        System.out.println("7. Create Review Document");
        System.out.println("8. Reviewer Mode");
        System.out.println("9. Assign reviewer");
        System.out.println("10. Amend own annual review document.");
        System.out.println("11. Change employee's manager.");

        String option = scan.next();
        switch (option) {
            case "1": logout(); break;
            case "2": readOwnPersonalDetails(); break;
            case "3": addNewLogin(); break;
            case "4": readOtherPersonalDetails(); break;
            case "5": createPersonalDetails(); break;
            case "6": amendPersonalDetails(); break;
            case "7": createAnnualReview(); break;
            case "8": reviewerMainMenu(); break;
            case "9": assignSecondReviewer(); break;
            case "10": amendOwnReview(); break;
            case "11": setManager(); break;
            default: System.out.println("That is not a valid option.");
        }
        runController();
    }

    /**
     * Options for manager.
     */
    private void managerMainMenu() {
        System.out.println("3. Amend own personal Details");
        System.out.println("4. Create annual review document.");
        System.out.println("5. Reviewer Mode.");
        System.out.println("6. Amend own annual review document.");

        String option = scan.next();
        switch (option) {
            case "1": logout(); break;
            case "2": readOwnPersonalDetails(); break;
            case "3": amendOwnPersonalDetails(); break;
            case "4": createAnnualReview(); break;
            case "5": reviewerMainMenu(); break;
            case "6": amendOwnReview(); break;
            default: System.out.println("That is not a valid option.");
        }
        runController();
    }

    /**
     * Options for director.
     */
    private void directorMainMenu() {
        System.out.println("3. Read annual review document.");
        System.out.println("4. Reviewer Mode.");
        String option = scan.next();
        switch (option) {
            case "1": logout(); break;
            case "2": readOwnPersonalDetails(); break;
            case "3": readAnnualReview(); break;
            case "4": reviewerMainMenu(); break;
            default: System.out.println("That is not a valid option.");
        }
        runController();
    }

    /**
     * Options for reviewer.
     */
    private void reviewerMainMenu() {
        loggedInUser.setAccessLevel(AccessLevel.REVIEWER);
        accessLevel = AccessLevel.REVIEWER;
        if(!hrDatabase.isReviewer(loggedInUser.getEmpNo())) {
            System.out.println("No employees to review.");
            return;
        }
        System.out.println();
        System.out.println();
        System.out.println("1. Log Out");
        System.out.println("2. Amend/Sign Review");
        System.out.println("3. Read Review");
        String option = scan.next();
        switch (option) {
            case "1": logout(); break;
            case "2": amendOtherReview(); break;
            case "3": readAnnualReview(); break;
        }
        runController();
    }


    /**
     * Prints result of read own personal details.
     */
    private void readOwnPersonalDetails() {
        PersonalDetails p = readPersonalDetails(loggedInUser.getEmpNo());
        printPersonalDetails(p);
    }

    /**
     * Prints result of read other personal details.
     */
    private void readOtherPersonalDetails() {
        System.out.print("Enter employee number of document owner:");
        PersonalDetails p = readPersonalDetails(scan.next());
        printPersonalDetails(p);

    }

    /**
     * Prints a personal details document to the terminal.
     * @param p The document to print.
     */
    private void printPersonalDetails(PersonalDetails p) {
        if(p != null) {
            System.out.println();
            System.out.println("Employee Number: " + p.getEmpNo());
            System.out.println("Forename: " + p.getForename());
            System.out.println("Surname: " + p.getSurname());
            System.out.println("Date of Birth: " + p.getDob());
            System.out.println("Mobile: " + p.getMobileNo());
            System.out.println("Tel: " + p.getTelephoneNo());
            System.out.println("Emergency Contact: " + p.getEmergContact());
            System.out.println("Emergency Tel: " + p.getEmergTel());
        } else {
            System.out.println("No personal details document for that employee was found. Contact HR.");
        }
    }

    /**
     * Menu for creating personal details.
     */
    private void createPersonalDetails() {
        System.out.print("Enter employee number of new employee: ");
        boolean success = createPersonalDetails(scan.next());
        System.out.println();
        if (success) {
            System.out.println("Success! Please amend the document to add values.");
        } else {
            System.out.println("Failure, document not created. Document may already exist.");
        }
    }

    /**
     * Passes logged in user to amendPersonalDetails menu.
     */
    private void amendOwnPersonalDetails() {
        amendPersonalDetailsMenu(loggedInUser.getEmpNo());
    }

    /**
     * Passes entered user to amendPersonalDetails menu.
     */
    private void amendPersonalDetails() {
        System.out.println("Enter employee number of employee: ");
        String emp = scan.next();
        amendPersonalDetailsMenu(emp);
    }

    /**
     * Amends the personal details for a user using a text interface.
     * @param emp The user to change the details of.
     */
    private void amendPersonalDetailsMenu(String emp) {
        boolean done = false;
        while(!done){
            printPersonalDetails(readPersonalDetails(emp));
            System.out.println("Select a field to change: ");
            System.out.println("1. Forename");
            System.out.println("2. Surname");
            System.out.println("3. Date of Birth");
            System.out.println("4. Mobile Number");
            System.out.println("5. Telephone Number");
            System.out.println("6. Emergency Contact Name");
            System.out.println("7. Emergency Telephone Number");
            boolean selected = false;
            while (!selected) {
                String option = scan.next();
                System.out.print("Please enter the new value: ");
                scan.nextLine();
                switch (option) {
                    case "1": amendPersonalDetails(emp, "forename", scan.nextLine()); selected = true;  break;
                    case "2": amendPersonalDetails(emp, "surname", scan.nextLine()); selected = true;  break;
                    case "3": amendPersonalDetails(emp, "dob", scan.nextLine()); selected = true;  break;
                    case "4": amendPersonalDetails(emp, "mobileNo", scan.nextLine()); selected = true;  break;
                    case "5": amendPersonalDetails(emp, "telephoneNo", scan.nextLine()); selected = true;  break;
                    case "6": amendPersonalDetails(emp, "emergContact", scan.nextLine()); selected = true; break;
                    case "7": amendPersonalDetails(emp, "emergTel", scan.nextLine()); selected = true; break;
                    default: System.out.println("Please select a valid option.");
                }
                done = isDone(done);
            }
        }
        System.out.println("Done!");
    }

    /**
     * Add new login menu for HR employees.
     */
    private void addNewLogin() {
        System.out.println("Enter details for new user:");
        System.out.print("Enter Employee Number: ");
        String user = scan.next();
        System.out.println();
        System.out.print("Enter Password: ");
        String pass = scan.next();
        System.out.println();
        System.out.println("Select Access Level: ");
        System.out.println("1. Employee");
        System.out.println("2. HR Employee");
        System.out.println("3. Manager");
        System.out.println("4. Director");
        String access = "";
        boolean selected = false;
        while(!selected) {
            String option = scan.next();
            switch (option) {
                case "1":
                    access = "employee";
                    selected = true;
                    break;
                case "2":
                    access = "hremployee";
                    selected = true;
                    break;
                case "3":
                    access = "manager";
                    selected = true;
                    break;
                case "4":
                    access = "director";
                    selected = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
            }
        }
        System.out.println("Select Access Level: ");
        System.out.println("1. Human Resources");
        System.out.println("2. Services Delivery");
        System.out.println("3. Sales and Marketing");
        System.out.println("4. Administration");
        selected = false;
        String department = "";
        while(!selected) {
            String option = scan.next();
            switch (option) {
                case "1":
                    department = "human resources";
                    selected = true;
                    break;
                case "2":
                    department = "services delivery";
                    selected = true;
                    break;
                case "3":
                    department = "sales and marketing";
                    selected = true;
                    break;
                case "4":
                    department = "administration";
                    selected = true;
                    break;
                default:
                    System.out.println("Please select a valid option.");
            }
        }

        if(authServer.insertLogin(user, pass, access) && hrDatabase.addUser(user, department, null, access)) {
            System.out.println(user + " added to database.");
        } else {
            System.out.println(user + " could not be added to database.");
        }
    }

    /**
     * Read an annual review for an employee.
     * Asks user to enter the employee ID and the year of the review.
     */
    private void readAnnualReview() {
        System.out.print("Enter Employee number of Reviewee: ");
        String empNo = scan.next();
        System.out.println();
        System.out.print("Enter Review Year: ");
        printAnnualReview(readAnnualReview(empNo, scan.next()));
    }

    /**
     * Prints a personal details document to the terminal.
     * @param a The document to print.
     */
    private void printAnnualReview(AnnualReview a) {
        if(a != null) {
            System.out.println();
            System.out.println("Reviewee:" + a.getEmpNo());
            System.out.println("Reviewee ID:" + a.getName());
            System.out.println("Reviewer One ID:" + a.getFirstReviewer());
            System.out.println("Reviewer Two ID:" + a.getSecondReviewer());
            System.out.println("Year of Review: " + a.getYear());
            System.out.println("Department" + a.getSection());
            System.out.println("Job Title: " + a.getJobTitle());
            System.out.println("Objectives: ");
            for(String obj: a.getObjectives()) { System.out.println("  " + obj); }
            System.out.println("Achievements: ");
            for(String ach: a.getAchievements()) { System.out.println("  " + ach); }
            System.out.println("Summary: " + a.getSummary());
            System.out.println("Goals: ");
            for(String goal: a.getGoals()) { System.out.println("  " + goal); }
            System.out.println("Reviewer Comments: ");
            for(String com: a.getReviewerComments()) { System.out.println("  " + com); }
            System.out.println("Reviewee Sign:      " + a.isSignedByReviewee());
            System.out.println("Reviewer One Sign:  " + a.isSignedByReviewerOne());
            System.out.println("Reviewer Two Sign:  " + a.isSignedByReviewerTwo());
        } else {
            System.out.println("Annual Review not found. Please check employee number and year are correct.");
        }
    }

    /**
     * Allows user to choose employee and year to amend review for.
     */
    private void amendOtherReview() {
        System.out.print("Enter Employee Number: ");
        String empNo = scan.next();
        System.out.println();
        System.out.print("Enter Year: ");
        amendReviewMenu(empNo, scan.next());
    }

    /**
     * Allows user to edit their own review documents.
     */
    private void amendOwnReview() {
        System.out.print("Enter Year: ");
        amendReviewMenu(loggedInUser.getEmpNo(), scan.next());
    }

    /**
     * Manu to allow user to amend elements of an annual review.
     * @param emp employee number of reviewee.
     * @param year year of review.
     */
    private void amendReviewMenu(String emp, String year) {
        if(hrDatabase.readAnnualReview(emp, year, loggedInUser).isReadOnly()) {
            System.out.println("Document is already signed.");
            return;
        }
        boolean done = false;
        while(!done){
            System.out.println("Select a field to change/append: ");
            System.out.println("1. Add Objective");
            System.out.println("2. Add Achievement");
            System.out.println("3. Change Summary");
            System.out.println("4. Add Goal");
            System.out.println("5. Add Reviewer Comment");
            System.out.println("6. Sign Review");
            boolean selected = false;
            while (!selected) {
                String option = scan.next();
                System.out.print("Please enter the new value: ");
                scan.nextLine();
                switch (option) {
                    case "1": amendAnnualReview(emp, year, "objectives", scan.nextLine()); selected = true;  break;
                    case "2": amendAnnualReview(emp, year, "achievements", scan.nextLine()); selected = true;  break;
                    case "3": amendAnnualReview(emp, year, "summary", scan.nextLine()); selected = true;  break;
                    case "4": amendAnnualReview(emp, year, "goals", scan.nextLine()); selected = true;  break;
                    case "5": amendAnnualReview(emp, year, "reviewerComments", scan.nextLine()); selected = true;  break;
                    case "6": amendAnnualReview(emp, year, "sign", scan.nextLine()); selected = true; break;
                }
                done = isDone(done);
            }
        }
        System.out.println("Done!");
    }

    /**
     * Allows a HR employee to assign a second reviewer to an employee.
     */
    private void assignSecondReviewer(){
        System.out.print("Enter Employee Number of Reviewee:");
        String empNo = scan.next();
        System.out.println();
        System.out.print("Enter Employee ID of Second Reviewer:");
        assignSecondReviewer(empNo, scan.next());
    }

    /**
     * Check if the user is done entering data.
     * @param done If the user is done.
     * @return If the user is done.
     */
    private boolean isDone(boolean done) {
        String option;
        System.out.println("Done? (y/n)");
        boolean finished = false;
        while (!finished) {
            option = scan.next();
            switch (option) {
                case "y":
                    finished = true;
                    done = true;
                    break;
                case "n":
                    finished = true;
                    done = false;
                    break;
                default: System.out.println("Please select a valid option.");
            }
        }
        return done;
    }

    /**
     * Menu for controlling setting the manager.
     */
    private void setManager() {
        System.out.print("Enter subordinate employee number: ");
        String empNo = scan.next();
        System.out.print("Enter manager employee number:");
        if(setManager(empNo, scan.next())) {
            System.out.println("Manager Changed!");
        } else {
            System.out.println("Manager not changed, check inputs and try again.");
        }
    }

}