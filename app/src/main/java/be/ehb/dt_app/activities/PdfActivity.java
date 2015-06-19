package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import be.ehb.dt_app.R;
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
    private ArrayAdapter<String> pdflijstAdapter;
    private String path;
    private ArrayList<String> fileNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        mPdfLV = (ListView) findViewById(R.id.lv_pdflijst);
        mPdfSV = (SearchView) findViewById(R.id.sv_pdflijst);


        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);

        mDBApi.getSession().startOAuth2Authentication(PdfActivity.this);

        path = getFilesDir().getPath();

        mPdfLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), fileNames.get(position));
                Uri myUri = Uri.fromFile(file);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(myUri, "*/*");

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                new PDFDownloadTask().execute();
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
            fileNames = new ArrayList<>();
            for (DropboxAPI.Entry e : entries.contents) {
                Log.i("Is Folder",String.valueOf(e.isDir));
                Log.i("Item Name",e.fileName());
                fileNames.add(e.fileName());
                File file = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), e.fileName());
                FileOutputStream outputStream = null;

                try {
                    outputStream = new FileOutputStream(file);
                    DropboxAPI.DropboxFileInfo info = mDBApi.getFile("/pdf/"+e.fileName(), null, outputStream, null);
                    outputStream.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (DropboxException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdflijstAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.ehb_pdflijst_listview,R.id.tv_pdfnaam,fileNames);
            mPdfLV.setAdapter(pdflijstAdapter);

            mPdfLV.setTextFilterEnabled(true);
            setupSearchView();

        }
    }
}
