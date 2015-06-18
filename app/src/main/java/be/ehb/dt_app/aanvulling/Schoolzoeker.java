package be.ehb.dt_app.aanvulling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
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
    private ArrayList<School> schools;
    private EditText schoolTxt, locSchoolTxt;
    private ArrayList<String> scholen;
    private short postcode;
    private Long schoolId;
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

            //Log.d("", "Schoollocatie = " + schoollocatie);
            //Log.d("", "Veldlengte = " + schoollocatie.length());
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
    private SchoolParser schoolParser;

    public Schoolzoeker(EditText locSchoolTxt, EditText schoolTxt, Activity a, Postcodezoeker p) {
        schoolParser = new SchoolParser(a);
        maakScholenKort();
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
        //Log.d("", "Postcode " + postcode);
        short s = 0;
        for (short i = 0; i < schools.size(); i++) {
            school = schools.get(i);
            s = 0;
            if (school != null) s = school.getPostcode();
            if (s == postcode) {
                //Log.d("", "Gemeente " + school.getName() + " " + school.getGemeente());
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

        for (short i = 0; i < schools.size(); i++) {
            school = schools.get(i);
            if (school != null) naam = school.getName().toLowerCase();
            if (school != null) gemeente = school.getGemeente().toLowerCase();
            lengte = naam.length();
            ok = naam.contains(zoek);
            if (!ok) ok = gemeente.contains(zoek);

            if (ok) {
                //Log.d("", "School " + school.getName() + " " + school.getGemeente());
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
                    //Log.d("", "Geemeente " + s);
                    postcodes.add(p);
                }
            }

        }

        //Log.d("", "Aantal gevonden: " + postcodes.size());
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
                //Log.d("", "keuze = " + which);
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

                //Log.d("", "keuze = " + which);
                //Log.d("", "Gekozen locatie " + postcodes.get(which).getGemeente());
                postcode = postcodes.get(which).getPostcode();
                schoolId = schools.get(which).getServerId();
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

        schoolParser.parsen();
        schools = schoolParser.getScholen();
    }

    public short getPostcode() {
        return postcode;
    }

    public void setPostcode(short postcode) {
        this.postcode = postcode;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }
}
