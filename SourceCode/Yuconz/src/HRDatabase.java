import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;

/**
 * stores all the records of the employees
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class HRDatabase {
    private final String DATABASE;
    private final String FILENAME;
    private User user;

    /**
     * constructor
     */
    public HRDatabase(String filename, String database) {
        DATABASE = database;
        FILENAME = filename;
    }

    /**
     * Connects to the Authentication database.
     *
     * @return A connection to the database.
     */
    private Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(DATABASE);
        } catch (Exception ex) {
            System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        return con;
    }

    /**
     * can be used from other classes to check if an employee is a reviewer
     * @param empNo employee number of reivewer.
     * @return true if employee is reviewer, false otherwise
     */
    public boolean isReviewer(String empNo) {
        // SQL Query
        String sql = "SELECT reviewerOne,reviewerTwo FROM employees WHERE reviewerOne = ? OR reviewerTwo = ?;";
        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);
            prep.setString(2, empNo);

            //results
            ResultSet results = prep.executeQuery();
            if (results.getString(1) != null || results.getString(2) != null) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Get a user object from the hr database.
     *
     * @param empNo the user to get.
     * @return a user object with the user's information.
     */
    public User getUser(String empNo) {
        // SQL Query
        String sql = "SELECT * FROM employees WHERE empID = ?";
        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);

            ResultSet results = prep.executeQuery();
            if (results.getString(1) != null) {
                String user = results.getString(1);
                String department = results.getString(2);
                String manager = results.getString(3);
                String accessString = results.getString(4);
                String firstReviewer = results.getString(5);
                String secondReviewer = results.getString(6);
                AccessLevel access;
                switch (accessString) {
                    case "employee":
                        access = AccessLevel.EMPLOYEE;
                        break;
                    case "hremployee":
                        access = AccessLevel.HREMPLOYEE;
                        break;
                    case "manager":
                        access = AccessLevel.MANAGER;
                        break;
                    case "director":
                        access = AccessLevel.DIRECTOR;
                        break;
                    default:
                        access = AccessLevel.EMPLOYEE;
                        break;
                }
                return new User(user, department, access, manager, firstReviewer, secondReviewer);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Add a user to the HR database.
     *
     * @param empNo      employee number to add.
     * @param department department the user works in.
     * @param manager    the employee number of the user's direct superior.
     * @param access     the access level of the new user.
     * @return true if successful.
     */
    boolean addUser(String empNo, String department, String manager, String access) {
        //Query
        String sql = "INSERT INTO employees (empID, department, manager, access, reviewerOne) values (?, ?, ?, ?, ?);";

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, empNo);
            prep.setString(2, department);
            prep.setString(3, manager);
            prep.setString(4, access);
            prep.setString(5, manager);
            prep.executeUpdate();
            return true;
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
            return false;
        }
    }

    /**
     * Set the manager for an employee.
     * @param empNo the employee number of the subordinate.
     * @param manager the employee number of the manager
     * @return if the change was successful
     */
    boolean setManager(String empNo, String manager) {
        //Query
        String sql = "UPDATE employees SET manager = ?, reviewerOne = ? WHERE empID = ?;";

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {
            prep.setString(1, manager);
            prep.setString(2, manager);
            prep.setString(3, empNo);
            prep.executeUpdate();
            return true;
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx.getMessage());
            return false;
        }
    }

    /**
     * allows user to read the personal details record of an employee
     *
     * @param empNo     the number of the employee who's record it refers to
     * @param requester the user who has requested to view the record
     * @return personal details record of an employee or null otherwise
     */
    public PersonalDetails readPersonalDetails(String empNo, User requester) {
        if (requester.getAccessLevel() == AccessLevel.HREMPLOYEE || requester.getAccessLevel() == AccessLevel.DIRECTOR || empNo.equals(requester.getEmpNo())) {
            // SQL Query
            String sql = "SELECT * FROM PersonalDetails where empNo = ?;";
            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                // Fill placeholders (? characters).
                prep.setString(1, empNo);

                ResultSet results = prep.executeQuery();
                if (results.getString(1) != null) {
                    PersonalDetails document = new PersonalDetails(empNo);
                    document.setForename(results.getString(2));
                    document.setSurname(results.getString(3));
                    document.setDob(results.getString(4));
                    document.setMobileNo(results.getString(5));
                    document.setTelephoneNo(results.getString(6));
                    document.setEmergContact(results.getString(7));
                    document.setEmergTel(results.getString(8));
                    writeToFile(requester.getEmpNo(), empNo + ".ReadPersonalDetails", true);
                    return document;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }

        }
        writeToFile(requester.getEmpNo(), empNo + ".ReadPersonalDetails", false);
        return null;
    }

    /**
     * allows a user to create a record of personal details of an employee
     *
     * @param empNo     the employee number who the record refers to
     * @param requester the user who is requesting to create a personal details record
     * @return true if a new record of personal details is created and false otherwise
     */
    public boolean createPersonalDetails(String empNo, User requester) {
        if (requester.getAccessLevel() == AccessLevel.HREMPLOYEE) {
            //SQL query
            String sql = "INSERT INTO PersonalDetails (empNo) VALUES (?);";
            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                //fill placeholder
                prep.setString(1, empNo);
                prep.executeUpdate();
                writeToFile(requester.getEmpNo(), empNo + ".CreatePersonalDetails", true);
                return true;
            } catch (SQLException sqlEx) {
                System.err.println(sqlEx.getMessage());
                writeToFile(requester.getEmpNo(), empNo + ".CreatePersonalDetails", false);
                return false;
            }
        } else {
            writeToFile(requester.getEmpNo(), empNo + ".CreatePersonalDetails", false);
            System.err.println("Access level " + requester.getAccessLevel().toString());
            return false;
        }
    }

    /**
     * allows a user to edit a personal details record
     *
     * @param empNo     the number of the employee the record is about
     * @param field     the field of the record the user wants to change
     * @param newVal    the new value of the field
     * @param requester the user who is requesting to change the record or null otherwise
     */
    public PersonalDetails amendPersonalDetails(String empNo, String field, String newVal, User requester) {
        if (requester.getAccessLevel() == AccessLevel.HREMPLOYEE || empNo.equals(requester.getEmpNo())) {
            //SQL query
            String sql = "UPDATE PersonalDetails SET " + field + " = ? WHERE empNo =?;";
            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                //fill placeholder
                prep.setString(1, newVal);
                prep.setString(2, empNo);
                //Execute statement
                prep.executeUpdate();
                writeToFile(requester.getEmpNo(), empNo + ".AmendPersonalDetails", true);
                return readPersonalDetails(empNo, requester);
            } catch (SQLException sqlEx) {
                System.err.println(sqlEx.getMessage());
            }
        }
        writeToFile(requester.getEmpNo(), empNo + ".AmendPersonalDetails", false);
        return null;
    }

    /**
     * allows user to read Annual Reviews
     *
     * @param empNo     the number of the employee who's review it refers to
     * @param requester the user who has requested to view the review
     * @param year      the year of the review
     * @return annual review or null otherwise
     */
    public AnnualReview readAnnualReview(String empNo, String year, User requester) {
        user = getUser(empNo);
        if (requester.getEmpNo().equals(empNo) || (requester.getEmpNo().equals(user.getReviewerOne()) || requester.getEmpNo().equals(user.getReviewerTwo())) && requester.getAccessLevel() == AccessLevel.REVIEWER || requester.getAccessLevel() == AccessLevel.DIRECTOR) {
            // SQL Query
            String sql = "SELECT * FROM AnnualReviews WHERE empID = ? AND year = ?;";
            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                // Fill placeholders (? characters).
                prep.setString(1, empNo);
                prep.setString(2, year);

                ResultSet results = prep.executeQuery();
                if (results.getString(1) != null && results.getString(15) != null) {
                    AnnualReview document = new AnnualReview(empNo);
                    document.setName(results.getString(2));
                    document.setFirstReviewer(results.getString(3));
                    document.setSecondReviewer(results.getString(4));
                    document.setSection(results.getString(5));
                    document.setJobTitle(results.getString(6));
                    document.setObjectives(results.getString(7).split("CHAR(10)"));
                    document.setAchievements(results.getString(8).split("CHAR(10)"));
                    document.setSummary(results.getString(9));
                    document.setGoals(results.getString(10).split("CHAR(10)"));
                    document.setReviewerComments(results.getString(11).split("CHAR(10)"));
                    document.setSignedByReviewee(results.getBoolean(12));
                    document.setSignedByReviewerOne(results.getBoolean(13));
                    document.setSignedByReviewerTwo(results.getBoolean(14));
                    document.setYear(results.getString(15));
                    document.setReadOnly(results.getBoolean(16));
                    writeToFile(requester.getEmpNo(), empNo + ".ReadAnnualReview", true);
                    return document;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }

        }
        writeToFile(requester.getEmpNo(), empNo + ".ReadAnnualReview", false);
        return null;
    }

    /**
     * allows a user to create an annual review of an employee
     *
     * @param empNo     the employee number who the review refers to
     * @param requester the user who is requesting to create an annual review
     * @return true if a new annual review is created and false otherwise
     */
    public boolean createAnnualReview(String empNo, User requester) {
        user = getUser(empNo);
        if (requester.getEmpNo().equals(empNo) && user.getReviewerOne() != null && user.getReviewerTwo() != null) {
            //current year
            String year = new SimpleDateFormat("yyyy").format(new Date());
            //SQL query
            String sql = "INSERT INTO AnnualReviews (empID, firstReviewer, secondReviewer, year) VALUES (?, ?, ?,?);";

            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                //fill placeholder
                prep.setString(1, empNo);
                prep.setString(2, user.getReviewerOne());
                prep.setString(3, user.getReviewerTwo());
                prep.setString(4, year);
                prep.executeUpdate();
                writeToFile(requester.getEmpNo(), empNo + ".CreateAnnualReview", true);
                return true;
            } catch (SQLException sqlEx) {
                System.err.println(sqlEx.getMessage());
                writeToFile(requester.getEmpNo(), empNo + ".CreateAnnualReview", false);
                return false;
            }
        } else {
            writeToFile(requester.getEmpNo(), empNo + ".CreateAnnualReview", false);
            System.err.println("Access level " + requester.getAccessLevel().toString());
            return false;
        }
    }

    /**
     * allows a reviewers to edit a review record
     *
     * @param empNo     the employee's number who the record is about
     * @param year      the year the review was created
     * @param requester the user requesting to amend the record
     */
    public AnnualReview amendAnnualReview(String empNo, String year, String field, String newVal, User requester) {
        AnnualReview review = readAnnualReview(empNo, year, requester);
        if(review == null) return null;
        if ((review.getFirstReviewer().equals(requester.getEmpNo()) || review.getSecondReviewer().equals(requester.getEmpNo()) || requester.getEmpNo().equals(empNo)) && !(review.isReadOnly()) && !(field.equals("signedByReviewee") || field.equals("signedByFirstReviewer") || field.equals("signedBySecondReviewer") || field.equals("year") || field.equals("readOnly")) && (requester.getAccessLevel() == AccessLevel.REVIEWER || requester.getEmpNo().equals(empNo))){
            if (field.equals("sign")) {
                return signReview(empNo, year, review, requester);
            }
            //Runs the alternative amend Annual Review method for specific fields
            review = alternativeAmendAnnualReview(empNo, year, field, newVal, requester);
            if (review != null) {
                return review;
            }

            //SQL query
            String sql = "UPDATE AnnualReviews SET " + field + " = ? WHERE empID = ? AND year = ?;";

            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                //fill placeholder
                prep.setString(1, newVal);
                prep.setString(2, empNo);
                prep.setString(3, year);

                //Execute statement
                prep.executeUpdate();
                writeToFile(requester.getEmpNo(), empNo + ".AmendAnnualReview", true);
                return readAnnualReview(empNo, year, requester);
            } catch (SQLException sqlEx) {
                System.err.println(sqlEx.getMessage());
            }
        }
        writeToFile(requester.getEmpNo(), empNo + ".AmendAnnualReview", false);
        return null;
    }

    /**
     * used by amendAnnualReview() - alternative method for amendAnnualReview for specific fields with multiple lines
     *
     * @param empNo     - employee ID
     * @param year      - year of the review
     * @param field     - field in the review that is amended
     * @param newVal    - new value of field
     * @param requester - person requesting the review document
     * @return Amended annual review document or null if failed.
     */
    private AnnualReview alternativeAmendAnnualReview(String empNo, String year, String field, String newVal, User requester) {
        if (field.equals("objectives") || field.equals("achievements") || field.equals("goals") || field.equals("reviewerComments")) { // Checks if the field requires multiple lines(e.g. Objectives, Achievements, Goals, Comments)
            //SQL query
            String sql = "UPDATE AnnualReviews SET " + field + " = (SELECT " + field + " FROM AnnualReviews WHERE empID = ? AND year = ?) || ? || CHAR(10) WHERE empID = ? AND year = ?;";

            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {

                //fill placeholder
                prep.setString(1, empNo);
                prep.setString(2, year);
                prep.setString(3, newVal);
                prep.setString(4, empNo);
                prep.setString(5, year);

                //Execute statement
                prep.executeUpdate();
                writeToFile(requester.getEmpNo(), empNo + ".AmendAnnualReview", true);
                return readAnnualReview(empNo, year, requester);
            } catch (Exception sqlEx) {
                System.err.println(sqlEx.getMessage());
            }
        }
        return null;
    }

    /**
     * called upon by amendAnnualReview() when someone tries to sign the document
     *
     * @param empNo identifier for reviewee
     * @param year year of review
     * @param review annual review document
     * @param requester employee making request
     * @return signed review or null if failed.
     */
    private AnnualReview signReview(String empNo, String year, AnnualReview review, User requester) {
        String sql = "";
        if (requester.getEmpNo().equals(empNo)) {
            sql = "UPDATE AnnualReviews SET signedByReviewee = 1 WHERE empID = ? AND year = ?;";
        } else if (requester.getEmpNo().equals(review.getFirstReviewer())) {
            sql = "UPDATE AnnualReviews SET signedByReviewerOne = 1 WHERE empID = ? AND year = ?;";
        } else if (requester.getEmpNo().equals(review.getSecondReviewer())) {
            sql = "UPDATE AnnualReviews SET signedByReviewerTwo = 1 WHERE empID = ? AND year = ?;";
        }

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {

            //fill placeholder
            prep.setString(1, empNo);
            prep.setString(2, year);

            //Execute statement
            prep.executeUpdate();
            AnnualReview rev = readAnnualReview(empNo, year, requester);
            if(rev.isSignedByReviewee() && rev.isSignedByReviewerOne() && rev.isSignedByReviewerTwo()) {readOnly(empNo, year);}
            return rev;
        } catch (Exception sqlEx) {
            System.err.println(sqlEx.getMessage());
        }
        return null;
    }

    /**
     *  used by signReview() once all document is signed by all three reviewers
     * @param empNo employee id of reviewee
     * @param year year of review.
     */
    private void readOnly(String empNo, String year) {
        //sql statement
        String sql = "UPDATE AnnualReviews SET ReadOnly = 1 WHERE empID = ? AND year = ?;";

        try (Connection con = this.connect();
             PreparedStatement prep = con.prepareStatement(sql)) {

            //fill placeholder
            prep.setString(1, empNo);
            prep.setString(2, year);

            //Execute statement
            prep.executeUpdate();
        } catch (Exception sqlEx) {
            System.err.println(sqlEx.getMessage());
        }
    }

    /**
     * Used to assign the second reviewer for the review
     * @param empNo employee id of reviewee
     * @param reviewerTwoID employee id of second reviewer.
     */
    public void assignSecondReviewer(String empNo, String reviewerTwoID) {
        User user = getUser(empNo);
        User user2 = getUser(reviewerTwoID);
        if(user == null || user2 == null || user.getReviewerOne() == null) {System.err.println("Manager not yet assigned."); return;}
        if(!user.getReviewerOne().equals(reviewerTwoID) && user2.getAccessLevel() != AccessLevel.EMPLOYEE) {
            //Query
            String sql = "UPDATE employees SET reviewerTwo = ? WHERE empID = ?;";

            try (Connection con = this.connect();
                 PreparedStatement prep = con.prepareStatement(sql)) {
                prep.setString(1, reviewerTwoID);
                prep.setString(2, empNo);
                prep.executeUpdate();
            } catch (SQLException sqlEx) {
                System.err.println(sqlEx.getMessage());
            }
        }
    }

        /**
         * Write data to the log file.
         *
         * @param empNo        Employee number.
         * @param documentName Document accessed.
         * @param success      true if allowed access.
         */
        private void writeToFile(String empNo, String documentName, boolean success){
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {
                String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String content = empNo + "," + documentName + "," + dateTime + "," + success + "\n";
                fw = new FileWriter(FILENAME, true);
                bw = new BufferedWriter(fw);
                bw.write(content);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bw != null)
                        bw.close();

                    if (fw != null)
                        fw.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

