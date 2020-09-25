/**
 * subclass of Employee
 * Stores the details of a manager
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/28
 */
public class Manager extends Employee {

    /**
     * constructor
     * @param empNo unique employee number for this manager.
     * @param manager direct superior.
     * @param department department the user works in.
     */
    public Manager(String empNo, String manager, String department) {
        super(empNo, manager, department, AccessLevel.MANAGER);
    }
}
