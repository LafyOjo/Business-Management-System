/**
 * stores the user details of a director
 * subclass of User
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class Director extends User {
    /**
     * constructor
     *
     * @param empNo unique employee number.
     * @param password password of the director
     */
    public Director(String empNo, String password) {
        super(empNo, password, AccessLevel.DIRECTOR, null);
    }
}
