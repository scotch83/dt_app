package be.ehb.dt_app.aanvulling;

/**
 * Created by Bart on 3/06/2015.
 * Bronnen
 * http://www.bpost.be/site/nl/residential/customerservice/search/postal_codes.html
 */
public class Postcode {

    private short postcode;
    private String gemeente;

    public Postcode(int postcode, String gemeente) {
        super();
        this.postcode = (short) postcode;
        this.gemeente = gemeente;

    }

    public Postcode(short postcode, String gemeente) {
        super();
        this.postcode = postcode;
        this.gemeente = gemeente;

    }

    public Postcode() {

    }

    public short getPostcode() {
        return postcode;
    }

    public void setPostcode(short postcode) {
        this.postcode = postcode;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }


}
