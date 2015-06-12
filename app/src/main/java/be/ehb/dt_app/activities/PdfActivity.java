package be.ehb.dt_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.PdflijstAdapter;
import be.ehb.dt_app.model.Pdf;

public class PdfActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    private SearchView mPdfSV;
    private ListView mPdfLV;
    private ArrayList<Pdf> pdfArrayList;
    private PdflijstAdapter pdflijstAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        mPdfLV = (ListView) findViewById(R.id.lv_pdflijst);
        mPdfSV = (SearchView) findViewById(R.id.sv_pdflijst);

        pdfArrayList = new ArrayList<Pdf>();
        pdfArrayList.add(new Pdf("Overzicht curriculum", "20 maart 2014"));
        pdfArrayList.add(new Pdf("Brochures", "5 april 2014"));
        pdfArrayList.add(new Pdf("Presentatie", "6 juni 2013"));
        pdfArrayList.add(new Pdf("Multec", "21 januari 2014"));
        pdfArrayList.add(new Pdf("Dig-ex", "2 maart 2015"));

        pdflijstAdapter = new PdflijstAdapter(this, pdfArrayList);
        mPdfLV.setAdapter(pdflijstAdapter);

        mPdfLV.setTextFilterEnabled(true);
        setupSearchView();
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
        mPdfSV.setBackgroundColor(Color.WHITE);
        mPdfSV.setQueryHint("Zoek je pdf hier");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mPdfLV.clearTextFilter();

        } else {
            pdflijstAdapter.getFilter().filter(newText);

        }

        return true;
    }
}
