package be.ehb.dt_app.maps;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeMap;

import be.ehb.dt_app.model.School;

/**
 * Bron
 * http://www.java2novice.com/java-collections-and-util/treeset/with-comparator/
 * Created by Bart on 9/06/2015.
 */
public class Regio implements Comparable<Regio>, Serializable {
    private final TreeMap<School, Short> locaties;
    private final String naam;
    private final float lengte, breedte;
    private final Comparator<School> comp = new SchoolComparator();
    private int aantalStudenten = 0;
    private String beschrijving;
    private boolean sorteerOpAatnal, camera = false;


    public Regio(String n, float b, float l, boolean c) {
        naam = n;
        locaties = new TreeMap<School, Short>(comp);
        lengte = l;
        breedte = b;
        camera = c;

    }


    public TreeMap<School, Short> getLocaties() {
        return locaties;
    }

    public String getNaam() {
        return naam;
    }


    protected void schoolToevoegen(School s, short a) {
        short aantal = 0;
        aantalStudenten += a;
        if (locaties.containsKey(s)) {
            aantal = locaties.get(s);
            aantal += a;
            a = aantal;
        }
        locaties.put(s, a);
    }

    protected void locatieWissen(School s) {
        short a = locaties.get(s);
        aantalStudenten -= a;
        locaties.remove(s);
    }

    public void maakBeschrijving() {
        beschrijving = "Aantal studenten uit de provincie " + naam + ": " + aantalStudenten;
        if (naam.contains("Brussel")) beschrijving.replace("de provincie", "het ");
    }


    @Override
    public int compareTo(Regio another) {
        if (sorteerOpAatnal) {
            return aantalStudenten - another.aantalStudenten;
        }

        return naam.compareTo(another.naam);
    }

    public boolean isCamera() {
        return camera;
    }

    public int getAantalStudenten() {
        return aantalStudenten;
    }

    public float getLengte() {
        return lengte;
    }

    public float getBreedte() {
        return breedte;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

}
