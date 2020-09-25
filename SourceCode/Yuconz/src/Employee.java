/**
 * subclass of User and superclass of HREmployee and Manager
 * stores the details of an employee
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class Employee extends User {

    /**
     * constructor
     * @param empNo employee's unique employee number.
     * @param manager employees direct manager
     * @param department department the employee works for.
     */
    public Employee(String empNo, String manager, String department) {
        super(empNo, department, AccessLevel.EMPLOYEE, manager);
    }

    /**
     * second constructor for different access levels.
     * @param empNo user's unique employee number.
     * @param manager user's direct superior
     * @param department department the user works in.
     * @param a access level the user has.
     */
    protected Employee(String empNo, String manager, String department, AccessLevel a) {
        super(empNo, department, a, manager);
    }
}
