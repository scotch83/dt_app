package be.ehb.dt_app.maps;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;


public class MapActivity extends ActionBarActivity {
    private School[] schools;
    private Subscription[] Subscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        maakScholenKort();
        maakSubscriptionen();
        Schooltotaal schooltotaal = new Schooltotaal();
        schooltotaal.gegevens();

        MapFragment fragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_heatmap);
        Schoolkaart kaart = new Schoolkaart(schooltotaal.getIndeling(), this);
        fragment.getMapAsync(kaart);
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

    private final void maakSubscriptionen() {

        ArrayList<Subscription> subscriptionArrayList;


//        Subscriptions[0] = new Subscription("Lorenz", "Vandevenne", "Acacialaan", "153", 1070, "Gent", "jos.dh@gmail.com", schools[1]);
//        Subscriptions[1] = new Subscription("Oscar", "Vleughels", "Lakenstraat", "15", 2800, "Mechelen", "bolleboos58@gmail.com", schools[1]);
//        Subscriptions[2] = new Subscription("Gregory", "Van Impe", "Lakenstraat", "16", 2801, "Mechelen", "greggy48@gmail.com", schools[1]);
//        Subscriptions[3] = new Subscription("Mario", "Martens", "Lakenstraat", "17", 2802, "Mechelen", "mario.martens@gmail.com", schools[2]);
//        Subscriptions[4] = new Subscription("Bram", "Vervaet", "Lakenstraat", "18", 2803, "Mechelen", "bram.vervaet@gmail.com", schools[1]);
//        Subscriptions[5] = new Subscription("Kelly", "Vervaet", "Lakenstraat", "19", 2804, "Mechelen", "kelly.vervaet@gmail.com", schools[1]);
//        Subscriptions[6] = new Subscription("Christophe", "Deleuter", "Mastlaan", "1", 2200, "Herentals", "crikke598@gmail.com", schools[5]);
//        Subscriptions[7] = new Subscription("John", "Vandenberghe", "Mastlaan", "2", 2201, "Herentals", "jhonny87@gmail.com", schools[5]);
//        Subscriptions[8] = new Subscription("Louis", "Delanghe", "Mastlaan", "3", 2202, "Herentals", "loebas78@gmail.com", schools[0]);
//        Subscriptions[9] = new Subscription("George", "Goegebuur", "Mastlaan", "4", 2203, "Herentals", "george45@gmail.com", schools[0]);
//        Subscriptions[10] = new Subscription("Marc", "Marconi", "Mastlaan", "3", 2202, "Herentals", "marconi78@gmail.com", schools[0]);
//        Subscriptions[11] = new Subscription("Erik", "Brabander", "Rodekruisplein", "23", 1981, "Hofstade", "erikske@gmail.com", schools[6]);
//        Subscriptions[12] = new Subscription("Eva", "Van Uffel", "Noorderlaan", "77", 3000, "Leuven", "eva.van.uffel@gmail.com", schools[6]);
//        Subscriptions[13] = new Subscription("Jef", "Van Nuffel", "Pontstraat", "2", 9300, "Aalst", "jef.vannuffel@gmail.com", schools[4]);
//        Subscriptions[14] = new Subscription("Louis", "Van Nuffel", "Pontstraat", "2", 9300, "Aalst", "louis.vannuffel@gmail.com", schools[4]);
    }
}
