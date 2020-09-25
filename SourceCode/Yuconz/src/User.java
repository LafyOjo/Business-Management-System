/**
 * the class is the superclass for all users
 * Stores the details of a user
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class User {
    private String empNo;
    private AccessLevel accessLevel;
    private String department;
    private String manager;
    private String reviewerOne;
    private String reviewerTwo;

    /**
     * constructor
     * @param empNo
     * @param department
     * @param accessLevel
     */
    public User(String empNo, String department, AccessLevel accessLevel, String manager) {
        this.empNo = empNo;
        this.department = department;
        this.accessLevel = accessLevel;
        this.manager = manager;
    }

    /**
     * second constructor
     * @param empNo
     * @param department
     * @param accessLevel
     * @param manager
     * @param reviewerOne
     * @param reviewerTwo
     */
    public User(String empNo, String department, AccessLevel accessLevel, String manager, String reviewerOne, String reviewerTwo) {
        this.empNo = empNo;
        this.department = department;
        this.accessLevel = accessLevel;
        this.manager = manager;
        this.reviewerOne = reviewerOne;
        this.reviewerTwo = reviewerTwo;
    }

    /**
     * gets the employee number of the user
     *
     * @return employee number.
     */
    public String getEmpNo(){
        return empNo;
    }

    /**
     * gets the access level of the user
     *
     * @return the access level of the user
     */
    public AccessLevel getAccessLevel(){
        return accessLevel;
    }

    /**
     * gets user's department.
     * @return the user's department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * gets the manager of the user.
     * @return manager.
     */
    public String getManager() {
        return manager;
    }

    /**
     * gets the first reviewer of the user
     * @return reviewerOne
     */
    public String getReviewerOne() { return reviewerOne; }

    /**
     * gets second reviewer of the user
     * @return reviewerTwo
     */
    public String getReviewerTwo() { return reviewerTwo; }

    /**
     * set the access level for the user.
     * @param a The new access level.
     */
    public void setAccessLevel(AccessLevel a) { accessLevel = a; }
}
