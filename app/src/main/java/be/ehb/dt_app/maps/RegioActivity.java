package be.ehb.dt_app.maps;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Iterator;
import java.util.TreeMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.School;

public class RegioActivity extends ActionBarActivity {
    private Regio regio;
    private String beschrijving, titel;
    private TextView regionaamTv, scholenTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regio);

        regionaamTv = (TextView) findViewById(R.id.regionaamTv);
        scholenTv = (TextView) findViewById(R.id.scholenTv);

        regioInfo1();

        regionaamTv.setText(titel);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void toonAantallen(TreeMap<School, Short> h) {
        Iterator<School> i = h.keySet().iterator();
        Iterator<Short> j = h.values().iterator();
        String school, tekst = "";
        School s;
        short aantal = 0;

        while (i.hasNext()) {
            s = i.next();
            school = s.getName() + " " + s.getGemeente();
            aantal = j.next();
            tekst += school + ": " + aantal + " studenten\n";
            Log.d("", tekst);
        }
        scholenTv.setText(tekst);
    }

    private void regioInfo1() {
        regio = (Regio) getIntent().getSerializableExtra("Regio");
        beschrijving = regio.getBeschrijving();
        titel = regio.getNaam();
        toonAantallen(regio.getLocaties());
    }

    private void regioInfo2() {
        beschrijving = (String) getIntent().getSerializableExtra("Beschrijving");
        titel = (String) getIntent().getSerializableExtra("Titel");
    }
}
