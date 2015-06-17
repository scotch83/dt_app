package be.ehb.dt_app.aanvulling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 3/06/2015.
 * http://developer.android.com/reference/android/text/TextWatcher.html
 * http://stackoverflow.com/questions/27494093/pop-up-dialog-is-shown-twice-in-android
 */
public final class Schoolzoeker {
    private final Activity activity;
    private final Postcodezoeker postcodezoeker;
    private School[] schools;
    private EditText schoolTxt, locSchoolTxt;
    private ArrayList<String> scholen;
    private short postcode;
    private ArrayList<Postcode> postcodes;
    private boolean gemeenteZoeken, schoolZoeken;

    private TextWatcher schoolLocatiezoeker = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable edit) {
            //Zoekt de gemeente van de school nadat 4 karakters zijn ingevoerd.
            String schoollocatie = locSchoolTxt.getText().toString().toLowerCase();
            if (schoollocatie.length() >= 4 && gemeenteZoeken && schoolZoeken) {
                scholen.clear();
                postcodes.clear();
                zoekSchoolOpGemeente(schoollocatie);
            }

            Log.d("", "Schoollocatie = " + schoollocatie);
            Log.d("", "Veldlengte = " + schoollocatie.length());
            if (schoollocatie.length() < 4) {
                gemeenteZoeken = true;
                schoolZoeken = true;
            }

        }
    };


    private TextWatcher schoolNaamzoeker = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Zoekt de naam van de school nadat 4 karakters zijn ingevoerd.
            String schoolnaam = schoolTxt.getText().toString();
            if (schoolnaam.length() >= 4 && gemeenteZoeken && schoolZoeken) {
                scholen.clear();
                postcodes.clear();
                zoekSchoolMetNaam(schoolnaam);

            }

            if (schoolnaam.length() < 4) {
                gemeenteZoeken = true;
                schoolZoeken = true;
            }

        }
    };

    public Schoolzoeker(EditText locSchoolTxt, EditText schoolTxt, Activity a, Postcodezoeker p) {
        maakScholen();
        scholen = new ArrayList<String>();
        postcodes = new ArrayList<Postcode>();

        this.schoolTxt = schoolTxt;
        this.locSchoolTxt = locSchoolTxt;
        activity = a;
        //schoolTxt.addTextChangedListener(schoolNaamzoeker);
        locSchoolTxt.addTextChangedListener(schoolLocatiezoeker);
        postcodezoeker = p;

    }


    private ArrayList<String> zoekSchoolMetPostcode(short postcode) {
        School school;
        Log.d("", "Postcode " + postcode);
        short s = 0;
        for (short i = 0; i < schools.length; i++) {
            school = schools[i];
            s = 0;
            if (school != null) s = school.getPostcode();
            if (s == postcode) {
                Log.d("", "Gemeente " + school.getName() + " " + school.getGemeente());
                scholen.add(school.getName() + " " + school.getGemeente());
            }
        }

        if (scholen.size() > 0) maakPopup(scholen.size());
        return scholen;
    }


    private ArrayList<String> zoekSchoolMetNaam(String zoek) {
        scholen.clear();
        postcodes.clear();
        postcode = 0;
        School school;
        String naam = "";
        String gemeente = "";
        zoek = zoek.toLowerCase();
        int lengte = 0;
        boolean ok = false;

        for (short i = 0; i < schools.length; i++) {
            school = schools[i];
            if (school != null) naam = school.getName().toLowerCase();
            if (school != null) gemeente = school.getGemeente().toLowerCase();
            lengte = naam.length();
            ok = naam.contains(zoek);
            if (!ok) ok = gemeente.contains(zoek);

            if (ok) {
                Log.d("", "School " + school.getName() + " " + school.getGemeente());
                scholen.add(school.getName() + " " + school.getGemeente());
            }
        }

        return scholen;
    }


    private ArrayList<String> zoekSchoolOpGemeente(String zoek) {
        ArrayList<Postcode> allePostcodes = postcodezoeker.getPostcodes();
        scholen.clear();
        Postcode p;
        String s;

        for (short i = 0; i < allePostcodes.size(); i++) {
            p = allePostcodes.get(i);
            if (p != null) {
                s = p.getGemeente().toLowerCase();
                if (s.contains(zoek)) {
                    Log.d("", "Geemeente " + s);
                    postcodes.add(p);
                }
            }

        }

        Log.d("", "Aantal gevonden: " + postcodes.size());
        if (postcodes.size() > 0) {
            if (gemeenteZoeken) maakGemeentenPopup();

        }


        return scholen;
    }


    private void maakPopup(int aantal) {
        String[] g = new String[aantal];
        for (short i = 0; i < aantal; i++) {
            g[i] = scholen.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Kies jouw school");
        builder.setItems(g, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                schoolZoeken = false;
                Log.d("", "keuze = " + which);
                schoolTxt.setText(scholen.get(which));

            }
        });
        builder.show();
    }


    private final void maakGemeentenPopup() {
        int aantal = postcodes.size();
        String[] g = new String[aantal];
        for (short i = 0; i < aantal; i++) {
            g[i] = postcodes.get(i).getPostcode() + " " + postcodes.get(i).getGemeente();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Kies locatie van school");
        builder.setItems(g, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gemeenteZoeken = false;

                Log.d("", "keuze = " + which);
                Log.d("", "Gekozen locatie " + postcodes.get(which).getGemeente());
                postcode = postcodes.get(which).getPostcode();
                locSchoolTxt.setText(postcodes.get(which).getGemeente());
                scholen = zoekSchoolMetPostcode(postcode);
            }
        });
        builder.setCancelable(true);
        builder.show();

    }


    public void wisVelden(short keuze) {
        switch (keuze) {
            case 1:
            case 2:
            case 3:
            case 4:
                locSchoolTxt.clearComposingText();
                gemeenteZoeken = true;
                schoolZoeken = true;
                break;
            case 5:
                schoolTxt.clearComposingText();
                schoolZoeken = true;
                break;
        }
    }


    private final void maakScholenKort() {

        schools = new School[11];
        schools[0] = new School(5957548062539770l, "Sint-Pieterscollege", "Jette", 1090);
        schools[1] = new School(5969014819913720l, "Sint-Jan Berchmanscollege", "Brussel-Stad", 1000);
        schools[2] = new School(5981780804894720l, "Sint-Guido-Instituut", "Anderlecht", 1070);
        schools[3] = new School(6464646778979998L, "IKSO", "Denderleeuw", 9470);
        schools[4] = new School(6464646778979998L, "Sint-Maartens Instituut", "Aalst", 9300);
        schools[5] = new School(4689794648979797L, "Koninklijk Atheneum", "Antwerpen", 2000);
        schools[6] = new School(4679467796164797L, "Koninklijk Atheneum", "Mechelen", 2800);
        schools[7] = new School(5487976479749797L, "Koninklijk Atheneum", "Gent", 9000);
        schools[8] = new School(4689794648979797L, "Koninklijk Atheneum", "Dendermonde", 9200);
        schools[9] = new School(4689794648979797L, "Koninklijk Atheneum", "Geraardsbergen", 9500);
        schools[10] = new School(7589676878479948L, "Koninklijk Lyceum", "Aalst", 9300);
    }

    private final void maakScholen() {
        schools = new School[1094];
        schools[0] = new School(28514, "Provinciaal Instituut PIVA", "ANTWERPEN", 2018);
        schools[1] = new School(28589, "Instituut Sint-Maria", "ANTWERPEN", 2000);
        schools[2] = new School(28589, "Instituut Sint-Maria", "ANTWERPEN", 2060);
        schools[3] = new School(28613, "Stedelijk Lyceum Olympiade", "ANTWERPEN", 2020);
        schools[4] = new School(28639, "Stedelijk Lyceum Lamorinière", "ANTWERPEN", 2018);
        schools[5] = new School(28639, "Stedelijk Lyceum Lamorinière", "ANTWERPEN", 2060);
        schools[6] = new School(28721, "Stedelijk Lyceum Cadix", "ANTWERPEN", 2000);
        schools[7] = new School(28721, "Stedelijk Lyceum Cadix", "ANTWERPEN", 2060);
        schools[8] = new School(28721, "Stedelijk Lyceum Cadix", "MERKSEM", 2170);
        schools[9] = new School(28845, "Sint-Norbertusinstituut", "ANTWERPEN", 2000);
        schools[10] = new School(28845, "Sint-Norbertusinstituut", "ANTWERPEN", 2018);
        schools[11] = new School(28852, "Onze-Lieve-Vrouwecollege", "ANTWERPEN", 2000);
        schools[12] = new School(28878, "Instituut Dames van het       Christelijk Onderwijs", "ANTWERPEN", 2000);
        schools[13] = new School(28951, "Tachkemoni Atheneum", "ANTWERPEN", 2018);
        schools[14] = new School(29017, "Stedelijk Lyceum Pestalozzi I", "ANTWERPEN", 2020);
        schools[15] = new School(29041, "Israelitisch Atheneum         Jesode-Hatora-Beth-Jacob", "ANTWERPEN", 2018);
        schools[16] = new School(29058, "Sint-Lucas Kunstsecundair", "ANTWERPEN", 2018);
        schools[17] = new School(29231, "Israelitische Middenschool    Jesode-Hatora-Beth-Jacob", "ANTWERPEN", 2018);
        schools[18] = new School(29281, "Koninklijke Balletschool      Antwerpen", "ANTWERPEN", 2000);
        schools[19] = new School(29306, "Stedelijk Lyceum Linkeroever", "ANTWERPEN", 2050);
        schools[20] = new School(29331, "Stedelijk Lyceum Lange        Beeldekens", "ANTWERPEN", 2060);
        schools[21] = new School(29348, "Stedelijk Lyceum Quellin", "ANTWERPEN", 2018);
        schools[22] = new School(29348, "Stedelijk Lyceum Quellin", "BORGERHOUT", 2140);
        schools[23] = new School(29421, "Sint-Lievenscollege           Middenschool", "ANTWERPEN", 2000);
        schools[24] = new School(29439, "Tachkemoni - Middenschool", "ANTWERPEN", 2018);
        schools[25] = new School(29447, "Instituut Maris Stella -      Sint-Agnes", "BORGERHOUT", 2140);
        schools[26] = new School(29454, "Sint-Claracollege", "ARENDONK", 2370);
        schools[27] = new School(29521, "Secundair Onderwijs voor      Schoonheidszorgen             Denise Grésiac", "BERCHEM", 2600);
        schools[28] = new School(29553, "Hiberniaschool Middelbare Steinerschool Antwerpen", "ANTWERPEN", 2000);
        schools[29] = new School(29751, "Xaveriuscollege", "BORGERHOUT", 2140);
        schools[30] = new School(29777, "Onze-Lieve-Vrouw-Presentatie", "BORNEM", 2880);
        schools[31] = new School(29785, "Onze-Lieve-Vrouw-Presentatie -Middenschool", "BORNEM", 2880);
        schools[32] = new School(29793, "Sint-Jozefsinstituut", "BORSBEEK", 2150);
        schools[33] = new School(29827, "Mater Dei Instituut", "BRASSCHAAT", 2930);
        schools[34] = new School(29843, "Sint-Michielscollege          Brasschaat", "BRASSCHAAT", 2930);
        schools[35] = new School(29851, "Mater Dei-Instituut", "BRASSCHAAT", 2930);
        schools[36] = new School(29868, "Gemeentelijk Instituut        Brasschaat Secundair Onderwijs", "BRASSCHAAT", 2930);
        schools[37] = new School(29876, "Gemeentelijke Middenschool    Brasschaat", "BRASSCHAAT", 2930);
        schools[38] = new School(29901, "Stedelijk Lyceum Lakbors", "ANTWERPEN", 2060);
        schools[39] = new School(29901, "Stedelijk Lyceum Lakbors", "DEURNE", 2100);
        schools[40] = new School(29942, "Stedelijk Lyceum Waterbaan", "DEURNE", 2100);
        schools[41] = new School(29983, "Gemeentelijk Technisch        Instituut", "DUFFEL", 2570);
        schools[42] = new School(30007, "Moretus 3", "EKEREN", 2180);
        schools[43] = new School(30015, "Moretus 1", "EKEREN", 2180);
        schools[44] = new School(30023, "Moretus 4", "EKEREN", 2180);
        schools[45] = new School(30031, "College van het Eucharistisch Hart", "ESSEN", 2910);
        schools[46] = new School(30049, "Sint-Jozefinstituut ASO", "ESSEN", 2910);
        schools[47] = new School(30056, "Don Bosco-Mariaberginstituut", "ESSEN", 2910);
        schools[48] = new School(30098, "KOGEKA 5", "GEEL", 2440);
        schools[49] = new School(30155, "Sint-Lambertusinstituut", "HEIST-OP-DEN-BERG", 2220);
        schools[50] = new School(30163, "Heilig Hart - Middenschool 1", "HEIST-OP-DEN-BERG", 2220);
        schools[51] = new School(30171, "Heilig Hart - Bovenbouw 1", "HEIST-OP-DEN-BERG", 2220);
        schools[52] = new School(30189, "Heilig Hart - Middenschool 2", "HEIST-OP-DEN-BERG", 2220);
        schools[53] = new School(30197, "Heilig Hart - Bovenbouw 2", "HEIST-OP-DEN-BERG", 2220);
        schools[54] = new School(30205, "Technisch Instituut Scheppers", "HERENTALS", 2200);
        schools[55] = new School(30213, "Francesco-Paviljoen", "HERENTALS", 2200);
        schools[56] = new School(30221, "Instituut van de              Voorzienigheid", "HERENTALS", 2200);
        schools[57] = new School(30239, "Sint-Jozefinstituut -         Normaalschool", "HERENTALS", 2200);
        schools[58] = new School(30262, "Sint-Jozefinstituut", "HERENTALS", 2200);
        schools[59] = new School(30312, "Don Bosco Technisch Instituut", "HOBOKEN", 2660);
        schools[60] = new School(30361, "Vrij Instituut voor Technisch Onderwijs", "HOOGSTRATEN", 2320);
        schools[61] = new School(30379, "Vrij Technisch Instituut      Spijker", "HOOGSTRATEN", 2320);
        schools[62] = new School(30395, "Klein Seminarie", "HOOGSTRATEN", 2320);
        schools[63] = new School(30403, "Instituut Spijker", "HOOGSTRATEN", 2320);
        schools[64] = new School(30411, "Regina Pacisinstituut - A.S.O.", "HOVE", 2540);
        schools[65] = new School(30437, "Gitok bovenbouw", "KALMTHOUT", 2920);
        schools[66] = new School(30445, "Instituut Heilig Hart", "KALMTHOUT", 2920);
        schools[67] = new School(30478, "Mater Salvatorisinstituut", "KAPELLEN", 2950);
        schools[68] = new School(30486, "Instituut Mater Salvatoris", "KAPELLEN", 2950);
        schools[69] = new School(30494, "KOGEKA 1", "KASTERLEE", 2460);
        schools[70] = new School(30502, "Vrij Technisch Instituut", "KONTICH", 2550);
        schools[71] = new School(30511, "Sint-Jozefinstituut", "KONTICH", 2550);
        schools[72] = new School(30528, "Sint-Ritacollege zesjarige    school", "KONTICH", 2550);
        schools[73] = new School(30544, "Vrij Technisch Instituut", "LIER", 2500);
        schools[74] = new School(30569, "Sint-Ursula-instituut", "LIER", 2500);
        schools[75] = new School(30577, "Sint-Aloysiusinstituut voor   Verpleegkunde", "LIER", 2500);
        schools[76] = new School(30585, "Sint-Gummaruscollege", "LIER", 2500);
        schools[77] = new School(30593, "Sint-Ursulalyceum", "LIER", 2500);
        schools[78] = new School(30635, "Ursulinen Mechelen 2", "MECHELEN", 2800);
        schools[79] = new School(30742, "Scheppersinstituut", "MECHELEN", 2800);
        schools[80] = new School(30759, "Sint-Romboutscollege", "MECHELEN", 2800);
        schools[81] = new School(30858, "Colomaplus eerste graad 1", "MECHELEN", 2800);
        schools[82] = new School(30866, "Colomaplus bovenbouw 2", "MECHELEN", 2800);
        schools[83] = new School(30924, "Stella Marisinstituut", "MERKSEM", 2170);
        schools[84] = new School(30941, "Sint-Eduardusinstituut", "MERKSEM", 2170);
        schools[85] = new School(30965, "Sint-Ludgardisschool", "MERKSEM", 2170);
        schools[86] = new School(31054, "Sint-Lutgardisinstituut", "MOL", 2400);
        schools[87] = new School(31062, "Sint-Jan Berchmanscollege", "MOL", 2400);
        schools[88] = new School(31161, "Gesubsidieerd Technisch       Instituut", "MORTSEL", 2640);
        schools[89] = new School(31179, "Gemeentelijk Instituut voor   Technisch en Handelsonderwijs", "NIJLEN", 2560);
        schools[90] = new School(31187, "Sint-Calasanzinstituut", "NIJLEN", 2560);
        schools[91] = new School(31245, "Immaculata Instituut", "WESTMALLE", 2390);
        schools[92] = new School(31252, "Maris Stella Instituut", "WESTMALLE", 2390);
        schools[93] = new School(31311, "Vita et Pax College", "SCHOTEN", 2900);
        schools[94] = new School(31328, "Sint-Michielscollege", "SCHOTEN", 2900);
        schools[95] = new School(31336, "Sint-Jozefinstituut", "SCHOTEN", 2900);
        schools[96] = new School(31344, "Sint-Cordula Instituut", "SCHOTEN", 2900);
        schools[97] = new School(31351, "Heilig Hart van               Maria-Instituut", "SCHILDE", 2970);
        schools[98] = new School(31393, "Provinciaal Instituut voor    Technisch Onderwijs", "STABROEK", 2940);
        schools[99] = new School(31427, "Heilig Grafinstituut", "TURNHOUT", 2300);
        schools[100] = new School(31435, "Heilig Grafinstituut", "TURNHOUT", 2300);
        schools[101] = new School(31451, "Heilig Grafinstituut", "TURNHOUT", 2300);
        schools[102] = new School(31468, "Hoger Instituut voor          Verpleegkunde Sint-Elisabeth", "TURNHOUT", 2300);
        schools[103] = new School(31468, "Hoger Instituut voor          Verpleegkunde Sint-Elisabeth", "MOL", 2400);
        schools[104] = new School(31476, "Sint-Victorinstituut", "TURNHOUT", 2300);
        schools[105] = new School(31492, "Heilig Grafinstituut", "TURNHOUT", 2300);
        schools[106] = new School(31559, "Heilig Grafinstituut", "TURNHOUT", 2300);
        schools[107] = new School(31583, "Kardinaal van Roey-Instituut  ASO", "LILLE", 2275);
        schools[108] = new School(31583, "Kardinaal van Roey-Instituut  ASO", "VORSELAAR", 2290);
        schools[109] = new School(31591, "Kardinaal van Roey-Instituut", "VORSELAAR", 2290);
        schools[110] = new School(31666, "Mariagaarde Instituut", "WESTMALLE", 2390);
        schools[111] = new School(31674, "Sint-Jan Berchmanscollege", "WESTMALLE", 2390);
        schools[112] = new School(31682, "Annuntia-Instituut", "WIJNEGEM", 2110);
        schools[113] = new School(31807, "Stella Matutina-Instituut", "WUUSTWEZEL", 2990);
        schools[114] = new School(31815, "Vrij Technisch Instituut", "ZANDHOVEN", 2240);
        schools[115] = new School(31849, "Sint-Jozefscollege 1", "AARSCHOT", 3200);
        schools[116] = new School(31856, "Damiaaninstituut C", "AARSCHOT", 3200);
        schools[117] = new School(31864, "Damiaaninstituut B", "AARSCHOT", 3200);
        schools[118] = new School(31881, "Stedelijk Instituut voor      Technische Beroepen - S.I.B.A.", "AARSCHOT", 3200);
        schools[119] = new School(31906, "Stedelijk Instituut voor      Technische Beroepen - S.I.M.A.", "AARSCHOT", 3200);
        schools[120] = new School(31922, "Sint-Victorinstituut -        Bovenbouw", "ALSEMBERG", 1652);
        schools[121] = new School(31931, "A.E.G. - Sint-Victorinstituut", "ALSEMBERG", 1652);
        schools[122] = new School(31963, "GO! atheneum Anderlecht", "BRUSSEL-STAD", 1000);
        schools[123] = new School(31963, "GO! atheneum Anderlecht", "ANDERLECHT", 1070);
        schools[124] = new School(31963, "GO! atheneum Anderlecht", "SINT-JANS-MOLENBEEK", 1080);
        schools[125] = new School(31997, "COOVI Secundair onderwijs", "ANDERLECHT", 1070);
        schools[126] = new School(32052, "Sint-Guido-Instituut", "ANDERLECHT", 1070);
        schools[127] = new School(32061, "Sint-Martinusscholen - ASO", "ASSE", 1730);
        schools[128] = new School(32078, "Sint-Martinusscholen TSO-BSO", "ASSE", 1730);
        schools[129] = new School(32086, "Sint-Martinusscholen -        Middenschool", "ASSE", 1730);
        schools[130] = new School(32094, "Lutgardiscollege", "OUDERGEM", 1160);
        schools[131] = new School(32102, "Sint-Jozefsinstituut -        Bovenbouw", "BETEKOM", 3130);
        schools[132] = new School(32111, "Sint-Jozefsinstituut -        Middenschool", "BETEKOM", 3130);
        schools[133] = new School(32136, "Maria Assumptalyceum          A.S.O.-T.S.O.-B.S.O.", "LAKEN", 1020);
        schools[134] = new School(32144, "Sint-Jan Berchmanscollege", "BRUSSEL-STAD", 1000);
        schools[135] = new School(32151, "Maria-Boodschaplyceum", "BRUSSEL-STAD", 1000);
        schools[136] = new School(32177, "Hoofdstedelijk Atheneum Karel Buls", "LAKEN", 1020);
        schools[137] = new School(32185, "Regina Pacisinstituut", "LAKEN", 1020);
        schools[138] = new School(32284, "Instituut Anneessens - Funck", "BRUSSEL-STAD", 1000);
        schools[139] = new School(32342, "Jan-van-Ruusbroeckollege", "LAKEN", 1020);
        schools[140] = new School(32409, "Sint-Jan Berchmanscollege", "DIEST", 3290);
        schools[141] = new School(32417, "Vrij Technisch Instituut      Mariëndaal", "DIEST", 3290);
        schools[142] = new School(32425, "Diocesane Middenschool", "DIEST", 3290);
        schools[143] = new School(32458, "Regina-Caelilyceum", "DILBEEK", 1700);
        schools[144] = new School(32524, "Don Bosco-instituut TSO/BSO", "HAACHT", 3150);
        schools[145] = new School(32532, "Don Bosco-instituut ASO", "HAACHT", 3150);
        schools[146] = new School(32541, "Middenschool Don Bosco", "HAACHT", 3150);
        schools[147] = new School(32557, "Sint-Albertuscollege -        Haasrode", "HEVERLEE", 3001);
        schools[148] = new School(32573, "Heilig-Hart&College 3", "HALLE", 1500);
        schools[149] = new School(32607, "Don Bosco Technisch Instituut", "HALLE", 1500);
        schools[150] = new School(32623, "Heilig-Hart&College 2", "HALLE", 1500);
        schools[151] = new School(32631, "Heilig-Hart&College 1", "HALLE", 1500);
        schools[152] = new School(32664, "Pedagogische Humaniora -      Heilig Hartinstituut", "HEVERLEE", 3001);
        schools[153] = new School(32672, "Heilig Hartinstituut TechnischOnderwijs", "HEVERLEE", 3001);
        schools[154] = new School(32722, "Heilig Hartinstituut Lyceum", "HEVERLEE", 3001);
        schools[155] = new School(32797, "Sint-Pieterscollege", "JETTE", 1090);
        schools[156] = new School(32813, "Sint-Theresiacollege", "KAPELLE-OP-DEN-BOS", 1880);
        schools[157] = new School(32821, "Sint-Godelieve Instituut", "KAPELLE-OP-DEN-BOS", 1880);
        schools[158] = new School(32839, "Sint-Michielsinstituut", "SCHRIEK", 2223);
        schools[159] = new School(32839, "Sint-Michielsinstituut", "KEERBERGEN", 3140);
        schools[160] = new School(32847, "GO! technisch atheneum GITBO", "KEERBERGEN", 3140);
        schools[161] = new School(32854, "Sint-Jozefinstituut", "KESSEL-LO", 3010);
        schools[162] = new School(32871, "Sancta Mariainstituut", "LEMBEEK", 1502);
        schools[163] = new School(32904, "Miniemeninstituut", "LEUVEN", 3000);
        schools[164] = new School(32921, "Vrije Technische School Leuven", "LEUVEN", 3000);
        schools[165] = new School(32938, "De Wijnpers - Provinciaal     onderwijs Leuven", "LEUVEN", 3000);
        schools[166] = new School(32946, "Sint-Franciscusinstituut voor Verpleegkunde", "LEUVEN", 3000);
        schools[167] = new School(32987, "Sint-Pieterscollege", "LEUVEN", 3000);
        schools[168] = new School(32995, "Heilige Drievuldigheidscollege", "LEUVEN", 3000);
        schools[169] = new School(33076, "Vrije Middenschool Leuven", "LEUVEN", 3000);
        schools[170] = new School(33092, "Sint-Gabriëlinstituut", "LIEDEKERKE", 1770);
        schools[171] = new School(33134, "Virgo Sapiensinstituut", "LONDERZEEL", 1840);
        schools[172] = new School(33142, "Gemeentelijk Technisch        Instituut", "LONDERZEEL", 1840);
        schools[173] = new School(33183, "Gemeentelijk Instituut voor   Secundair Onderwijs", "MACHELEN", 1830);
        schools[174] = new School(33209, "Sint-Donatusinstituut", "MERCHTEM", 1785);
        schools[175] = new School(33217, "Gemeentelijke Technische en   Beroepsschool", "MERCHTEM", 1785);
        schools[176] = new School(33225, "Gemeentelijke Technische      Tuinbouwschool", "MERCHTEM", 1785);
        schools[177] = new School(33241, "Sint-Donatusinstituut -       Middenschool", "MERCHTEM", 1785);
        schools[178] = new School(33258, "Imelda-Instituut", "BRUSSEL-STAD", 1000);
        schools[179] = new School(33258, "Imelda-Instituut", "ANDERLECHT", 1070);
        schools[180] = new School(33291, "Vrij Katholiek Onderwijs      Opwijk", "OPWIJK", 1745);
        schools[181] = new School(33308, "Vrij Katholiek Onderwijs      Opwijk - Middenschool", "OPWIJK", 1745);
        schools[182] = new School(33316, "Gemeentelijk Instituut voor   Technisch Onderwijs", "OVERIJSE", 3090);
        schools[183] = new School(33341, "Montfortaans Seminarie", "ROTSELAAR", 3110);
        schools[184] = new School(33449, "Onze-Lieve-Vrouwinstituut -   Secundair Onderwijs - A.S.O. -T.S.O - B.S.O.", "SINT-GENESIUS-RODE", 1640);
        schools[185] = new School(33514, "Sint-Jozefsinstituut", "TERNAT", 1740);
        schools[186] = new School(33522, "Katholiek Secundair Onderwijs Ternat - Sint-Angela", "TERNAT", 1740);
        schools[187] = new School(33548, "Gemeentelijk Instituut voor   Technisch Onderwijs", "TERVUREN", 3080);
        schools[188] = new School(33571, "Provinciaal Instituut voor    Secundair onderwijs", "TIENEN", 3300);
        schools[189] = new School(33671, "Sint-Angela-Instituut", "HAACHT", 3150);
        schools[190] = new School(33704, "Het College", "VILVOORDE", 1800);
        schools[191] = new School(33712, "Virgo", "VILVOORDE", 1800);
        schools[192] = new School(33721, "TechnOV", "VILVOORDE", 1800);
        schools[193] = new School(33746, "Virgo Fidelisinstituut Eerste graad II", "VILVOORDE", 1800);
        schools[194] = new School(33803, "Mater Dei-Instituut", "SINT-PIETERS-WOLUWE", 1150);
        schools[195] = new School(33811, "Sint-Jozefscollege", "SINT-PIETERS-WOLUWE", 1150);
        schools[196] = new School(33829, "Don Bosco Technisch Instituut", "SINT-PIETERS-WOLUWE", 1150);
        schools[197] = new School(33886, "Sint-Tarcisiusinstituut", "ZOUTLEEUW", 3440);
        schools[198] = new School(33894, "Sint-Leonardusinstituut", "ZOUTLEEUW", 3440);
        schools[199] = new School(33928, "Bovenbouw Sint-Gertrudis", "LANDEN", 3400);
        schools[200] = new School(33936, "Middenschool Sint-Gertrudis", "LANDEN", 3400);
        schools[201] = new School(33944, "Immaculata Maria Instituut", "ROOSDAAL", 1760);
        schools[202] = new School(33951, "Sint-Janscollege", "HOEGAARDEN", 3320);
        schools[203] = new School(33969, "Sint-Vincentiusinstituut", "ANZEGEM", 8570);
        schools[204] = new School(33993, "Sint-Jan Berchmanscollege", "AVELGEM", 8580);
        schools[205] = new School(34009, "Sint-Jan Berchmansmiddenschool", "AVELGEM", 8580);
        schools[206] = new School(34017, "Sint-Lutgartinstituut", "OEDELEM", 8730);
        schools[207] = new School(34025, "Sint-Jozef Sint-Pieter", "BLANKENBERGE", 8370);
        schools[208] = new School(34033, "Sint-Jozef Sint-Pieter", "BLANKENBERGE", 8370);
        schools[209] = new School(34041, "Sint-Jozef Sint-Pieter", "BLANKENBERGE", 8370);
        schools[210] = new School(34058, "Sint-Jozefsinstituut", "BRUGGE", 8000);
        schools[211] = new School(34074, "Vrij Technisch Instituut      Brugge", "BRUGGE", 8000);
        schools[212] = new School(34074, "Vrij Technisch Instituut      Brugge", "SINT-MICHIELS", 8200);
        schools[213] = new School(34082, "Technisch Instituut Heilige   Familie", "BRUGGE", 8000);
        schools[214] = new School(34124, "Hotel- en Toerismeschool      Spermalie", "BRUGGE", 8000);
        schools[215] = new School(34141, "Sint-Leocollege", "BRUGGE", 8000);
        schools[216] = new School(34165, "Sint-Franciscus-Xaveriusinstituut", "BRUGGE", 8000);
        schools[217] = new School(34173, "Lyceum Hemelsdaele            3", "BRUGGE", 8000);
        schools[218] = new School(34181, "Sint-Andreasinstituut", "BRUGGE", 8000);
        schools[219] = new School(34207, "Sint-Jozefsinstituut - ASO", "BRUGGE", 8000);
        schools[220] = new School(34231, "Onze-Lieve-Vrouwecollege", "SINT-KRUIS", 8310);
        schools[221] = new School(34249, "Abdijschool van Zevenkerken", "SINT-MICHIELS", 8200);
        schools[222] = new School(34256, "Onze-Lieve-Vrouw-Hemelvaart   Instituut", "SINT-MICHIELS", 8200);
        schools[223] = new School(34272, "Sint-Andreaslyceum", "SINT-KRUIS", 8310);
        schools[224] = new School(34306, "Immaculata-Instituut", "SINT-MICHIELS", 8200);
        schools[225] = new School(34314, "Hotelschool en Slagerijschool Ter Groene Poorte", "SINT-MICHIELS", 8200);
        schools[226] = new School(34331, "Vrij Handels- en              Sportinstituut Sint-Michiels", "SINT-MICHIELS", 8200);
        schools[227] = new School(34355, "Sint-Aloysiuscollege", "DIKSMUIDE", 8600);
        schools[228] = new School(34363, "Vrij Technisch Instituut", "DIKSMUIDE", 8600);
        schools[229] = new School(34389, "Sint-Godelievecollege", "GISTEL", 8470);
        schools[230] = new School(34397, "Vrij Technisch Instituut 4", "HARELBEKE", 8530);
        schools[231] = new School(34397, "Vrij Technisch Instituut 4", "WEVELGEM", 8560);
        schools[232] = new School(34447, "Spes Nostra-Instituut         T.S.O./B.S.O.", "HEULE", 8501);
        schools[233] = new School(34454, "Instituut Spes Nostra ASO", "HEULE", 8501);
        schools[234] = new School(34462, "VTI Ieper", "IEPER", 8900);
        schools[235] = new School(34471, "Heilige Familie Ieper", "IEPER", 8900);
        schools[236] = new School(34496, "Immaculata Ieper", "IEPER", 8900);
        schools[237] = new School(34512, "Lyceum Ieper", "IEPER", 8900);
        schools[238] = new School(34521, "College Ieper", "IEPER", 8900);
        schools[239] = new School(34538, "Prizma - Middenschool         Ingelmunster", "INGELMUNSTER", 8770);
        schools[240] = new School(34553, "Prizma - Campus VTI", "IZEGEM", 8870);
        schools[241] = new School(34561, "Prizma - Campus IdP", "IZEGEM", 8870);
        schools[242] = new School(34579, "Prizma - Campus College", "IZEGEM", 8870);
        schools[243] = new School(34587, "Prizma - Middenschool Izegem 1", "IZEGEM", 8870);
        schools[244] = new School(34611, "Sint-Jozefsinstituut Lyceum", "KNOKKE-HEIST", 8300);
        schools[245] = new School(34629, "Sint-Bernardusinstituut", "KNOKKE-HEIST", 8300);
        schools[246] = new School(34661, "Instituut Sint-Martinus", "KOEKELARE", 8680);
        schools[247] = new School(34678, "Hotelschool Ter Duinen", "KOKSIJDE", 8670);
        schools[248] = new School(34686, "Margareta-Maria-Instituut -   T.S.O. - B.S.O.", "KORTEMARK", 8610);
        schools[249] = new School(34694, "Margareta-Maria-Instituut -   A.S.O.", "KORTEMARK", 8610);
        schools[250] = new School(34793, "Don Boscocollege", "KORTRIJK", 8500);
        schools[251] = new School(34835, "Leielandscholen Campus OLV    Vlaanderen Kortrijk", "KORTRIJK", 8500);
        schools[252] = new School(34843, "Leielandscholen Campus Stella Maris Kortrijk", "KORTRIJK", 8500);
        schools[253] = new School(34868, "Leielandscholen Campus Sint-  Theresia Kortrijk", "KORTRIJK", 8500);
        schools[254] = new School(34926, "Leielandscholen Campus Sint-  Niklaas Kortrijk", "KORTRIJK", 8500);
        schools[255] = new School(34926, "Leielandscholen Campus Sint-  Niklaas Kortrijk", "MARKE", 8510);
        schools[256] = new School(34934, "Spes Nostra Instituut", "KUURNE", 8520);
        schools[257] = new School(34942, "Prizma - Middenschool         Lendelede", "LENDELEDE", 8860);
        schools[258] = new School(34959, "Technisch Instituut Sint-Lucas", "MENEN", 8930);
        schools[259] = new School(34975, "Sint-Aloysiuscollege", "MENEN", 8930);
        schools[260] = new School(35022, "Vrij Instituut voor           Lichamelijke Opvoeding Ter    Borcht", "MEULEBEKE", 8760);
        schools[261] = new School(35097, "Land- en Tuinbouwinstituut", "OEDELEM", 8730);
        schools[262] = new School(35139, "GO! technisch atheneum Ensorinstituut Oostende", "OOSTENDE", 8400);
        schools[263] = new School(35139, "GO! technisch atheneum Ensorinstituut Oostende", "KOEKELARE", 8680);
        schools[264] = new School(35154, "Sint-Andreasinstituut", "OOSTENDE", 8400);
        schools[265] = new School(35162, "Petrus & Paulus campus centrumOnze-Lieve-Vrouwecollege", "OOSTENDE", 8400);
        schools[266] = new School(35171, "Petrus & Paulus campus west   Sint-Lutgardinstituut", "OOSTENDE", 8400);
        schools[267] = new School(35188, "Petrus & Paulus campus west   Vrij Technisch Instituut", "OOSTENDE", 8400);
        schools[268] = new School(35212, "Petrus & Paulus campus centrumSint-Jozefinstituut", "OOSTENDE", 8400);
        schools[269] = new School(35238, "Middenschool Sint-Pieter", "OOSTKAMP", 8020);
        schools[270] = new School(35253, "Immaculata-instituut", "DE PANNE", 8660);
        schools[271] = new School(35295, "Sint-Janscollege 2", "POPERINGE", 8970);
        schools[272] = new School(35311, "Vrij Technisch Instituut", "POPERINGE", 8970);
        schools[273] = new School(35345, "Burgerschool", "ROESELARE", 8800);
        schools[274] = new School(35378, "VABI", "ROESELARE", 8800);
        schools[275] = new School(35394, "Hoger Beroepsonderwijs        Verpleegkunde Ic Dien", "ROESELARE", 8800);
        schools[276] = new School(35527, "Sint-Jozefsinstituut", "TIELT", 8700);
        schools[277] = new School(35535, "Handelsinstituut Regina Pacis", "TIELT", 8700);
        schools[278] = new School(35584, "Vrij Technisch Instituut", "TIELT", 8700);
        schools[279] = new School(35592, "Technisch Instituut           Sint-Vincentius", "TORHOUT", 8820);
        schools[280] = new School(35601, "Vrij Land- en                 Tuinbouwinstituut", "TORHOUT", 8820);
        schools[281] = new School(35618, "Sint-Jozefsinstituut", "TORHOUT", 8820);
        schools[282] = new School(35626, "Sint-Jozefscollege", "TORHOUT", 8820);
        schools[283] = new School(35634, "Vrij Technisch Instituut      Sint-Aloysius", "TORHOUT", 8820);
        schools[284] = new School(35659, "Vrij Technisch Instituut      Veurne", "VEURNE", 8630);
        schools[285] = new School(35667, "Bisschoppelijk College der    Onbevlekte Ontvangenis", "VEURNE", 8630);
        schools[286] = new School(35675, "Annuntiata-Instituut", "VEURNE", 8630);
        schools[287] = new School(35691, "Heilig Harthandelsinstituut", "WAREGEM", 8790);
        schools[288] = new School(35709, "Vrij Technisch Instituut", "WAREGEM", 8790);
        schools[289] = new School(35717, "Onze-Lieve-Vrouw-Hemelvaart-  instituut 2", "WAREGEM", 8790);
        schools[290] = new School(35741, "Heilig-Hartcollege", "WAREGEM", 8790);
        schools[291] = new School(35758, "Onze-Lieve-Vrouw-Hemelvaart-  instituut 1", "WAREGEM", 8790);
        schools[292] = new School(35766, "Hoger Beroepsonderwijs        Verpleegkunde Aleydis", "TIELT", 8700);
        schools[293] = new School(35766, "Hoger Beroepsonderwijs        Verpleegkunde Aleydis", "WAREGEM", 8790);
        schools[294] = new School(35824, "Spes Nostra Instituut", "ZEDELGEM", 8210);
        schools[295] = new School(35899, "DVM Handels-, Technisch en    Beroepsonderwijs", "AALST", 9300);
        schools[296] = new School(35907, "Sint-Augustinusinstituut", "AALST", 9300);
        schools[297] = new School(35915, "Sint-Jozefscollege", "AALST", 9300);
        schools[298] = new School(35931, "DvM - Humaniora", "AALST", 9300);
        schools[299] = new School(35964, "Technisch Instituut           Sint-Maarten", "AALST", 9300);
        schools[300] = new School(36053, "Sint-Jorisinstituut", "BAZEL", 9150);
        schools[301] = new School(36111, "Gemeentelijk Technisch        Instituut", "BEVEREN-WAAS", 9120);
        schools[302] = new School(36152, "Sint-Vincentiuscollege", "BUGGENHOUT", 9255);
        schools[303] = new School(36202, "Vrij Technisch Instituut      Deinze", "DEINZE", 9800);
        schools[304] = new School(36228, "Leiepoort Deinze campus Sint- Theresia", "DEINZE", 9800);
        schools[305] = new School(36285, "Vrij Handels- en Technisch    Instituut - bovenbouw", "DENDERMONDE", 9200);
        schools[306] = new School(36301, "Vrij Technisch Instituut", "DENDERMONDE", 9200);
        schools[307] = new School(36335, "Heilige Maagdcollege", "DENDERMONDE", 9200);
        schools[308] = new School(36343, "Sint-Vincentiusinstituut", "DENDERMONDE", 9200);
        schools[309] = new School(36418, "Onze-Lieve-Vrouw-ten-Doorn", "EEKLO", 9900);
        schools[310] = new School(36467, "Provinciaal Technisch         Instituut", "EEKLO", 9900);
        schools[311] = new School(36475, "Sint-Leoinstituut", "EEKLO", 9900);
        schools[312] = new School(36483, "Sint-Annainstituut", "EEKLO", 9900);
        schools[313] = new School(36491, "Sint-Teresiacollege", "LOKEREN", 9160);
        schools[314] = new School(36517, "Sint-Franciscus Evergem", "EVERGEM", 9940);
        schools[315] = new School(36566, "Technisch Instituut Sint-Jozef", "GERAARDSBERGEN", 9500);
        schools[316] = new School(36566, "Technisch Instituut Sint-Jozef", "OUDENAARDE", 9700);
        schools[317] = new School(36608, "Sint-Catharinacollege", "GERAARDSBERGEN", 9500);
        schools[318] = new School(36616, "Sint-Jozefsinstituut", "GERAARDSBERGEN", 9500);
        schools[319] = new School(36624, "Provinciaal Handels- en       Taalinstituut - Volledig      Leerplan", "GENT", 9000);
        schools[320] = new School(36699, "Provinciaal Instituut voor    Haartooi en Schoonheidszorgen", "GENT", 9000);
        schools[321] = new School(36715, "Hoger Technisch Instituut     Sint-Antonius", "GENT", 9000);
        schools[322] = new School(36764, "Hotelschool Gent", "GENT", 9000);
        schools[323] = new School(36913, "Vrije Handelsschool Sint-Joris", "GENT", 9000);
        schools[324] = new School(36939, "Vrij Instituut voor Secundair Onderwijs - Gent", "GENT", 9000);
        schools[325] = new School(36939, "Vrij Instituut voor Secundair Onderwijs - Gent", "MARIAKERKE", 9030);
        schools[326] = new School(36954, "Sint-Pietersinstituut         bovenbouw", "GENT", 9000);
        schools[327] = new School(36962, "Sint-Barbaracollege", "GENT", 9000);
        schools[328] = new School(36996, "Sint-Bavohumaniora", "GENT", 9000);
        schools[329] = new School(37028, "Humaniora Nieuwen Bosch", "GENT", 9000);
        schools[330] = new School(37069, "Instituut voor Verpleegkunde  Sint-Vincentius", "GENT", 9000);
        schools[331] = new School(37069, "Instituut voor Verpleegkunde  Sint-Vincentius", "ZOTTEGEM", 9620);
        schools[332] = new School(37069, "Instituut voor Verpleegkunde  Sint-Vincentius", "OUDENAARDE", 9700);
        schools[333] = new School(37069, "Instituut voor Verpleegkunde  Sint-Vincentius", "EEKLO", 9900);
        schools[334] = new School(37085, "IVG-School", "GENT", 9000);
        schools[335] = new School(37259, "Instituut Sint-Vincentius a   Paulo", "GIJZEGEM", 9308);
        schools[336] = new School(37275, "Provinciaal Technisch         Instituut Hamme", "HAMME", 9220);
        schools[337] = new School(37309, "Sint-Jozefinstituut -         Secundair Onderwijs", "HAMME", 9220);
        schools[338] = new School(37317, "Heilig Hartinstituut -        Middenschool", "HAMME", 9220);
        schools[339] = new School(37325, "Sint-Paulusinstituut", "HERZELE", 9550);
        schools[340] = new School(37499, "Vrij Technisch Instituut      Sint-Laurentius", "LOKEREN", 9160);
        schools[341] = new School(37523, "Sint-Lodewijkscollege", "LOKEREN", 9160);
        schools[342] = new School(37531, "Sint-Lodewijkscollege", "LOKEREN", 9160);
        schools[343] = new School(37556, "Virgo Sapientiae Instituut", "MALDEGEM", 9990);
        schools[344] = new School(37581, "Visitatie", "MARIAKERKE", 9030);
        schools[345] = new School(37598, "Sint-Franciscusinstituut", "MELLE", 9090);
        schools[346] = new School(37606, "College der Paters Jozefieten", "MELLE", 9090);
        schools[347] = new School(37614, "Sint-Franciscusinstituut", "MELLE", 9090);
        schools[348] = new School(37648, "Sint-Jozefschool", "MERE", 9420);
        schools[349] = new School(37655, "GO! atheneum Merelbeke", "MERELBEKE", 9820);
        schools[350] = new School(37705, "Heilig Harten Secundair", "NINOVE", 9400);
        schools[351] = new School(37747, "Provinciaal Technisch         Instituut", "NINOVE", 9400);
        schools[352] = new School(37821, "Bernardusscholen 6", "OUDENAARDE", 9700);
        schools[353] = new School(37846, "Bernardusscholen 4", "OUDENAARDE", 9700);
        schools[354] = new School(37853, "Bernardusscholen 5", "OUDENAARDE", 9700);
        schools[355] = new School(37879, "Bernardusscholen 1", "OUDENAARDE", 9700);
        schools[356] = new School(37887, "Bernardusscholen 2", "OUDENAARDE", 9700);
        schools[357] = new School(37903, "Provinciaal Instituut Vlaamse Ardennen", "OUDENAARDE", 9700);
        schools[358] = new School(38083, "Don Bosco Technisch Instituut", "SINT-DENIJS-WESTREM", 9051);
        schools[359] = new School(38125, "Edward Poppe-Instituut", "SINT-LAUREINS", 9980);
        schools[360] = new School(38158, "Sint-Franciscusinstituut", "BRAKEL", 9660);
        schools[361] = new School(38182, "Vrije Technische Scholen", "SINT-NIKLAAS", 9100);
        schools[362] = new School(38208, "Technisch Berkenboom-Instituut", "SINT-NIKLAAS", 9100);
        schools[363] = new School(38216, "Technisch Instituut           Sint-Carolus", "SINT-NIKLAAS", 9100);
        schools[364] = new School(38224, "Broederscholen Hiëronymus 2", "SINT-NIKLAAS", 9100);
        schools[365] = new School(38257, "Broederscholen Hiëronymus 1", "SINT-NIKLAAS", 9100);
        schools[366] = new School(38257, "Broederscholen Hiëronymus 1", "STEKENE", 9190);
        schools[367] = new School(38265, "Instituut Heilige Familie     Secundair", "SINT-NIKLAAS", 9100);
        schools[368] = new School(38273, "Sint-Jozef - Klein-Seminarie", "SINT-NIKLAAS", 9100);
        schools[369] = new School(38281, "Onze-Lieve-Vrouw-Presentatie secundair onderwijs", "SINT-NIKLAAS", 9100);
        schools[370] = new School(38299, "Berkenboom Humaniora bovenbouw", "SINT-NIKLAAS", 9100);
        schools[371] = new School(38307, "Onze-Lieve-Vrouw-Presentatie secundair onderwijs 1", "SINT-NIKLAAS", 9100);
        schools[372] = new School(38381, "Scheppersinstituut", "WETTEREN", 9230);
        schools[373] = new School(38422, "Mariagaard", "WETTEREN", 9230);
        schools[374] = new School(38471, "Instituut Onze-Lieve-Vrouw -  Secundair Onderwijs", "ZELE", 9240);
        schools[375] = new School(38489, "Pius X-Instituut", "ZELE", 9240);
        schools[376] = new School(38562, "Onze-Lieve-Vrouwcollege I", "ZOTTEGEM", 9620);
        schools[377] = new School(38596, "Provinciaal Technisch         Instituut", "ZOTTEGEM", 9620);
        schools[378] = new School(38604, "Don Boscocollege", "ZWIJNAARDE", 9052);
        schools[379] = new School(38653, "Instituut Stella Matutina", "BRAKEL", 9660);
        schools[380] = new School(38695, "Spectrumcollege Bovenbouw VTI", "BERINGEN", 3580);
        schools[381] = new School(38703, "Spectrumcollege Bovenbouw Sinte-Lutgart", "BERINGEN", 3580);
        schools[382] = new School(38711, "Spectrumcollege Bovenbouw Sint-Jozef", "BERINGEN", 3580);
        schools[383] = new School(38729, "Spectrumcollege Middenschool  Sint-Jan", "BERINGEN", 3580);
        schools[384] = new School(38761, "Technisch Instituut Sint-Jozef", "BILZEN", 3740);
        schools[385] = new School(38844, "Sint-Augustinusinstituut      BSO/TSO", "BREE", 3960);
        schools[386] = new School(38851, "Middenschool                  Heilig Hartinstituut", "BREE", 3960);
        schools[387] = new School(38885, "Provinciale Secundaire School", "DIEPENBEEK", 3590);
        schools[388] = new School(38919, "Provinciale Middenschool", "DIEPENBEEK", 3590);
        schools[389] = new School(38927, "Stedelijke Humaniora", "DILSEN-STOKKEM", 3650);
        schools[390] = new School(38935, "Instituut Maria Koningin", "DILSEN-STOKKEM", 3650);
        schools[391] = new School(38951, "Technisch Instituut           Sint-Lodewijk - vzw KASOG", "GENK", 3600);
        schools[392] = new School(39057, "Don Bosco Genk", "GENK", 3600);
        schools[393] = new School(39073, "WICO - 039073", "HAMONT-ACHEL", 3930);
        schools[394] = new School(39099, "Provinciale Middenschool      Hasselt", "HASSELT", 3500);
        schools[395] = new School(39107, "Technisch Instituut Heilig    Hart", "HASSELT", 3500);
        schools[396] = new School(39115, "Vrij Technisch Instituut", "HASSELT", 3500);
        schools[397] = new School(39115, "Vrij Technisch Instituut", "NEERPELT", 3910);
        schools[398] = new School(39263, "Provinciale Handelsschool     Hasselt", "HASSELT", 3500);
        schools[399] = new School(39271, "Humaniora Kindsheid Jesu", "HASSELT", 3500);
        schools[400] = new School(39289, "Don Bosco-College", "HECHTEL", 3940);
        schools[401] = new School(39305, "Don Bosco Technisch Instituut", "HELCHTEREN", 3530);
        schools[402] = new School(39313, "Sint-Martinusscholen 039313", "HERK-DE-STAD", 3540);
        schools[403] = new School(39321, "Sint-Martinusscholen 039321", "HERK-DE-STAD", 3540);
        schools[404] = new School(39479, "Harlindis en Relindis         Instituut Heilig Graf", "MAASEIK", 3680);
        schools[405] = new School(39503, "Heilig Hartcollege", "LANAKEN", 3620);
        schools[406] = new School(39511, "Technisch Instituut Sparrendal", "LANAKEN", 3620);
        schools[407] = new School(39529, "Sint-Vincentiusmiddenschool", "LANAKEN", 3620);
        schools[408] = new School(39545, "Bovenbouw Sint-Michiel", "LEOPOLDSBURG", 3970);
        schools[409] = new School(39552, "Sint-Michiel Middenschool", "LEOPOLDSBURG", 3970);
        schools[410] = new School(39561, "Provinciaal Instituut Lommel -Secundair Onderwijs PROVIL", "LOMMEL", 3920);
        schools[411] = new School(39611, "Spectrumcollege Bovenbouw OHvM", "LUMMEN", 3560);
        schools[412] = new School(39628, "Spectrumcollege Middenschool  OHvM", "LUMMEN", 3560);
        schools[413] = new School(39636, "Harlindis en Relindis         Technisch Instituut           Sint-Jansberg A", "MAASEIK", 3680);
        schools[414] = new School(39669, "campus de helix³", "MAASMECHELEN", 3630);
        schools[415] = new School(39677, "campus de helix²", "MAASMECHELEN", 3630);
        schools[416] = new School(39719, "Provinciale Technische School", "MAASMECHELEN", 3630);
        schools[417] = new School(39743, "Provinciale Secundaire School Bilzen", "BILZEN", 3740);
        schools[418] = new School(39826, "Spectrumcollege Middenschool  OLVI", "PAAL", 3583);
        schools[419] = new School(39842, "Instituut Agnetendal", "PEER", 3990);
        schools[420] = new School(39859, "Technicum", "SINT-TRUIDEN", 3800);
        schools[421] = new School(39925, "Instituut Mariaburcht -       Secundair Onderwijs", "STEVOORT", 3512);
        schools[422] = new School(39941, "Technisch Heilig Hartinstituut", "TESSENDERLO", 3980);
        schools[423] = new School(40055, "Provinciaal Instituut voor    Biotechnisch Onderwijs", "TONGEREN", 3700);
        schools[424] = new School(40097, "Vrije Middenschool 1", "ZONHOVEN", 3520);
        schools[425] = new School(40105, "Sint-Jan Berchmansinstituut", "ZONHOVEN", 3520);
        schools[426] = new School(40113, "Vrije Middenschool 2", "ZONHOVEN", 3520);
        schools[427] = new School(40121, "Provinciale Secundaire School te Voeren", "'S GRAVENVOEREN", 3798);
        schools[428] = new School(40204, "MS Jan Fevijn", "SINT-MICHIELS", 8200);
        schools[429] = new School(40204, "MS Jan Fevijn", "SINT-KRUIS", 8310);
        schools[430] = new School(40253, "GO! atheneum Beveren-Waas", "SINT-NIKLAAS", 9100);
        schools[431] = new School(40253, "GO! atheneum Beveren-Waas", "BEVEREN-WAAS", 9120);
        schools[432] = new School(40253, "GO! atheneum Beveren-Waas", "TEMSE", 9140);
        schools[433] = new School(40287, "GO! atheneum Antwerpen", "ANTWERPEN", 2060);
        schools[434] = new School(40311, "GO! Lyceum Antwerpen", "ANTWERPEN", 2018);
        schools[435] = new School(40411, "GO! atheneum Boom", "BOOM", 2850);
        schools[436] = new School(40428, "GO! middenschool Den Brandt Boom", "AARTSELAAR", 2630);
        schools[437] = new School(40428, "GO! middenschool Den Brandt Boom", "NIEL", 2845);
        schools[438] = new School(40428, "GO! middenschool Den Brandt Boom", "BOOM", 2850);
        schools[439] = new School(40469, "GO! atheneum Brasschaat", "BRASSCHAAT", 2930);
        schools[440] = new School(40477, "GO! technisch atheneum Brasschaat", "BRASSCHAAT", 2930);
        schools[441] = new School(40485, "GO! middenschool Brasschaat", "BRASSCHAAT", 2930);
        schools[442] = new School(40519, "GO! atheneum Deurne", "DEURNE", 2100);
        schools[443] = new School(40584, "GO! technisch atheneum        Da Vinci Edegem", "LIER", 2500);
        schools[444] = new School(40584, "GO! technisch atheneum        Da Vinci Edegem", "EDEGEM", 2650);
        schools[445] = new School(40601, "GO! middenschool Ekeren", "EKEREN", 2180);
        schools[446] = new School(40618, "GO! atheneum Ekeren", "EKEREN", 2180);
        schools[447] = new School(40626, "GO! Erasmusatheneum Essen-    Kalmthout", "ESSEN", 2910);
        schools[448] = new School(40626, "GO! Erasmusatheneum Essen-    Kalmthout", "KALMTHOUT", 2920);
        schools[449] = new School(40634, "GO! middenschool Geel", "GEEL", 2440);
        schools[450] = new School(40642, "GO! atheneum Geel", "WESTERLO", 2260);
        schools[451] = new School(40642, "GO! atheneum Geel", "GEEL", 2440);
        schools[452] = new School(40709, "GO! middenschool De Vesten Herentals", "HERENTALS", 2200);
        schools[453] = new School(40717, "GO! atheneum De Vesten Herentals", "HERENTALS", 2200);
        schools[454] = new School(40774, "GO! atheneum Irishof Kapellen", "KALMTHOUT", 2920);
        schools[455] = new School(40774, "GO! atheneum Irishof Kapellen", "KAPELLEN", 2950);
        schools[456] = new School(40782, "GO! technisch atheneum Kapellen", "STABROEK", 2940);
        schools[457] = new School(40782, "GO! technisch atheneum Kapellen", "KAPELLEN", 2950);
        schools[458] = new School(40791, "GO! middenschool Kapellen", "KAPELLEN", 2950);
        schools[459] = new School(40808, "GO! Atheneum Arthur Vanderpoorten Lier", "LIER", 2500);
        schools[460] = new School(40808, "GO! Atheneum Arthur Vanderpoorten Lier", "MORTSEL", 2640);
        schools[461] = new School(40816, "GO! middenschool Anton Bergmann Lier", "LIER", 2500);
        schools[462] = new School(40832, "GO! technisch atheneum 't SpuiLier", "LIER", 2500);
        schools[463] = new School(40857, "GO! atheneum Pitzemburg Mechelen", "MECHELEN", 2800);
        schools[464] = new School(40873, "GO! Lyceum Mechelen", "MECHELEN", 2800);
        schools[465] = new School(40907, "GO! Paramedisch Instituut     Mechelen", "JETTE", 1090);
        schools[466] = new School(40907, "GO! Paramedisch Instituut     Mechelen", "MECHELEN", 2800);
        schools[467] = new School(40923, "GO! atheneum Merksem", "MERKSEM", 2170);
        schools[468] = new School(40949, "GO! atheneum Mol", "MOL", 2400);
        schools[469] = new School(40956, "GO! middenschool Mol", "MOL", 2400);
        schools[470] = new School(40964, "GO! technisch atheneum Mol", "MOL", 2400);
        schools[471] = new School(40972, "GO! atheneum Mortsel", "MORTSEL", 2640);
        schools[472] = new School(41004, "GO! technisch atheneum Den    Biezerd", "NIEL", 2845);
        schools[473] = new School(41021, "GO! middenschool Malle", "WESTMALLE", 2390);
        schools[474] = new School(41038, "GO! atheneum Malle", "WESTMALLE", 2390);
        schools[475] = new School(41137, "Talentenschool Turnhout campusZenit", "TURNHOUT", 2300);
        schools[476] = new School(41145, "Talentenschool Turnhout campusBoomgaard TA met Hotelschool", "TURNHOUT", 2300);
        schools[477] = new School(41152, "Talentenschool Turnhout campusBoomgaard MS", "TURNHOUT", 2300);
        schools[478] = new School(41178, "GO! technisch atheneum De Beeltjens Westerlo", "WESTERLO", 2260);
        schools[479] = new School(41178, "GO! technisch atheneum De Beeltjens Westerlo", "GEEL", 2440);
        schools[480] = new School(41194, "GO! Atheneum Willebroek", "WILLEBROEK", 2830);
        schools[481] = new School(41202, "GO! Atheneum Klein-Brabant", "PUURS", 2870);
        schools[482] = new School(41202, "GO! Atheneum Klein-Brabant", "BORNEM", 2880);
        schools[483] = new School(41236, "GO! atheneum Aarschot", "AARSCHOT", 3200);
        schools[484] = new School(41236, "GO! atheneum Aarschot", "DIEST", 3290);
        schools[485] = new School(41236, "GO! atheneum Aarschot", "TESSENDERLO", 3980);
        schools[486] = new School(41244, "GO! middenschool Aarschot", "AARSCHOT", 3200);
        schools[487] = new School(41301, "GO! atheneum campus Vijverbeek Asse", "ASSE", 1730);
        schools[488] = new School(41319, "GO! middenschool campus Vijverbeek Asse", "ASSE", 1730);
        schools[489] = new School(41368, "GO! lyceum Brussel Martha Somers", "LAKEN", 1020);
        schools[490] = new School(41426, "GO! atheneum Prins Van Oranje Diest", "DIEST", 3290);
        schools[491] = new School(41467, "GO! technisch atheneum 1 Diest", "DIEST", 3290);
        schools[492] = new School(41475, "GO! middenschool Prins Van Oranje Diest", "DIEST", 3290);
        schools[493] = new School(41483, "GO! atheneum Etterbeek", "ETTERBEEK", 1040);
        schools[494] = new School(41533, "GO! atheneum Halle", "HALLE", 1500);
        schools[495] = new School(41541, "GO! middenschool Halle", "HALLE", 1500);
        schools[496] = new School(41558, "GO! technisch atheneum Pro Technica Halle", "HALLE", 1500);
        schools[497] = new School(41558, "GO! technisch atheneum Pro Technica Halle", "LENNIK", 1750);
        schools[498] = new School(41574, "GO! technisch atheneum Jette", "JETTE", 1090);
        schools[499] = new School(41591, "GO! middenschool Keerbergen", "KEERBERGEN", 3140);
        schools[500] = new School(41608, "GO! atheneum Keerbergen", "KEERBERGEN", 3140);
        schools[501] = new School(41632, "GO! atheneum Unescoschool Koekelberg", "KOEKELBERG", 1081);
        schools[502] = new School(41665, "GO! middenschool 1 Leuven", "LEUVEN", 3000);
        schools[503] = new School(41673, "GO! technisch atheneum Liedekerke", "LIEDEKERKE", 1770);
        schools[504] = new School(41699, "GO! atheneum Sint-Jans-Molenbeek", "ANDERLECHT", 1070);
        schools[505] = new School(41699, "GO! atheneum Sint-Jans-Molenbeek", "SINT-JANS-MOLENBEEK", 1080);
        schools[506] = new School(41756, "GO! atheneum Emanuel Hiel Schaarbeek-Evere", "SCHAARBEEK", 1030);
        schools[507] = new School(41756, "GO! atheneum Emanuel Hiel Schaarbeek-Evere", "EVERE", 1140);
        schools[508] = new School(41764, "GO! technisch atheneum Zavelenberg Sint-Agatha-Berchem", "SINT-AGATHA-BERCHEM", 1082);
        schools[509] = new School(41781, "GO! middenschool Sigo Lennik", "LENNIK", 1750);
        schools[510] = new School(41863, "GO! atheneum Ukkel", "UKKEL", 1180);
        schools[511] = new School(41871, "GO! middenschool Ukkel", "UKKEL", 1180);
        schools[512] = new School(41897, "GO! technisch atheneum Horteco Vilvoorde", "VILVOORDE", 1800);
        schools[513] = new School(41921, "GO! technisch atheneum Campus Wemmel", "WEMMEL", 1780);
        schools[514] = new School(41939, "GO! middenschool Campus Wemmel", "WEMMEL", 1780);
        schools[515] = new School(41954, "GO! atheneum Sint-Pieters-Woluwe", "BRUSSEL-STAD", 1000);
        schools[516] = new School(41954, "GO! atheneum Sint-Pieters-Woluwe", "SINT-PIETERS-WOLUWE", 1150);
        schools[517] = new School(42002, "GO! atheneum d' hek Landen", "LANDEN", 3400);
        schools[518] = new School(42011, "GO! Middenschool D'hek Landen", "LANDEN", 3400);
        schools[519] = new School(42036, "GO! atheneum Avelgem", "AVELGEM", 8580);
        schools[520] = new School(42044, "GO! middenschool Avelgem", "AVELGEM", 8580);
        schools[521] = new School(42069, "GO! middenschool Maerlant Blankenberge", "BLANKENBERGE", 8370);
        schools[522] = new School(42085, "GO! atheneum Maerlant Blankenberge", "BLANKENBERGE", 8370);
        schools[523] = new School(42085, "GO! atheneum Maerlant Blankenberge", "DE HAAN", 8420);
        schools[524] = new School(42119, "GO! atheneum 1 Brugge -centrum", "BRUGGE", 8000);
        schools[525] = new School(42151, "KA Jan Fevijn", "SINT-MICHIELS", 8200);
        schools[526] = new School(42151, "KA Jan Fevijn", "SINT-KRUIS", 8310);
        schools[527] = new School(42201, "GO! technisch atheneum Brugge", "SINT-MICHIELS", 8200);
        schools[528] = new School(42218, "GO! middenschool Brugge-centrum", "BRUGGE", 8000);
        schools[529] = new School(42267, "GO! technisch atheneum Diksmuide", "DIKSMUIDE", 8600);
        schools[530] = new School(42267, "GO! technisch atheneum Diksmuide", "NIEUWPOORT", 8620);
        schools[531] = new School(42267, "GO! technisch atheneum Diksmuide", "DE PANNE", 8660);
        schools[532] = new School(42283, "GO! technisch atheneum Gistel", "GISTEL", 8470);
        schools[533] = new School(42283, "GO! technisch atheneum Gistel", "KOEKELARE", 8680);
        schools[534] = new School(42325, "GO! technisch atheneum 2 Heule", "HEULE", 8501);
        schools[535] = new School(42333, "GO! atheneum Ieper", "IEPER", 8900);
        schools[536] = new School(42341, "GO! middenschool Ieper", "DE PANNE", 8660);
        schools[537] = new School(42341, "GO! middenschool Ieper", "IEPER", 8900);
        schools[538] = new School(42366, "GO! technisch atheneum Ieper", "WAREGEM", 8790);
        schools[539] = new School(42366, "GO! technisch atheneum Ieper", "IEPER", 8900);
        schools[540] = new School(42374, "GO! Atheneum Bellevue Izegem", "IZEGEM", 8870);
        schools[541] = new School(42408, "GO! atheneum Zwinstede Knokke", "KNOKKE-HEIST", 8300);
        schools[542] = new School(42416, "GO! middenschool Zwinstede Knokke", "KNOKKE-HEIST", 8300);
        schools[543] = new School(42441, "GO!Da Vinci Atheneum Koekelare", "GISTEL", 8470);
        schools[544] = new School(42441, "GO!Da Vinci Atheneum Koekelare", "KOEKELARE", 8680);
        schools[545] = new School(42465, "GO! Atheneum Pottelberg 2de en3de graad", "KORTRIJK", 8500);
        schools[546] = new School(42499, "GO! technisch atheneum Drie hofsteden Kortrijk", "KORTRIJK", 8500);
        schools[547] = new School(42515, "GO! Atheneum Pottelberg 1ste  graad", "KORTRIJK", 8500);
        schools[548] = new School(42523, "GO! middenschool 2 Kortrijk", "KORTRIJK", 8500);
        schools[549] = new School(42523, "GO! middenschool 2 Kortrijk", "HEULE", 8501);
        schools[550] = new School(42531, "Grenslandscholen Menen en     Wervik", "ROESELARE", 8800);
        schools[551] = new School(42531, "Grenslandscholen Menen en     Wervik", "MENEN", 8930);
        schools[552] = new School(42531, "Grenslandscholen Menen en     Wervik", "WERVIK", 8940);
        schools[553] = new School(42556, "Grenslandscholen Menen en     Wervik", "WEVELGEM", 8560);
        schools[554] = new School(42556, "Grenslandscholen Menen en     Wervik", "MENEN", 8930);
        schools[555] = new School(42556, "Grenslandscholen Menen en     Wervik", "WERVIK", 8940);
        schools[556] = new School(42581, "GO! middenschool De vierboete Nieuwpoort", "NIEUWPOORT", 8620);
        schools[557] = new School(42581, "GO! middenschool De vierboete Nieuwpoort", "DE PANNE", 8660);
        schools[558] = new School(42622, "GO! technisch atheneum Vesaliusinstituut Oostende", "SINT-MICHIELS", 8200);
        schools[559] = new School(42622, "GO! technisch atheneum Vesaliusinstituut Oostende", "OOSTENDE", 8400);
        schools[560] = new School(42622, "GO! technisch atheneum Vesaliusinstituut Oostende", "BREDENE", 8450);
        schools[561] = new School(42622, "GO! technisch atheneum Vesaliusinstituut Oostende", "GENT", 9000);
        schools[562] = new School(42622, "GO! technisch atheneum Vesaliusinstituut Oostende", "RONSE", 9600);
        schools[563] = new School(42648, "GO! atheneum 2 Oostende", "OOSTENDE", 8400);
        schools[564] = new School(42689, "GO! technisch atheneum De Panne", "NIEUWPOORT", 8620);
        schools[565] = new School(42689, "GO! technisch atheneum De Panne", "DE PANNE", 8660);
        schools[566] = new School(42739, "MSKA Atheneum Roeselare", "ROESELARE", 8800);
        schools[567] = new School(42739, "MSKA Atheneum Roeselare", "IZEGEM", 8870);
        schools[568] = new School(42739, "MSKA Atheneum Roeselare", "MENEN", 8930);
        schools[569] = new School(42754, "MSKA Middenschool Roeselare", "ROESELARE", 8800);
        schools[570] = new School(42762, "GO! atheneum Campus De Reynaert Tielt", "TIELT", 8700);
        schools[571] = new School(42762, "GO! atheneum Campus De Reynaert Tielt", "WAREGEM", 8790);
        schools[572] = new School(42762, "GO! atheneum Campus De Reynaert Tielt", "DEINZE", 9800);
        schools[573] = new School(42762, "GO! atheneum Campus De Reynaert Tielt", "AALTER", 9880);
        schools[574] = new School(42796, "GO! technisch atheneum Houtlandinstituut Torhout", "TORHOUT", 8820);
        schools[575] = new School(42812, "GO! atheneum Veurne Centrum", "VEURNE", 8630);
        schools[576] = new School(42846, "Secundair Onderwijs Groenhove Campus Middenschool", "WAREGEM", 8790);
        schools[577] = new School(42853, "Secundair Onderwijs Groenhove Campus Atheneum", "TIELT", 8700);
        schools[578] = new School(42853, "Secundair Onderwijs Groenhove Campus Atheneum", "WAREGEM", 8790);
        schools[579] = new School(42853, "Secundair Onderwijs Groenhove Campus Atheneum", "DEINZE", 9800);
        schools[580] = new School(42853, "Secundair Onderwijs Groenhove Campus Atheneum", "AALTER", 9880);
        schools[581] = new School(42929, "GO! atheneum 1 Aalst", "AALST", 9300);
        schools[582] = new School(42952, "GO! lyceum Aalst", "AALST", 9300);
        schools[583] = new School(42961, "GO! technisch atheneum Handelsschool Aalst", "AALST", 9300);
        schools[584] = new School(42994, "Technisch Atheneum            De Voorstad", "AALST", 9300);
        schools[585] = new School(43018, "Middenschool TechniGO!", "AALST", 9300);
        schools[586] = new School(43026, "GO! technisch atheneum Ledebaan Aalst", "AALST", 9300);
        schools[587] = new School(43042, "GO! middenschool De Beuk Aalter", "TIELT", 8700);
        schools[588] = new School(43042, "GO! middenschool De Beuk Aalter", "WAREGEM", 8790);
        schools[589] = new School(43042, "GO! middenschool De Beuk Aalter", "DEINZE", 9800);
        schools[590] = new School(43042, "GO! middenschool De Beuk Aalter", "AALTER", 9880);
        schools[591] = new School(43117, "GO! atheneum Denderleeuw", "LIEDEKERKE", 1770);
        schools[592] = new School(43117, "GO! atheneum Denderleeuw", "DENDERLEEUW", 9470);
        schools[593] = new School(43141, "GO! atheneum Dendermonde", "DENDERMONDE", 9200);
        schools[594] = new School(43166, "GO! technisch atheneum Dendermonde", "DENDERMONDE", 9200);
        schools[595] = new School(43174, "GO! middenschool 1 Dendermonde", "DENDERMONDE", 9200);
        schools[596] = new School(43182, "GO! middenschool Zwijveke Dendermonde", "DENDERMONDE", 9200);
        schools[597] = new School(43216, "Einstein Atheneum,ASO Talen,  Wetenschappen & Kunst", "EVERGEM", 9940);
        schools[598] = new School(43241, "GO! atheneum Geraardsbergen", "GERAARDSBERGEN", 9500);
        schools[599] = new School(43257, "GO! middenschool Geraardsbergen", "GERAARDSBERGEN", 9500);
        schools[600] = new School(43257, "GO! middenschool Geraardsbergen", "BRAKEL", 9660);
        schools[601] = new School(43273, "GO! atheneum Voskenslaan Gent", "GENT", 9000);
        schools[602] = new School(43299, "GO! lyceum Gent", "GENT", 9000);
        schools[603] = new School(43299, "GO! lyceum Gent", "MERELBEKE", 9820);
        schools[604] = new School(43307, "GO! technisch atheneum 1 Gent", "GENT", 9000);
        schools[605] = new School(43307, "GO! technisch atheneum 1 Gent", "DRONGEN", 9031);
        schools[606] = new School(43356, "GO! middenschool 2 Gent", "GENT", 9000);
        schools[607] = new School(43406, "GO! middenschool De Veerman Hamme", "HAMME", 9220);
        schools[608] = new School(43513, "GO! technisch atheneum Ohrem Lokeren", "LOKEREN", 9160);
        schools[609] = new School(43521, "GO! middenschool Mevrouw Courtmans Maldegem", "MALDEGEM", 9990);
        schools[610] = new School(43539, "GO! atheneum Mevrouw Courtmans Maldegem", "MALDEGEM", 9990);
        schools[611] = new School(43554, "GO! atheneum Mariakerke", "MARIAKERKE", 9030);
        schools[612] = new School(43562, "GO! technisch atheneum Tuinbouwschool Melle", "MELLE", 9090);
        schools[613] = new School(43588, "GO! middenschool De Moerbei Moerbeke-Waas", "MOERBEKE-WAAS", 9180);
        schools[614] = new School(43596, "GO! atheneum Ninove", "NINOVE", 9400);
        schools[615] = new School(43604, "GO! middenschool Ninove", "NINOVE", 9400);
        schools[616] = new School(43729, "GO! atheneum Campus Parklaan Sint-Niklaas", "SINT-NIKLAAS", 9100);
        schools[617] = new School(43786, "Lyceum Aan De Stroom", "TEMSE", 9140);
        schools[618] = new School(43802, "GO! atheneum Campus Noordlaan Wetteren", "WETTEREN", 9230);
        schools[619] = new School(43836, "Campus Kompas Middenschool", "WETTEREN", 9230);
        schools[620] = new School(43869, "GO! atheneum Zelzate", "ZELZATE", 9060);
        schools[621] = new School(43885, "GO! middenschool Zelzate", "ZELZATE", 9060);
        schools[622] = new School(43927, "GO! technisch atheneum De Rijdtmeersen Brakel", "BRAKEL", 9660);
        schools[623] = new School(43968, "GO! Atheneum Martinus Bilzen", "TONGEREN", 3700);
        schools[624] = new School(43968, "GO! Atheneum Martinus Bilzen", "BILZEN", 3740);
        schools[625] = new School(43992, "GO! Atheneum Bree", "BREE", 3960);
        schools[626] = new School(44016, "GO! atheneum Maasland-Campus Dilsen-Stokkem", "MAASMECHELEN", 3630);
        schools[627] = new School(44016, "GO! atheneum Maasland-Campus Dilsen-Stokkem", "DILSEN-STOKKEM", 3650);
        schools[628] = new School(44041, "GO! campus Genk Middenschool", "GENK", 3600);
        schools[629] = new School(44057, "GO Campus Genk ALTEA", "GENK", 3600);
        schools[630] = new School(44073, "GO campus Genk Technisch Atheneum De Wijzer", "GENK", 3600);
        schools[631] = new School(44081, "GO! atheneum 1 Hasselt", "HASSELT", 3500);
        schools[632] = new School(44107, "GO! middenschool 3 Hasselt", "HASSELT", 3500);
        schools[633] = new School(44123, "GO! technisch atheneum 1 Hasselt", "HASSELT", 3500);
        schools[634] = new School(44156, "GO! technisch atheneum Villers Hasselt", "HASSELT", 3500);
        schools[635] = new School(44156, "GO! technisch atheneum Villers Hasselt", "HERK-DE-STAD", 3540);
        schools[636] = new School(44172, "GO! atheneum sporthumaniora Hasselt", "HASSELT", 3500);
        schools[637] = new School(44181, "GO! middenschool 2 Herk-de-stad", "HASSELT", 3500);
        schools[638] = new School(44181, "GO! middenschool 2 Herk-de-stad", "HERK-DE-STAD", 3540);
        schools[639] = new School(44263, "GO! technisch atheneum Lanaken", "LANAKEN", 3620);
        schools[640] = new School(44289, "GO! atheneum Leopoldsburg", "LEOPOLDSBURG", 3970);
        schools[641] = new School(44297, "GO! middenschool Leopoldsburg", "LEOPOLDSBURG", 3970);
        schools[642] = new School(44313, "GO! middenschool Mercurius Lommel", "LOMMEL", 3920);
        schools[643] = new School(44321, "GO! atheneum Mercurius Lommel", "LOMMEL", 3920);
        schools[644] = new School(44347, "GO! atheneum Maaseik", "MAASEIK", 3680);
        schools[645] = new School(44347, "GO! atheneum Maaseik", "BREE", 3960);
        schools[646] = new School(44362, "GO! technisch atheneum Maaseik", "MAASEIK", 3680);
        schools[647] = new School(44371, "GO! middenschool Maaseik", "MAASEIK", 3680);
        schools[648] = new School(44388, "GO! atheneum Maasland-Campus Maasmechelen", "MAASMECHELEN", 3630);
        schools[649] = new School(44388, "GO! atheneum Maasland-Campus Maasmechelen", "DILSEN-STOKKEM", 3650);
        schools[650] = new School(44412, "GO! Atheneum Overpelt", "OVERPELT", 3900);
        schools[651] = new School(44438, "GO! middenschool De Wingerd Overpelt", "OVERPELT", 3900);
        schools[652] = new School(44446, "GO! atheneum Sint-Truiden", "SINT-TRUIDEN", 3800);
        schools[653] = new School(44453, "GO! middenschool 1 Sint-Truiden", "SINT-TRUIDEN", 3800);
        schools[654] = new School(44487, "GO! technisch atheneum Domein Speelhof Sint-Truiden", "SINT-TRUIDEN", 3800);
        schools[655] = new School(44487, "GO! technisch atheneum Domein Speelhof Sint-Truiden", "BORGLOON", 3840);
        schools[656] = new School(44495, "GO! middenschool 2 Borgloon   Domein Speelhof", "SINT-TRUIDEN", 3800);
        schools[657] = new School(44495, "GO! middenschool 2 Borgloon   Domein Speelhof", "BORGLOON", 3840);
        schools[658] = new School(44503, "GO! middenschool Tessenderlo", "TESSENDERLO", 3980);
        schools[659] = new School(44511, "GO! atheneum Tessenderlo", "TESSENDERLO", 3980);
        schools[660] = new School(44537, "GO! atheneum Tongeren", "TONGEREN", 3700);
        schools[661] = new School(44537, "GO! atheneum Tongeren", "BILZEN", 3740);
        schools[662] = new School(44537, "GO! atheneum Tongeren", "BORGLOON", 3840);
        schools[663] = new School(44552, "GO! technisch atheneum Campus Plinius Tongeren", "TONGEREN", 3700);
        schools[664] = new School(44552, "GO! technisch atheneum Campus Plinius Tongeren", "BILZEN", 3740);
        schools[665] = new School(44669, "GO! middenschool Voskenslaan Gent", "GENT", 9000);
        schools[666] = new School(44727, "Virgo Fidelisinstituut Eerste graad", "VILVOORDE", 1800);
        schools[667] = new School(46391, "GO! atheneum Schoten", "SCHOTEN", 2900);
        schools[668] = new School(46391, "GO! atheneum Schoten", "SCHILDE", 2970);
        schools[669] = new School(46409, "GO! technisch atheneum 2 Diest", "DIEST", 3290);
        schools[670] = new School(46813, "Sint-Annacollege -Middenschool", "ANTWERPEN", 2050);
        schools[671] = new School(46821, "Vrije Israelitische school    voor Secundair Onderwijs Yavne", "ANTWERPEN", 2018);
        schools[672] = new School(46854, "Prizma - Middenschool Izegem 2", "IZEGEM", 8870);
        schools[673] = new School(46862, "Provinciale Middenschool", "GENT", 9000);
        schools[674] = new School(46871, "IVG-School", "GENT", 9000);
        schools[675] = new School(47209, "Instituut Sint-Lutgardis", "ZOMERGEM", 9930);
        schools[676] = new School(47217, "Sint-Vincentiuscollege -      Middenschool", "ZOMERGEM", 9930);
        schools[677] = new School(47225, "Sint-Jozefinstituut", "GENK", 3600);
        schools[678] = new School(47258, "Scheppersinstituut 1          Deurne & Antwerpen", "ANTWERPEN", 2060);
        schools[679] = new School(47258, "Scheppersinstituut 1          Deurne & Antwerpen", "DEURNE", 2100);
        schools[680] = new School(47282, "Sint-Annacollege", "ANTWERPEN", 2050);
        schools[681] = new School(47316, "Middelbare Rudolf             Steinerschool Vlaanderen", "LIER", 2500);
        schools[682] = new School(47316, "Middelbare Rudolf             Steinerschool Vlaanderen", "BERCHEM", 2600);
        schools[683] = new School(47316, "Middelbare Rudolf             Steinerschool Vlaanderen", "WIJGMAAL", 3018);
        schools[684] = new School(47316, "Middelbare Rudolf             Steinerschool Vlaanderen", "SINT-KRUIS", 8310);
        schools[685] = new School(47316, "Middelbare Rudolf             Steinerschool Vlaanderen", "GENT", 9000);
        schools[686] = new School(47589, "Sint-Jozefinstituut", "WETTEREN", 9230);
        schools[687] = new School(47597, "Moretus 2", "EKEREN", 2180);
        schools[688] = new School(47886, "Biotechnicum", "BOCHOLT", 3950);
        schools[689] = new School(47894, "Berthoutinstituut-Klein       Seminarie 2", "MECHELEN", 2800);
        schools[690] = new School(47944, "GO! atheneum en leefschool De Tandem Eeklo", "EEKLO", 9900);
        schools[691] = new School(48025, "Instituut voor Katholiek      Secundair Onderwijs", "DENDERLEEUW", 9470);
        schools[692] = new School(48033, "Instituut Zusters Maricolen - Middenschool", "MALDEGEM", 9990);
        schools[693] = new School(48066, "Pius X - Middenschool", "TESSENDERLO", 3980);
        schools[694] = new School(48074, "Pius X - College", "TESSENDERLO", 3980);
        schools[695] = new School(48091, "Stedelijke Handelsschool      Turnhout", "TURNHOUT", 2300);
        schools[696] = new School(48108, "H. Pius X-instituut -         Middenschool", "ANTWERPEN", 2020);
        schools[697] = new School(48397, "Sint-Lambertus 5", "WESTERLO", 2260);
        schools[698] = new School(48652, "GO! technisch atheneum 3 Hasselt", "HASSELT", 3500);
        schools[699] = new School(48728, "Sint-Willebrord-H.Familie", "BERCHEM", 2600);
        schools[700] = new School(48769, "Brood- en banketbakkerijschoolTer Groene Poorte", "SINT-MICHIELS", 8200);
        schools[701] = new School(48967, "KSO Glorieux", "RONSE", 9600);
        schools[702] = new School(48975, "Vrij Instituut voor Secundair Onderwijs Cor Mariae", "BRAKEL", 9660);
        schools[703] = new School(48991, "Gitok eerste graad", "KALMTHOUT", 2920);
        schools[704] = new School(49023, "GO! kunsthumaniora Brussel-Stad", "LAKEN", 1020);
        schools[705] = new School(49189, "Sint-Lukas Kunsthumaniora", "SCHAARBEEK", 1030);
        schools[706] = new School(49445, "de]Kunsthumaniora", "ANTWERPEN", 2018);
        schools[707] = new School(49445, "de]Kunsthumaniora", "WILRIJK", 2610);
        schools[708] = new School(50096, "Provinciale Secundaire School Hasselt", "HASSELT", 3500);
        schools[709] = new School(50161, "Kunstschool Genk - vzw KASOG", "GENK", 3600);
        schools[710] = new School(50336, "Stedelijke Academie voor      Schone Kunsten - KSO", "BRUGGE", 8000);
        schools[711] = new School(50609, "MUDA Atheneum voor Podiumkunsten", "EVERGEM", 9940);
        schools[712] = new School(50633, "Secundair Kunstinstituut", "GENT", 9000);
        schools[713] = new School(50658, "Kunsthumaniora Sint-Lucas", "GENT", 9000);
        schools[714] = new School(51003, "Lemmensinstituut Secundair    Onderwijs", "LEUVEN", 3000);
        schools[715] = new School(51086, "Stedelijke Academie voor      Beeldende Kunsten", "AALST", 9300);
        schools[716] = new School(53124, "Koninklijk Werk IBIS", "BREDENE", 8450);
        schools[717] = new School(53173, "OLV Pulhof", "BERCHEM", 2600);
        schools[718] = new School(53331, "H.Pius X-instituut - Bovenbouw", "ANTWERPEN", 2020);
        schools[719] = new School(55913, "Secundaire Handelsschool      Sint-Lodewijk", "ANTWERPEN", 2000);
        schools[720] = new School(60831, "GO! technisch atheneum Wollemarkt Mechelen", "MECHELEN", 2800);
        schools[721] = new School(60848, "Instituut Sancta Maria", "RUISELEDE", 8755);
        schools[722] = new School(61929, "Sint-Elisabeth-Instituut", "MERKSEM", 2170);
        schools[723] = new School(61937, "Stella Matutinacollege", "LEDE", 9340);
        schools[724] = new School(62091, "GO! atheneum Hoboken", "HOBOKEN", 2660);
        schools[725] = new School(62141, "Sint-Maarten Middenschool", "BEVEREN-WAAS", 9120);
        schools[726] = new School(62158, "Sint-Maarten Bovenschool", "BEVEREN-WAAS", 9120);
        schools[727] = new School(103391, "Leerwijzer", "KOKSIJDE", 8670);
        schools[728] = new School(103424, "Eureka-Onderwijs", "KESSEL-LO", 3010);
        schools[729] = new School(104141, "Hogere Beroepsopleiding voor  Verpleegkunde                 Sint-Jan - Sint-Jozef", "BRUGGE", 8000);
        schools[730] = new School(104141, "Hogere Beroepsopleiding voor  Verpleegkunde                 Sint-Jan - Sint-Jozef", "SINT-MICHIELS", 8200);
        schools[731] = new School(104141, "Hogere Beroepsopleiding voor  Verpleegkunde                 Sint-Jan - Sint-Jozef", "OOSTENDE", 8400);
        schools[732] = new School(104166, "Sint-Jan Berchmansinstituut   ASO-TSO-BSO", "PUURS", 2870);
        schools[733] = new School(104174, "Sint-Jan Berchmansinstituut   Eerste graad", "PUURS", 2870);
        schools[734] = new School(104182, "ZAVO", "ZAVENTEM", 1930);
        schools[735] = new School(104257, "GO! Maritiem Instituut Oostende", "OOSTENDE", 8400);
        schools[736] = new School(105395, "Onze-Lieve-Vrouwe-Instituut", "GENT", 9000);
        schools[737] = new School(105403, "Groenendaalcollege", "MERKSEM", 2170);
        schools[738] = new School(105411, "Sint-Bernarduscollege", "NIEUWPOORT", 8620);
        schools[739] = new School(105486, "Sint-Laurensinstituut - ASO", "ZELZATE", 9060);
        schools[740] = new School(105486, "Sint-Laurensinstituut - ASO", "WACHTEBEKE", 9185);
        schools[741] = new School(105494, "Technisch Instituut           Sint-Laurens", "ZELZATE", 9060);
        schools[742] = new School(107581, "Sint-Aloysiuscollege", "NINOVE", 9400);
        schools[743] = new School(107599, "Sint-Gertrudiscollege", "WETTEREN", 9230);
        schools[744] = new School(107607, "Leiepoort Deinze campus Sint- Vincentius", "DEINZE", 9800);
        schools[745] = new School(107615, "Leiepoort Deinze campus Sint- Hendrik, bovenbouw", "DEINZE", 9800);
        schools[746] = new School(107664, "Harlindis en Relindis         College Heilig Kruis - Sint-  Ursula A", "MAASEIK", 3680);
        schools[747] = new School(107672, "Harlindis en Relindis E.G.S.1", "MAASEIK", 3680);
        schools[748] = new School(107706, "GO! atheneum Tervuren", "TERVUREN", 3080);
        schools[749] = new School(109843, "Sint-Ursula-Instituut", "WILRIJK", 2610);
        schools[750] = new School(109892, "Technische Scholen Mechelen", "MECHELEN", 2800);
        schools[751] = new School(109959, "Sint-Lievenscollege", "ANTWERPEN", 2000);
        schools[752] = new School(109975, "Sint-Jozefscollege", "HERENTALS", 2200);
        schools[753] = new School(109983, "Katholiek Scholencentrum JOMA", "MERKSEM", 2170);
        schools[754] = new School(109991, "campus de helix¹", "MAASMECHELEN", 3630);
        schools[755] = new School(110007, "Instituut Mariawende-Blydhove", "SINT-KRUIS", 8310);
        schools[756] = new School(110015, "Leielandscholen Campus Sint-  Niklaas Gemeente Zwevegem 1", "ZWEVEGEM", 8550);
        schools[757] = new School(110031, "Stedelijk Lyceum Meir", "ANTWERPEN", 2000);
        schools[758] = new School(110247, "Regina Mundi - vzw KASOG", "GENK", 3600);
        schools[759] = new School(110312, "GO! technisch atheneum Zwijndrecht", "ZWIJNDRECHT", 2070);
        schools[760] = new School(110321, "VIA-3", "TIENEN", 3300);
        schools[761] = new School(110338, "VIA-1", "TIENEN", 3300);
        schools[762] = new School(110346, "EDUGO campus Glorieux         Technisch Instituut", "OOSTAKKER", 9041);
        schools[763] = new School(110379, "TSM Middenschool", "MECHELEN", 2800);
        schools[764] = new School(110395, "Vrij Technisch Instituut - 2", "AALST", 9300);
        schools[765] = new School(111741, "Sint-Agnesinstituut", "HOBOKEN", 2660);
        schools[766] = new School(111757, "Sint-Agnesinstituut Middenschool", "HOBOKEN", 2660);
        schools[767] = new School(111765, "Technicum Noord-Antwerpen     Bovenbouwschool", "ANTWERPEN", 2000);
        schools[768] = new School(111807, "Virga Jessecollege", "HASSELT", 3500);
        schools[769] = new School(111823, "Harlindis en Relindis E.G.S.3", "KINROOI", 3640);
        schools[770] = new School(111823, "Harlindis en Relindis E.G.S.3", "MAASEIK", 3680);
        schools[771] = new School(111831, "Technisch Instituut           Sint-Lodewijk-1- vzw KASOG", "GENK", 3600);
        schools[772] = new School(111906, "V.T.I. 2", "ROESELARE", 8800);
        schools[773] = new School(111922, "Vrij Technisch Instituut 1", "KORTRIJK", 8500);
        schools[774] = new School(111931, "Vrij Technisch Instituut 2", "KORTRIJK", 8500);
        schools[775] = new School(111931, "Vrij Technisch Instituut 2", "WEVELGEM", 8560);
        schools[776] = new School(111948, "Vrij Technisch Instituut 3", "KORTRIJK", 8500);
        schools[777] = new School(111948, "Vrij Technisch Instituut 3", "WEVELGEM", 8560);
        schools[778] = new School(112011, "De Bron", "TIELT", 8700);
        schools[779] = new School(112037, "Onze-Lieve-Vrouw Ter Duinen 2", "HEIST-AAN-ZEE", 8301);
        schools[780] = new School(112037, "Onze-Lieve-Vrouw Ter Duinen 2", "ZEEBRUGGE", 8380);
        schools[781] = new School(112045, "Onze-Lieve-Vrouw Ter Duinen 1", "HEIST-AAN-ZEE", 8301);
        schools[782] = new School(112045, "Onze-Lieve-Vrouw Ter Duinen 1", "ZEEBRUGGE", 8380);
        schools[783] = new School(112052, "Middenschool Sint-Rembert 1", "LICHTERVELDE", 8810);
        schools[784] = new School(112052, "Middenschool Sint-Rembert 1", "TORHOUT", 8820);
        schools[785] = new School(112061, "VTI Ieper eerste graad", "IEPER", 8900);
        schools[786] = new School(112078, "Vrij Technisch Instituut -    Brugge Middenschool", "BRUGGE", 8000);
        schools[787] = new School(112078, "Vrij Technisch Instituut -    Brugge Middenschool", "SINT-MICHIELS", 8200);
        schools[788] = new School(112086, "Vrij Technisch Instituut 2", "WAREGEM", 8790);
        schools[789] = new School(112094, "Sint-Theresiacollege AEG", "KAPELLE-OP-DEN-BOS", 1880);
        schools[790] = new School(112102, "Atheneum Wispelberg", "GENT", 9000);
        schools[791] = new School(112136, "Leiepoort Deinze campus Sint- Hendrik, eerste graad", "DEINZE", 9800);
        schools[792] = new School(112144, "Don Boscocollege Eerste graad", "ZWIJNAARDE", 9052);
        schools[793] = new School(112169, "Sint-Bavohumaniora Middenschool", "GENT", 9000);
        schools[794] = new School(112292, "GO! atheneum Leuven", "LEUVEN", 3000);
        schools[795] = new School(112301, "Middenschool Ter Beuke", "KESSEL-LO", 3010);
        schools[796] = new School(112318, "Provinciaal Technisch         Instituut", "KORTRIJK", 8500);
        schools[797] = new School(112797, "Provinciaal Instituut         Sint-Godelieve", "ANTWERPEN", 2018);
        schools[798] = new School(112797, "Provinciaal Instituut         Sint-Godelieve", "DEURNE", 2100);
        schools[799] = new School(115221, "Regina-Caelilyceum E.G.", "DILBEEK", 1700);
        schools[800] = new School(115238, "GO! atheneum Zottegem", "HERZELE", 9550);
        schools[801] = new School(115238, "GO! atheneum Zottegem", "ZOTTEGEM", 9620);
        schools[802] = new School(115238, "GO! atheneum Zottegem", "BRAKEL", 9660);
        schools[803] = new School(115253, "Sint-Franciscuscollege        Middenschool 1", "HEUSDEN-ZOLDER", 3550);
        schools[804] = new School(115261, "Sint-Franciscuscollege        Middenschool 2", "HEUSDEN-ZOLDER", 3550);
        schools[805] = new School(115279, "Sint-Franciscuscollege ASO", "HEUSDEN-ZOLDER", 3550);
        schools[806] = new School(115287, "Sint-Franciscuscollege        Handelsschool", "HEUSDEN-ZOLDER", 3550);
        schools[807] = new School(115295, "Sint-Franciscuscollege TSO/BSO", "HEUSDEN-ZOLDER", 3550);
        schools[808] = new School(115303, "Scheppersinstituut 2          Deurne & Antwerpen", "ANTWERPEN", 2060);
        schools[809] = new School(115303, "Scheppersinstituut 2          Deurne & Antwerpen", "DEURNE", 2100);
        schools[810] = new School(115311, "Scheppersinstituut 3          Deurne & Antwerpen", "ANTWERPEN", 2060);
        schools[811] = new School(115311, "Scheppersinstituut 3          Deurne & Antwerpen", "DEURNE", 2100);
        schools[812] = new School(115329, "Don Bosco-Instituut EG", "GROOT-BIJGAARDEN", 1702);
        schools[813] = new School(115337, "Don Bosco-Instituut           ASO/TSO/BSO", "GROOT-BIJGAARDEN", 1702);
        schools[814] = new School(115352, "Sint-Janscollege", "SINT-AMANDSBERG", 9040);
        schools[815] = new School(115361, "Sint-Janscollege eerste graad", "SINT-AMANDSBERG", 9040);
        schools[816] = new School(115378, "Sint-Andreas Middenschool", "OOSTENDE", 8400);
        schools[817] = new School(115394, "GO! atheneum Gentbrugge", "LEDEBERG", 9050);
        schools[818] = new School(115411, "Middenschool Sint-Rembert 2", "TORHOUT", 8820);
        schools[819] = new School(116749, "School voor Verkoop,          Informatieverwerking en       Personenzorg", "GENT", 9000);
        schools[820] = new School(116756, "GO! atheneum - IPB Ronse", "RONSE", 9600);
        schools[821] = new School(116764, "GO! Spectrumschool Campus Deurne", "DEURNE", 2100);
        schools[822] = new School(116764, "GO! Spectrumschool Campus Deurne", "BORGERHOUT", 2140);
        schools[823] = new School(116781, "Sint-Vincentius", "EEKLO", 9900);
        schools[824] = new School(116806, "College Hagelstein 2", "SINT-KATELIJNE-WAVER", 2860);
        schools[825] = new School(116831, "GO! atheneum De Ring Leuven", "LEUVEN", 3000);
        schools[826] = new School(116855, "KOGEKA 2", "GEEL", 2440);
        schools[827] = new School(116871, "KOGEKA 4", "GEEL", 2440);
        schools[828] = new School(116913, "Onze-Lieve-Vrouwe-instituut", "POPERINGE", 8970);
        schools[829] = new School(116921, "Sint-Janscollege 1", "POPERINGE", 8970);
        schools[830] = new School(116947, "Vrij Handels- en Technisch    Instituut - eerste graad", "DENDERMONDE", 9200);
        schools[831] = new School(116971, "Vrije Technische Scholen van  Turnhout", "TURNHOUT", 2300);
        schools[832] = new School(116988, "Vrije Technische Scholen van  Turnhout", "TURNHOUT", 2300);
        schools[833] = new School(117036, "Onze-Lieve-Vrouwinstituut     Middenschool 1", "BOOM", 2850);
        schools[834] = new School(117044, "Onze-Lieve-Vrouwinstituut     Middenschool 2", "BOOM", 2850);
        schools[835] = new School(117051, "Onze-Lieve-Vrouwinstituut     Bovenbouw ASO", "BOOM", 2850);
        schools[836] = new School(117069, "Onze-Lieve-Vrouwinstituut     Bovenbouw TSO-BSO", "BOOM", 2850);
        schools[837] = new School(117093, "Sint-Willebrord-H.Familie 1e  graad", "BERCHEM", 2600);
        schools[838] = new School(117101, "Middenschool Sint-Rembert 3", "TORHOUT", 8820);
        schools[839] = new School(117754, "GO! technisch atheneum Heist-op-den-Berg", "HEIST-OP-DEN-BERG", 2220);
        schools[840] = new School(117762, "GO! atheneum Herzele", "GERAARDSBERGEN", 9500);
        schools[841] = new School(117762, "GO! atheneum Herzele", "HERZELE", 9550);
        schools[842] = new School(117762, "GO! atheneum Herzele", "ZOTTEGEM", 9620);
        schools[843] = new School(117771, "Provinciale Middenschool      Sint-Godelieve", "ANTWERPEN", 2018);
        schools[844] = new School(117771, "Provinciale Middenschool      Sint-Godelieve", "DEURNE", 2100);
        schools[845] = new School(117788, "Provinciale Middenschool", "STABROEK", 2940);
        schools[846] = new School(117812, "GO! atheneum Vilvoorde", "VILVOORDE", 1800);
        schools[847] = new School(117821, "GO! atheneum Grimbergen", "GRIMBERGEN", 1850);
        schools[848] = new School(117838, "GO! technisch atheneum De Brug Vilvoorde", "VILVOORDE", 1800);
        schools[849] = new School(117846, "Heilig-Grafinstituut", "BILZEN", 3740);
        schools[850] = new School(117853, "Instituut voor Katholiek      Secundair Onderwijs", "HOESELT", 3730);
        schools[851] = new School(117861, "Sint-Lambertuscollege 1", "BILZEN", 3740);
        schools[852] = new School(118257, "Sint-Pauluscollege", "WEVELGEM", 8560);
        schools[853] = new School(118265, "viio 1", "BORGLOON", 3840);
        schools[854] = new School(118281, "Scheppersinstituut 1", "WETTEREN", 9230);
        schools[855] = new School(118299, "Sint-Gertrudiscollege Eerste  Graad", "WETTEREN", 9230);
        schools[856] = new School(118307, "Klein Seminarie Hoogstraten   eerste graad", "HOOGSTRATEN", 2320);
        schools[857] = new School(118315, "Sint-Martinusscholen 118315", "HERK-DE-STAD", 3540);
        schools[858] = new School(118323, "Sint-Martinusscholen 118323", "HERK-DE-STAD", 3540);
        schools[859] = new School(118331, "Sint-Martinusscholen 118331", "HERK-DE-STAD", 3540);
        schools[860] = new School(118349, "Sint-Martinusscholen 118349", "HERK-DE-STAD", 3540);
        schools[861] = new School(118356, "Instituut Heilig Hart van     Maria", "BERLAAR", 2590);
        schools[862] = new School(118364, "H. Hart van Mariainstituut", "BERLAAR", 2590);
        schools[863] = new School(118372, "Sint-Jan Berchmanscollege", "MOL", 2400);
        schools[864] = new School(118381, "Sint-Jan Berchmanscollege", "MOL", 2400);
        schools[865] = new School(118398, "Sint-Jan Berchmanscollege eerste graad - vzw KASOG", "GENK", 3600);
        schools[866] = new School(118406, "Sint-Jan Berchmanscollege -   vzw KASOG", "GENK", 3600);
        schools[867] = new School(122382, "Vrije Nederlandstalige school Lucerna College", "ANDERLECHT", 1070);
        schools[868] = new School(122382, "Vrije Nederlandstalige school Lucerna College", "ANTWERPEN", 2060);
        schools[869] = new School(122382, "Vrije Nederlandstalige school Lucerna College", "GENK", 3600);
        schools[870] = new School(122382, "Vrije Nederlandstalige school Lucerna College", "MELLE", 9090);
        schools[871] = new School(122671, "GO! atheneum Oudenaarde", "OUDENAARDE", 9700);
        schools[872] = new School(122705, "Onze-Lieve-Vrouwcollege II", "ZOTTEGEM", 9620);
        schools[873] = new School(122713, "Onze-Lieve-Vrouwcollege III", "ZOTTEGEM", 9620);
        schools[874] = new School(122721, "Mariagaarde Instituut         Middenschool", "WESTMALLE", 2390);
        schools[875] = new School(122739, "Ursulinen Mechelen 1", "MECHELEN", 2800);
        schools[876] = new School(122747, "Technisch Instituut           Sint-Carolus-1", "SINT-NIKLAAS", 9100);
        schools[877] = new School(122754, "Sint-Jozef", "SINT-NIKLAAS", 9100);
        schools[878] = new School(122762, "Kardinaal van Roey-Instituut  AEG", "VORSELAAR", 2290);
        schools[879] = new School(122771, "Leielandscholen Campus Sint-  Niklaas Gemeente Zwevegem 2", "ZWEVEGEM", 8550);
        schools[880] = new School(122788, "Heilig Grafinstituut 122788", "TURNHOUT", 2300);
        schools[881] = new School(122796, "Berthoutinstituut-Klein       Seminarie 1", "MECHELEN", 2800);
        schools[882] = new School(122861, "Vrij Technisch Instituut      Spijker eerste graad", "HOOGSTRATEN", 2320);
        schools[883] = new School(122879, "Don Bosco Instituut eerste    graad", "HALLE", 1500);
        schools[884] = new School(123265, "Sint-Ritacollege eerste graad", "KONTICH", 2550);
        schools[885] = new School(123273, "Sint-Gummaruscollege EG-1", "LIER", 2500);
        schools[886] = new School(123281, "Sint-Jozefinstituut eerste graad", "KONTICH", 2550);
        schools[887] = new School(123554, "Sint-Jorisschool", "MENEN", 8930);
        schools[888] = new School(123571, "Sint-Pietersinstituut", "TURNHOUT", 2300);
        schools[889] = new School(123588, "Sint-Pietersinstituut Hoger   Secundair Onderwijs", "TURNHOUT", 2300);
        schools[890] = new School(123612, "Sint-Jozef-2", "SINT-NIKLAAS", 9100);
        schools[891] = new School(123621, "Damiaaninstituut A", "AARSCHOT", 3200);
        schools[892] = new School(123638, "Sint-Jozefscollege 2", "AARSCHOT", 3200);
        schools[893] = new School(123646, "Sint-Jozefscollege 3", "AARSCHOT", 3200);
        schools[894] = new School(123653, "Sint-Gummaruscollege EG-2", "LIER", 2500);
        schools[895] = new School(123661, "KOGEKA 3", "GEEL", 2440);
        schools[896] = new School(123679, "KOGEKA 6", "GEEL", 2440);
        schools[897] = new School(123687, "KOGEKA 7", "GEEL", 2440);
        schools[898] = new School(123695, "KOGEKA 8", "GEEL", 2440);
        schools[899] = new School(123703, "Guldensporencollege 4", "KORTRIJK", 8500);
        schools[900] = new School(123703, "Guldensporencollege 4", "HARELBEKE", 8530);
        schools[901] = new School(123711, "Guldensporencollege 3", "KORTRIJK", 8500);
        schools[902] = new School(123711, "Guldensporencollege 3", "HARELBEKE", 8530);
        schools[903] = new School(123761, "Sint-Gabriëlcollege", "BOECHOUT", 2530);
        schools[904] = new School(123778, "Sint-Gabriëlcollege -         Middenschool 1", "BOECHOUT", 2530);
        schools[905] = new School(123786, "Technisch Instituut           Sint-Paulus", "MOL", 2400);
        schools[906] = new School(123794, "Technisch Instituut           Sint-Paulus", "MOL", 2400);
        schools[907] = new School(123802, "Sint-Lodewijkscollege", "SINT-MICHIELS", 8200);
        schools[908] = new School(123811, "Sint-Lodewijkscollege         Eerste Graad", "SINT-MICHIELS", 8200);
        schools[909] = new School(123828, "Onze-Lieve-Vrouw 1", "SINT-TRUIDEN", 3800);
        schools[910] = new School(123836, "Onze-Lieve-Vrouw 2", "SINT-TRUIDEN", 3800);
        schools[911] = new School(123844, "Sint-Lambertus 3", "WESTERLO", 2260);
        schools[912] = new School(123851, "Sint-Lambertus 1", "WESTERLO", 2260);
        schools[913] = new School(123869, "Sint-Lambertus 2", "WESTERLO", 2260);
        schools[914] = new School(123869, "Sint-Lambertus 2", "VEERLE", 2431);
        schools[915] = new School(123877, "Sint-Lambertus 4", "WESTERLO", 2260);
        schools[916] = new School(123935, "Klein Seminarie", "ROESELARE", 8800);
        schools[917] = new School(123943, "Klein Seminarie eerste graad", "ROESELARE", 8800);
        schools[918] = new School(123951, "BARNUM", "ROESELARE", 8800);
        schools[919] = new School(123968, "Barnum eerste graad", "ROESELARE", 8800);
        schools[920] = new School(123976, "VISO", "ROESELARE", 8800);
        schools[921] = new School(123984, "VISO eerste graad", "ROESELARE", 8800);
        schools[922] = new School(125187, "Mariagaard Eerste Graad", "WETTEREN", 9230);
        schools[923] = new School(125195, "Sint-Jan Berchmanscollege MS", "WESTMALLE", 2390);
        schools[924] = new School(125203, "Sint-Gabriëlcollege -         Middenschool 2", "BOECHOUT", 2530);
        schools[925] = new School(125211, "Sint-Godelieve-Instituut ASO", "LENNIK", 1750);
        schools[926] = new School(125229, "Sint-Godelieve-Instituut      Autonome Eerste Graad", "LENNIK", 1750);
        schools[927] = new School(125252, "Instituut Sancta Maria - A", "AARSCHOT", 3200);
        schools[928] = new School(125261, "Instituut Sancta Maria - B", "AARSCHOT", 3200);
        schools[929] = new School(125278, "viio 4", "TONGEREN", 3700);
        schools[930] = new School(125286, "viio 3", "TONGEREN", 3700);
        schools[931] = new School(125294, "viio 5", "TONGEREN", 3700);
        schools[932] = new School(125302, "viio 2", "TONGEREN", 3700);
        schools[933] = new School(125328, "Stedelijk Lyceum Zuid", "ANTWERPEN", 2020);
        schools[934] = new School(125344, "Sint-Lambertuscollege 2", "BILZEN", 3740);
        schools[935] = new School(125351, "Guldensporencollege 5", "KORTRIJK", 8500);
        schools[936] = new School(125377, "Guldensporencollege 6", "KORTRIJK", 8500);
        schools[937] = new School(125393, "Onze-Lieve-Vrouwlyceum -eerstegraad", "GENK", 3600);
        schools[938] = new School(125401, "Onze-Lieve-Vrouwlyceum", "GENK", 3600);
        schools[939] = new School(125419, "Onze-Lieve-Vrouw-Presentatie 1", "LOKEREN", 9160);
        schools[940] = new School(125427, "Onze-Lieve-Vrouw-Presentatie 2", "LOKEREN", 9160);
        schools[941] = new School(125435, "KCST 1", "SINT-TRUIDEN", 3800);
        schools[942] = new School(125443, "KCST 2", "SINT-TRUIDEN", 3800);
        schools[943] = new School(125451, "KCST 3", "SINT-TRUIDEN", 3800);
        schools[944] = new School(125799, "Rozenberg S.O.", "MOL", 2400);
        schools[945] = new School(125807, "Rozenberg S.O.", "MOL", 2400);
        schools[946] = new School(125823, "Rozenberg S.O.", "MOL", 2400);
        schools[947] = new School(125831, "Rozenberg S.O.", "MOL", 2400);
        schools[948] = new School(125849, "Sint-Jan Berchmanscollege     eerste graad b - vzw KASOG", "GENK", 3600);
        schools[949] = new School(125922, "GO! technisch atheneum Victor Hortaschool Evere", "EVERE", 1140);
        schools[950] = new School(125948, "GO! Atheneum Lokeren", "LOKEREN", 9160);
        schools[951] = new School(125963, "Koninklijk Atheneum           Berchem", "MERKSEM", 2170);
        schools[952] = new School(125963, "Koninklijk Atheneum           Berchem", "BERCHEM", 2600);
        schools[953] = new School(125971, "Talentenschool Turnhout campusBoomgaard KA", "TURNHOUT", 2300);
        schools[954] = new School(125997, "Sint-Jozefscollege 1", "AALST", 9300);
        schools[955] = new School(126003, "Sint-Norbertusinstituut 1", "DUFFEL", 2570);
        schools[956] = new School(126011, "Sint-Norbertusinstituut 2", "DUFFEL", 2570);
        schools[957] = new School(126029, "Sint-Niklaasinstituut", "ANDERLECHT", 1070);
        schools[958] = new School(126037, "Sint-Niklaasinstituut-autonome1ste graad", "ANDERLECHT", 1070);
        schools[959] = new School(126045, "Guldensporencollege 2", "KORTRIJK", 8500);
        schools[960] = new School(126045, "Guldensporencollege 2", "HARELBEKE", 8530);
        schools[961] = new School(126052, "Guldensporencollege 1", "KORTRIJK", 8500);
        schools[962] = new School(126052, "Guldensporencollege 1", "HARELBEKE", 8530);
        schools[963] = new School(126061, "Broederscholen Hiëronymus 3", "SINT-NIKLAAS", 9100);
        schools[964] = new School(126094, "Heilige DrievuldigheidscollegeEerstegraadschool", "LEUVEN", 3000);
        schools[965] = new School(126102, "Instituut H. Hart van         Maria Middenschool 2", "BERLAAR", 2590);
        schools[966] = new School(126111, "Don Bosco Technisch Instituut 1", "HELCHTEREN", 3530);
        schools[967] = new School(126151, "Instituut Sint-Vincentius a   Paulo 1", "GIJZEGEM", 9308);
        schools[968] = new School(126169, "Sint-Pieterscollege           Eerste graadschool", "LEUVEN", 3000);
        schools[969] = new School(126177, "Don Bosco Technisch Instituut E.G.", "SINT-DENIJS-WESTREM", 9051);
        schools[970] = new School(126185, "TSM-Bovenbouw", "MECHELEN", 2800);
        schools[971] = new School(126193, "WICO - 126193", "OVERPELT", 3900);
        schools[972] = new School(126201, "WICO - 126201", "OVERPELT", 3900);
        schools[973] = new School(126219, "WICO - 126219", "LOMMEL", 3920);
        schools[974] = new School(126227, "WICO - 126227", "LOMMEL", 3920);
        schools[975] = new School(126235, "WICO - 126235", "NEERPELT", 3910);
        schools[976] = new School(126243, "WICO - 126243", "NEERPELT", 3910);
        schools[977] = new School(126251, "WICO - 126251", "NEERPELT", 3910);
        schools[978] = new School(126268, "WICO - 126268", "NEERPELT", 3910);
        schools[979] = new School(126276, "Eerste graad Voorzienigheid", "DIEST", 3290);
        schools[980] = new School(126284, "Humaniora Voorzienigheid", "DIEST", 3290);
        schools[981] = new School(126292, "Vrij Technisch Instituut      Voorzienigheid", "DIEST", 3290);
        schools[982] = new School(126433, "BenedictusPoort campus Maria  Middelares", "GENT", 9000);
        schools[983] = new School(126433, "BenedictusPoort campus Maria  Middelares", "LEDEBERG", 9050);
        schools[984] = new School(126441, "BenedictusPoort campus        Ledeberg", "LEDEBERG", 9050);
        schools[985] = new School(126466, "Heilig Hartcollege", "WEZEMBEEK-OPPEM", 1970);
        schools[986] = new School(126474, "Heilig Hartcollege", "WEZEMBEEK-OPPEM", 1970);
        schools[987] = new School(126649, "Sint-Barbaracollege I", "GENT", 9000);
        schools[988] = new School(126656, "Pedagogische Humaniora -      Heilig Hartinstituut          Middenschool", "HEVERLEE", 3001);
        schools[989] = new School(126664, "Heilig Hartinstituut TechnischOnderwijs Middenschool", "HEVERLEE", 3001);
        schools[990] = new School(126672, "Heilig Hartinstituut Lyceum   Middenschool", "HEVERLEE", 3001);
        schools[991] = new School(126706, "Sint-Godelievecollege         Middenschool", "GISTEL", 8470);
        schools[992] = new School(126706, "Sint-Godelievecollege         Middenschool", "EERNEGEM", 8480);
        schools[993] = new School(126714, "Sint-Jozefscollege 2", "AALST", 9300);
        schools[994] = new School(126731, "Sint-Martinuscollege", "OVERIJSE", 3090);
        schools[995] = new School(126748, "Sint-Martinuscollege 1e graad", "OVERIJSE", 3090);
        schools[996] = new School(126797, "Sint-Augustinusinstituut ASO", "BREE", 3960);
        schools[997] = new School(126805, "Petrus & Paulus campus centrumOnze-Lieve-Vrouwecollege      middenschool", "OOSTENDE", 8400);
        schools[998] = new School(126847, "Bernardusscholen 3", "OUDENAARDE", 9700);
        schools[999] = new School(126854, "Stedelijk Lyceum Pestalozzi II", "ANTWERPEN", 2020);
        schools[1000] = new School(126854, "Stedelijk Lyceum Pestalozzi II", "BERCHEM", 2600);
        schools[1001] = new School(126854, "Stedelijk Lyceum Pestalozzi II", "WILRIJK", 2610);
        schools[1002] = new School(126871, "College Hagelstein 1", "SINT-KATELIJNE-WAVER", 2860);
        schools[1003] = new School(126888, "Paridaensinstituut secundair  onderwijs", "LEUVEN", 3000);
        schools[1004] = new School(126896, "Paridaensinstituut            Eerstegraadsschool", "LEUVEN", 3000);
        schools[1005] = new School(126904, "Don Bosco Groenveld", "HEVERLEE", 3001);
        schools[1006] = new School(126921, "Vrij Technisch Instituut - 3", "AALST", 9300);
        schools[1007] = new School(126938, "Vrij Technisch Instituut - 1", "AALST", 9300);
        schools[1008] = new School(126946, "Sint-Ursula-Instituut 1", "ONZE-LIEVE-VROUW-WAVER", 2861);
        schools[1009] = new School(126953, "Sint-Ursula-Instituut 2", "ONZE-LIEVE-VROUW-WAVER", 2861);
        schools[1010] = new School(126961, "Sint-Ursula-Instituut 3", "ONZE-LIEVE-VROUW-WAVER", 2861);
        schools[1011] = new School(126987, "Sint-Maarteninstituut", "AALST", 9300);
        schools[1012] = new School(126995, "Sint-Maarteninstituut eerste  graad", "AALST", 9300);
        schools[1013] = new School(127159, "Margareta-Maria-Instituut -   T.S.O. - B.S.O. 1ste graad", "KORTEMARK", 8610);
        schools[1014] = new School(127423, "COLOMAplus 3", "MECHELEN", 2800);
        schools[1015] = new School(127431, "Sint-Jorisschool eerste graad", "MENEN", 8930);
        schools[1016] = new School(127431, "Sint-Jorisschool eerste graad", "WERVIK", 8940);
        schools[1017] = new School(127449, "Xaveriuscollege2", "BORGERHOUT", 2140);
        schools[1018] = new School(127456, "Onze-Lieve-Vrouwecollege 2", "ANTWERPEN", 2000);
        schools[1019] = new School(127464, "Jan-van-Ruusbroeckollege      Eerste Graad", "LAKEN", 1020);
        schools[1020] = new School(127472, "Sint-Ludgardis Belpaire", "ANTWERPEN", 2000);
        schools[1021] = new School(127481, "Sint-Ludgardisschool", "ANTWERPEN", 2000);
        schools[1022] = new School(127514, "Sint-Jozefcollege Turnhout", "TURNHOUT", 2300);
        schools[1023] = new School(127522, "Sint-Jozefcollege Turnhout 1", "TURNHOUT", 2300);
        schools[1024] = new School(127531, "Sint-Paulusinstituut 1", "GENT", 9000);
        schools[1025] = new School(127548, "Sint-Paulusinstituut 2", "GENT", 9000);
        schools[1026] = new School(127548, "Sint-Paulusinstituut 2", "SINT-DENIJS-WESTREM", 9051);
        schools[1027] = new School(127563, "Inspirocollege", "HELCHTEREN", 3530);
        schools[1028] = new School(127571, "Inspirocollege", "HELCHTEREN", 3530);
        schools[1029] = new School(127597, "Maris Stella Instituut 1", "WESTMALLE", 2390);
        schools[1030] = new School(127605, "Heilig-Hartcollege 2", "WAREGEM", 8790);
        schools[1031] = new School(127613, "Virga Jessecollege -          eerste graad 1", "HASSELT", 3500);
        schools[1032] = new School(127621, "Virga Jessecollege -          eerste graad 2", "HASSELT", 3500);
        schools[1033] = new School(127639, "Middenschool Kindsheid Jesu", "HASSELT", 3500);
        schools[1034] = new School(127647, "Harlindis en Relindis E.G.S.2", "MAASEIK", 3680);
        schools[1035] = new School(127654, "Harlindis en Relindis         College Heilig Kruis - Sint-  Ursula B", "MAASEIK", 3680);
        schools[1036] = new School(127662, "Harlindis en Relindis         Technisch Instituut           Sint-Jansberg B", "MAASEIK", 3680);
        schools[1037] = new School(127671, "Harlindis en Relindis E.G.S.4", "KINROOI", 3640);
        schools[1038] = new School(127688, "Sint-Lievenscollege 1", "GENT", 9000);
        schools[1039] = new School(127696, "Sint-Lievenscollege", "GENT", 9000);
        schools[1040] = new School(127704, "EDUGO campus De Toren", "OOSTAKKER", 9041);
        schools[1041] = new School(127704, "EDUGO campus De Toren", "LOCHRISTI", 9080);
        schools[1042] = new School(127712, "EDUGO campus De Brug 1", "OOSTAKKER", 9041);
        schools[1043] = new School(127721, "EDUGO campus De Brug 2", "OOSTAKKER", 9041);
        schools[1044] = new School(127738, "Onze-Lieve-Vrouw van Vreugde", "ROESELARE", 8800);
        schools[1045] = new School(127746, "Onze-Lieve-Vrouw van Vreugde  eerste graad", "ROESELARE", 8800);
        schools[1046] = new School(127753, "Vrije Middelbare School", "ROESELARE", 8800);
        schools[1047] = new School(127761, "Vrije Middelbare School       eerste graad", "ROESELARE", 8800);
        schools[1048] = new School(127779, "Vrij Technisch Instituut 1", "ROESELARE", 8800);
        schools[1049] = new School(127779, "Vrij Technisch Instituut 1", "ARDOOIE", 8850);
        schools[1050] = new School(127787, "V.T.I. eerste graad", "ROESELARE", 8800);
        schools[1051] = new School(127795, "Heilige Kindsheid", "ARDOOIE", 8850);
        schools[1052] = new School(127803, "Heilig Kindsheid eerste graad", "ARDOOIE", 8850);
        schools[1053] = new School(127811, "Sancta Maria Leuven", "LEUVEN", 3000);
        schools[1054] = new School(127829, "Sancta Maria Instituut", "LEUVEN", 3000);
        schools[1055] = new School(127837, "WICO campus Mater Dei - 127837", "OVERPELT", 3900);
        schools[1056] = new School(127845, "WICO campus Mater Dei - 127845", "OVERPELT", 3900);
        schools[1057] = new School(127852, "Sint-Michielscollege          Brasschaat 1", "BRASSCHAAT", 2930);
        schools[1058] = new School(127861, "Sint-Norbertusinstituut 3", "DUFFEL", 2570);
        schools[1059] = new School(127878, "Maria Assumptalyceum", "LAKEN", 1020);
        schools[1060] = new School(127886, "VIA-2", "TIENEN", 3300);
        schools[1061] = new School(127894, "GO! technisch atheneum - GITO Groenkouter Sint-Amandsberg", "SINT-AMANDSBERG", 9040);
        schools[1062] = new School(127911, "Berkenboom Humaniora eerste   graad", "SINT-NIKLAAS", 9100);
        schools[1063] = new School(127928, "Sint-Michielscollege 1", "SCHOTEN", 2900);
        schools[1064] = new School(127936, "Sint-Pieterscollege", "JETTE", 1090);
        schools[1065] = new School(127944, "Onze-Lieve-Vrouw-van-Lourdes- college", "MORTSEL", 2640);
        schools[1066] = new School(127944, "Onze-Lieve-Vrouw-van-Lourdes- college", "EDEGEM", 2650);
        schools[1067] = new School(127951, "Onze-Lieve-Vrouw-van-Lourdes- college Middenschool", "EDEGEM", 2650);
        schools[1068] = new School(127969, "Emmaüsinstituut@1", "AALTER", 9880);
        schools[1069] = new School(127977, "Emmaüsinstituut@2", "AALTER", 9880);
        schools[1070] = new School(127985, "GO! atheneum Erasmus Deinze", "TIELT", 8700);
        schools[1071] = new School(127985, "GO! atheneum Erasmus Deinze", "WAREGEM", 8790);
        schools[1072] = new School(127985, "GO! atheneum Erasmus Deinze", "DEINZE", 9800);
        schools[1073] = new School(127985, "GO! atheneum Erasmus Deinze", "DE PINTE", 9840);
        schools[1074] = new School(127985, "GO! atheneum Erasmus Deinze", "AALTER", 9880);
        schools[1075] = new School(127993, "GO! atheneum Erasmus De Pinte", "DE PINTE", 9840);
        schools[1076] = new School(128017, "Lyceum Ieper eerste graad", "IEPER", 8900);
        schools[1077] = new School(128025, "Sint-Pietersinstituut         eerstegraadsschool", "GENT", 9000);
        schools[1078] = new School(128108, "GO! atheneum Zaventem", "ZAVENTEM", 1930);
        schools[1079] = new School(128447, "Guldensporencollege 7", "KORTRIJK", 8500);
        schools[1080] = new School(128538, "PTS, Provinciale Scholen voor Tuinbouw en Techniek", "MECHELEN", 2800);
        schools[1081] = new School(128538, "PTS, Provinciale Scholen voor Tuinbouw en Techniek", "BOOM", 2850);
        schools[1082] = new School(128546, "Provinciale Middenschool", "MECHELEN", 2800);
        schools[1083] = new School(128546, "Provinciale Middenschool", "BOOM", 2850);
        schools[1084] = new School(128553, "Technisch Instituut           Sint-Michiel", "BREE", 3960);
        schools[1085] = new School(128561, "Technisch Instituut           Sint-Michiel eerste graad", "MEEUWEN-GRUITRODE", 3670);
        schools[1086] = new School(128561, "Technisch Instituut           Sint-Michiel eerste graad", "BREE", 3960);
        schools[1087] = new School(128983, "GO! atheneum Tienen", "TIENEN", 3300);
        schools[1088] = new School(129411, "Katholiek Secundair Onderwijs Mortsel", "MORTSEL", 2640);
        schools[1089] = new School(129916, "Sint-Jozefscollege", "MENEN", 8930);
        schools[1090] = new School(129916, "Sint-Jozefscollege", "WERVIK", 8940);
        schools[1091] = new School(130807, "GO! atheneum Centrum Oostende", "OOSTENDE", 8400);
        schools[1092] = new School(131243, "Safe", "MECHELEN", 2800);

    }
}
