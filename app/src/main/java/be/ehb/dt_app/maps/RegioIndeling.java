package be.ehb.dt_app.maps;

import android.util.Log;

import java.util.Iterator;
import java.util.TreeMap;

import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 9/06/2015.
 */
public class RegioIndeling {
    private final static String OVL = "Oost-Vlaanderen";
    private final static String WVL = "West-Vlaanderen";
    private final static String VBR = "Vlaams-Brabant";
    private final static String ANT = "Antwerpen";
    private final static String LIM = "Limburg";
    private final static String WBR = "Waals-Brabant";
    private final static String NAM = "Namen";
    private final static String HEN = "Henegouwen";
    private final static String LUX = "Luxemburg";
    private final static String LUI = "Luik";
    private final static String BHG = "Brussels Hoofdstedelijk Gewest";
    private final TreeMap<String, Regio> regios;

    public RegioIndeling() {
        regios = new TreeMap<String, Regio>();
        provinciesToekennen();
    }

    public TreeMap<String, Regio> getRegios() {
        return regios;
    }

    private final void provinciesToekennen() {
        regios.put(OVL, new Regio(OVL, 51.052749f, 3.726878f, false));
        regios.put(LIM, new Regio(LIM, 50.929490f, 5.337566f, false));
        regios.put(WVL, new Regio(WVL, 51.208143f, 3.225326f, false));
        regios.put(ANT, new Regio(ANT, 51.218157f, 4.408137f, false));
        regios.put(BHG, new Regio(BHG, 50.846523f, 4.351731f, true));
        regios.put(VBR, new Regio(VBR, 50.879156f, 4.700730f, false));
        regios.put(WBR, new Regio(WBR, 50.713793f, 4.611268f, false));
        regios.put(LUI, new Regio(LUI, 50.638474f, 5.572657f, false));
        regios.put(LUX, new Regio(LUX, 49.684653f, 5.811041f, false));
        regios.put(HEN, new Regio(HEN, 50.451940f, 3.953951f, false));
        regios.put(NAM, new Regio(NAM, 50.465326f, 4.871099f, false));
        Log.d("", "Provincies aangemaakt");
    }


    protected boolean schoolToevoegenAanProvincie(School s, short a) {
        short p = s.getPostcode();
        Regio r;
        if (p >= 1000 && p < 1300) {
            r = regios.get(BHG);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + BHG);
            return true;
        }

        if (p >= 1300 && p < 1500) {
            r = regios.get(WBR);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + WBR);
            return true;
        }

        if (p >= 2000 && p < 3000) {
            r = regios.get(ANT);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + ANT);
            return true;
        }

        if (p >= 1500 && p < 2000 || p <= 3000 && p < 3500) {
            r = regios.get(VBR);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + VBR);
            return true;
        }

        if (p >= 3500 && p < 4000) {
            r = regios.get(LIM);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + LIM);
            return true;
        }

        if (p >= 4000 && p < 5000) {
            r = regios.get(LUI);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + LUI);
            return true;
        }

        if (p >= 5000 && p < 6000) {
            r = regios.get(NAM);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + NAM);
            return true;
        }

        if (p >= 6000 && p < 6500 || p >= 7000 && p < 8000) {
            r = regios.get(HEN);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + HEN);
            return true;
        }

        if (p >= 6500 && p < 7000) {
            r = regios.get(LUX);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + LUX);
            return true;
        }

        if (p >= 8000 && p < 9000) {
            r = regios.get(WVL);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + WVL);
            return true;
        }

        if (p >= 9000 && p < 10000) {
            r = regios.get(OVL);
            r.schoolToevoegen(s, a);
            Log.d("", "School " + s.getName() + " toegevoegd aan " + OVL);
            return true;
        }

        return false;
    }

    public float berekenGemiddelde() {
        float totaal = 0;
        int aantalStudenten = 0;
        short aantalRegios = 0;
        Iterator<Regio> i = regios.values().iterator();
        Regio regio;
        while (i.hasNext()) {
            regio = i.next();
            if (!regio.getLocaties().isEmpty()) {
                aantalStudenten = regio.getAantalStudenten();
                Log.d("", "Provincie: " + regio.getNaam() + ": " + aantalStudenten);
                totaal += aantalStudenten;
                aantalRegios++;
            }

        }

        float gemiddelde = totaal / aantalRegios;
        return gemiddelde;
    }

    public void toonRegios() {
        Iterator<Regio> i = regios.values().iterator();

        Regio regio;
        while (i.hasNext()) {
            regio = i.next();
            if (!regio.getLocaties().isEmpty()) {
                Log.d("", "Regio: " + regio.getNaam() + "\nScholen: " + regio.getAantalStudenten());
            }

        }
    }

}
