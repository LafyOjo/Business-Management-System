/**
 * subclass of Employee
 * stores the details of a HR employee
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class HREmployee extends Employee {

    /**
     * constructor
     * @param empNo employee login for the hr employee.
     * @param manager direct superior.
     * @param department department the user works in.
     */
    public HREmployee(String empNo, String manager, String department) {
        super(empNo, manager, department, AccessLevel.HREMPLOYEE);
    }
}
