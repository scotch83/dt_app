package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.PdflijstAdapter;
import be.ehb.dt_app.model.Pdf;

public class PdfActivity extends Activity implements SearchView.OnQueryTextListener {

    private SearchView mPdfSV;
    private ListView mPdfLV;
    private ArrayList<Pdf> pdfArrayList;
    private PdflijstAdapter pdflijstAdapter;
    private long lastUsed = System.currentTimeMillis();
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        mPdfLV = (ListView) findViewById(R.id.lv_pdflijst);
        mPdfSV = (SearchView) findViewById(R.id.sv_pdflijst);

        pdfArrayList = new ArrayList<Pdf>();
        pdfArrayList.add(new Pdf("Overzicht curriculum", "20 maart 2014"));
        pdfArrayList.add(new Pdf("Brochures", "5 april 2014"));
        pdfArrayList.add(new Pdf("Presentatie", "6 juni 2013"));
        pdfArrayList.add(new Pdf("Multec", "21 januari 2014"));
        pdfArrayList.add(new Pdf("Dig-ex", "2 maart 2015"));
        pdfArrayList.add(new Pdf("Curriculum Dig-x", "21 januari 2014"));
        pdfArrayList.add(new Pdf("Informatie EhB", "2 maart 2015"));
        pdfArrayList.add(new Pdf("Overzicht curriculum2", "20 maart 2014"));
        pdfArrayList.add(new Pdf("Brochures2", "5 april 2014"));
        pdfArrayList.add(new Pdf("Presentatie2", "6 juni 2013"));
        pdfArrayList.add(new Pdf("Multec2", "21 januari 2014"));
        pdfArrayList.add(new Pdf("Dig-ex2", "2 maart 2015"));
        pdfArrayList.add(new Pdf("Curriculum Dig-x2", "21 januari 2014"));
        pdfArrayList.add(new Pdf("Informatie EhB3", "2 maart 2015"));

        pdflijstAdapter = new PdflijstAdapter(this, pdfArrayList);
        mPdfLV.setAdapter(pdflijstAdapter);

        mPdfLV.setTextFilterEnabled(true);
        setupSearchView();
        startScreensaverThread();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pdf, menu);
        return true;
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void setupSearchView() {
        mPdfSV.setIconifiedByDefault(false);
        mPdfSV.setOnQueryTextListener(this);
        mPdfSV.setSubmitButtonEnabled(true);

        mPdfSV.setQueryHint("Zoek je pdf hier");

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mPdfLV.clearTextFilter();

        } else {
            pdflijstAdapter.getFilter().filter(newText);
            mPdfLV.setFilterText(newText.toString());

        }

        return true;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        lastUsed = System.currentTimeMillis();
    }

    public void startScreensaverThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long idle = 0;

                idle = System.currentTimeMillis() - lastUsed;

                if (idle > preferences.getInt("Screensaver timelapse", 5000)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getApplicationContext(), SlideshowActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });
        thread.start();

    }
}
