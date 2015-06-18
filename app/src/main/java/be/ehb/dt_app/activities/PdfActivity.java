package be.ehb.dt_app.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.PdflijstAdapter;
import be.ehb.dt_app.model.Pdf;

public class PdfActivity extends Activity implements SearchView.OnQueryTextListener {

    //https://www.dropbox.com/developers/core/sdks/android
    //https://www.dropbox.com/developers/core/start/android

    final static private String APP_KEY = "zaoo56rk9zylzzt";
    final static private String APP_SECRET = "uzk9zlhe6ro5vg1";
    private DropboxAPI<AndroidAuthSession> mDBApi;

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
/*
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
        pdfArrayList.add(new Pdf("Informatie EhB3", "2 maart 2015"));*/

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        mDBApi.getSession().startOAuth2Authentication(PdfActivity.this);

//        pdflijstAdapter = new PdflijstAdapter(this, pdfArrayList);
//        mPdfLV.setAdapter(pdflijstAdapter);

        mPdfLV.setTextFilterEnabled(true);
        setupSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                //new PDFDownloadTask().execute();
            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
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

    private class PDFDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            DropboxAPI.Entry entries = null;
            try {
                entries = mDBApi.metadata("/pdf", 100, null, true, null);
            } catch (DropboxException e) {
                e.printStackTrace();
            }

            for (DropboxAPI.Entry e : entries.contents) {
                File file = new File("/data/data/be.ehb.hellocloudpdf/files/", e.fileName());
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(file);
                    DropboxAPI.DropboxFileInfo info = mDBApi.getFile("/"+e.fileName(), null, outputStream, null);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DropboxException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
