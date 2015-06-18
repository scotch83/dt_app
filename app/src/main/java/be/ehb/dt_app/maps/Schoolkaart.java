package be.ehb.dt_app.maps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Bronnen
 * http://keystore-explorer.sourceforge.net/downloads.php
 * http://www.vinch.be/blog/2010/03/16/villes-de-belgique-aux-formats-csv-xml-json-et-sql/
 * https://developers.google.com/maps/documentation/android/marker
 * Created by Bart on 10/06/2015.
 */
public class Schoolkaart implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, Serializable {
    private final RegioIndeling regioIndeling;
    private final Activity activity;
    private GoogleMap map;
    private float kleur = Color.GREEN;

    private GoogleMap.OnInfoWindowClickListener infogever = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            maakIntent(marker);
        }
    };

    public Schoolkaart(RegioIndeling r, Activity a) {
        activity = a;
        regioIndeling = r;
    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        maakPinnen();

        map.setOnInfoWindowClickListener(infogever);
        Toast.makeText(activity.getApplication(), "Kaart is ingeladen", Toast.LENGTH_SHORT);
    }

    private final void maakPinnen() {
        TreeMap<String, Regio> regios = regioIndeling.getRegios();
        Iterator<String> i = regios.keySet().iterator();
        Iterator<Regio> j = regios.values().iterator();
        float gem = regioIndeling.berekenGemiddelde();
        String naam;
        Regio regio;
        LatLng pos;

        while (i.hasNext()) {
            naam = i.next();
            regio = j.next();
            pos = new LatLng(regio.getBreedte(), regio.getLengte());
            regio.maakBeschrijving();
            if (regio.isCamera()) bepaalCamera(pos);
            Log.d("", "Kleurwaarde: " + kleur);
            kleur = bepaalKleur(regio.getAantalStudenten(), gem);

            map.addMarker(new MarkerOptions().title(naam)
                    .position(pos)
                    .snippet(regio.getBeschrijving())
                    .icon(BitmapDescriptorFactory.defaultMarker(kleur)));
            Log.d("", "Pin regio " + naam + " geplaatst");
        }

        Log.d("", "Pinnen geplaatst op kaart");
    }

    private void bepaalCamera(LatLng l) {
        CameraUpdateFactory.newLatLng(l);
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(l, 7);
        map.animateCamera(cu);
        Log.d("", "Camerapositie ingesteld");
    }


    private void maakIntent(Marker marker) {
        Intent in = new Intent(activity.getApplicationContext(), RegioActivity.class);
        String titel = marker.getTitle();
        Iterator<Regio> i = regioIndeling.getRegios().values().iterator();
        String beschrijving = marker.getSnippet();
        Log.d("", "Beschrijving: " + beschrijving);
        Regio r;

        while (i.hasNext()) {
            r = i.next();
            if (r.getNaam().equals(titel)) {
                in.putExtra("Regio", r);
                Log.d("", "Regio: " + r.getNaam());
            }

        }

        in.putExtra("Titel", titel);
        in.putExtra("Beschrijving", beschrijving);
        Log.d("", "Intent gestart");
        activity.startActivity(in);

    }

    private float bepaalKleur(int aantal, float gem) {
        if (aantal > gem * 1.5f) return BitmapDescriptorFactory.HUE_RED; //Rood
        if (aantal > gem) return 20; //Donker oranje
        if (aantal > gem * 0.5f && aantal < gem) return 40; //Licht oranje
        return 60; //Geel
    }
}
