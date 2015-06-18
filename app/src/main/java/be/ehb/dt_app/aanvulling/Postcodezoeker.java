package be.ehb.dt_app.aanvulling;

/*Bronnen
http://stackoverflow.com/questions/16389581/android-create-a-popup-that-has-multiple-selection-options
http://developer.android.com/reference/android/text/TextWatcher.html

 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Bart on 3/06/2015.
 */
public final class Postcodezoeker {
    private ArrayList<String> gemeenten;
    private short postcode = 1000;
    private TextWatcher postcodeZoeker = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //Zoekt de postcode nadat de postcode is ingevoerd.
            String regex = "[0-9]{0,4}";
            String tekst = postcodeTxt.getText().toString();

            if (tekst.matches(regex) && tekst.length() == 4) {
                postcode = Short.parseShort(tekst);
                gemeenten = zoekGemeentenMetPostcode(postcode);
                if (!gemeenten.isEmpty()) maakPopup(gemeenten.size());
            }

        }
    };
    private ArrayList<Postcode> postcodes;
    private EditText postcodeTxt, gemeenteTxt;
    private Activity activity;
    private PostcodeParser postcodeParser;


    public Postcodezoeker(EditText postcodeTxt, EditText gemeenteTxt, Activity a) {
        this.postcodeParser = new PostcodeParser(a);
        this.postcodeTxt = postcodeTxt;
        this.gemeenteTxt = gemeenteTxt;
        activity = a;
        maakPostcodes();
        gemeenten = new ArrayList<String>();
        if (postcodeTxt != null)
            this.postcodeTxt.addTextChangedListener(postcodeZoeker);

    }

    private void maakPopup(int aantal) {
        String[] g = new String[aantal];
        for (short i = 0; i < aantal; i++) {
            g[i] = gemeenten.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Kies stad of gemeente");
        builder.setItems(g, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("", "keuze = " + which);
                gemeenteTxt.setText(gemeenten.get(which));
            }
        });
        builder.show();
    }

    public ArrayList<Postcode> getPostcodes() {
        return postcodes;
    }

    private ArrayList<String> zoekGemeentenMetPostcode(short postcode) {
        gemeenten.clear();
        Postcode p;
        short s = 0;
        for (short i = 0; i < postcodes.size(); i++) {
            p = postcodes.get(i);
            s = 0;
            if (p != null) s = p.getPostcode();
            if (s == postcode) {
                Log.d("", "Gemeente " + p.getGemeente());
                gemeenten.add(p.getGemeente());
            }
        }

        return gemeenten;
    }

    private final void maakPostcodes() {
        postcodeParser.parsen();
        postcodes = postcodeParser.getPostcodes();

    }
}