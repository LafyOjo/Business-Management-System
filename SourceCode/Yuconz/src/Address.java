/**
 * stores the details of an employee's address
 * (part of their personal details)
 *
 * @author Alice Jaffray and Kieran D'Arcy
 * @version 2019/02/16
 */
public class Address {
    private String houseNo;
    private String street;
    private String town;
    private String county;
    private String postcode;

    /**
     * constructor
     * @param houseNo house number in street
     * @param street name of street
     * @param town town name
     * @param county name of county
     * @param postcode postcode of house
     */
    public Address(String houseNo, String street, String town, String county, String postcode)
    {
        this.houseNo = houseNo;
        this.street = street;
        this.town = town;
        this.county = county;
        this.postcode = postcode;
    }
}
