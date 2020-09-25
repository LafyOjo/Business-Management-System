/**
 * superclass of Personal details and annual review
 * stores the details of a document
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/16
 */
public class Document {
    private String empNo;

    /**
     * constructor
     *
     * @param empNo the creator of the document
     */
    public Document(String empNo) {
        this.empNo = empNo;
    }

    /**
     * gets the employee's id
     *
     * @return the employee's id
     */
    public String getEmpNo(){
        return empNo;
    }
}
