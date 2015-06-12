package be.ehb.dt_app.model;

/**
 * Created by mobapp003 on 12/06/15.
 */
public class Pdf {

    private String naam;
    private String datum;

    public Pdf(String naam, String datum) {
        this.naam = naam;
        this.datum = datum;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }
}
