/**
 * subclss of Document
 * stores the information needed for a personal details record
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class PersonalDetails extends Document{
    private String forename;
    private String surname;
    private String dob;
    private String mobileNo;
    private String telephoneNo;
    private String emergContact;
    private String emergTel;

    /**
     * constructor
     *
     * @param empNo employee number of the employee who the document refers to
     */
    public PersonalDetails(String empNo) {
        super(empNo);
        forename = "";
        surname = "";
        dob = "";
        mobileNo  = "";
        telephoneNo = "";
        emergContact = "";
        emergTel = "";
    }

    /**
     * gets the first name of an employee
     *
     * @return the forename of an employee
     */
    public String getForename() {
        return forename;
    }

    /**
     * sets the first name of an employee
     *
     * @param forename first name of the employee
     */
    public void setForename(String forename) {
        this.forename = forename;
    }

    /**
     * gets the last name of an employee
     *
     * @return the surname of the employee
     */
    public String getSurname() {
        return surname;
    }

    /**
     * sets the last name of the employee
     *
     * @param surname the last name of the employee
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * gets the date of birth of an employee
     *
     * @return the date of birth of the employee
     */
    public String getDob() {
        return dob;
    }

    /**
     * sets the date of birth of an employee
     *
     * @param dob the date of birth of an employee
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * gets the mobile number of the employee
     *
     * @return mobile number of the employee
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * sets the mobile number of an employee
     *
     * @param mobileNo the mobile number of an employee
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * ges the telephone number of an employee
     *
     * @return the telephone number of the employee
     */
    public String getTelephoneNo() {
        return telephoneNo;
    }

    /**
     * sets the telephone number of an employee
     *
     * @param telephoneNo the telephone number of an employee
     */
    public void setTelephoneNo(String telephoneNo) {
        this.telephoneNo = telephoneNo;
    }

    /**
     * gets the emergency contact name for an employee
     *
     * @return the name of the emergency contact for an employee
     */
    public String getEmergContact() {
        return emergContact;
    }

    /**
     * sets the emergency contact name for an employee
     *
     * @param emergContact name of emergency contact for an employee
     */
    public void setEmergContact(String emergContact) {
        this.emergContact = emergContact;
    }

    /**
     * gets the emergency telephone number of an employee's emergency contact
     *
     * @return the emergency contact's telephone number for an employee
     */
    public String getEmergTel() {
        return emergTel;
    }

    /**
     * sets the emergency contact's telephone number for an employee
     *
     * @param emergTel the telephone number of an employee's emergency contact
     */
    public void setEmergTel(String emergTel) {
        this.emergTel = emergTel;
    }
}

