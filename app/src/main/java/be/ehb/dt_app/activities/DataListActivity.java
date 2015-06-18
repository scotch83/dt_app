package be.ehb.dt_app.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.StudentenlijstAdapter;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.fragments.ScreensaverDialog;
import be.ehb.dt_app.maps.MapActivity;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.Subscription;

public class DataListActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    //    protected Fragment form1, form2;
//    private ViewPager mPagerRegistratie;
//    private PagerAdapter mPagerAdapter;
    private ImageView img_page1, img_page2;
    private ImageButton heatMapIB;
    private View progressOverlay;
    private SharedPreferences preferences;
    private ListView studentenlijstLV;
    private ArrayList<Subscription> studentenlijstArray = new ArrayList<>();
    private StudentenlijstAdapter slAdapter;
    private SearchView mStudententSV;
    private LinearLayout studentenlijstLL;
    private ScrollView scrollView;
    private long lastUsed = System.currentTimeMillis();
    private boolean stopScreenSaver;
    private ScreensaverDialog screensaverDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        setupDesign();

        heatMapIB = (ImageButton) findViewById(R.id.ib_heatmap_navigeer);
        studentenlijstLL = (LinearLayout) findViewById(R.id.ll_registratie1_persoonsgegevens);
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        if (Utils.isNetworkAvailable(this))
            new HttpDataRequestTask().execute();
        else {
            //studentenlijstArray = new ArrayList<>(Subscription.listAll(Subscription.class));
            setupAdapters();
        }
        //screensaverDialog = new ScreensaverDialog(this, R.style.screensaver_dialog);
        //startScreensaverThread();

        mStudententSV.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (v.getId() == mStudententSV.getId()) {
                    if (hasFocus) {
                        int[] loc = new int[2];
                        v.getLocationOnScreen(loc);
                        studentenlijstLL.scrollBy(0, 80);
                    }
                }

            }
        });


    }

    public void goToHeatMap(View V) {
        Intent i = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(i);
    }

    private void setupDesign() {
        mStudententSV = (SearchView) findViewById(R.id.sv_studentenlijst);
        studentenlijstLV = (ListView) findViewById(R.id.lv_studentenlijst);
        progressOverlay = findViewById(R.id.progress_overlay);
        studentenlijstLV.setTextFilterEnabled(true);
        setupSearchView();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private void setupAdapters() {

        slAdapter = new StudentenlijstAdapter(this, studentenlijstArray);

        studentenlijstLV.setAdapter(slAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private void setupSearchView() {
        mStudententSV.setIconifiedByDefault(false);
        mStudententSV.setOnQueryTextListener(this);
        mStudententSV.setSubmitButtonEnabled(true);

        mStudententSV.setQueryHint("Zoek studenten");

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            studentenlijstLV.clearTextFilter();

        } else {
            slAdapter.getFilter().filter(newText);
            studentenlijstLV.setFilterText(newText.toString());

        }

        return true;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        DATALIST PAGER                                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        lastUsed = System.currentTimeMillis();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR DATA RETRIEVAL                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void startScreensaverThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long idle = 0;
                Log.d("started", "the screensaver has started");
                do {
                    idle = System.currentTimeMillis() - lastUsed;
                    Log.d("something", "Application is idle for " + idle + " ms");

                    if (idle > 5000) {
                        if (!screensaverDialog.isShowing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    screensaverDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            stopScreenSaver = false;
                                        }
                                    });
                                    screensaverDialog.show();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    screensaverDialog.screensaverIV.setImageBitmap(screensaverDialog.bitmapArray.get(screensaverDialog.nextImage()));
                                }
                            });

                        }
                    }
                    try {
                        Thread.sleep(5000); //check every 5 seconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (!stopScreenSaver);
            }

        });
        thread.start();

    }

    private class DatalistPagerAdapter extends FragmentStatePagerAdapter {

        Fragment formPart1, formPart2;

        public DatalistPagerAdapter(FragmentManager supportFragmentManager, Fragment formPart1, Fragment formPart2) {
            super(supportFragmentManager);
            this.formPart1 = formPart1;
            this.formPart2 = formPart2;

        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return formPart1;
                case 1:
                    return formPart2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR DATA RETRIEVAL                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class HttpDataRequestTask extends AsyncTask<Void, Void, Void> {


        private String server;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading wheel
            Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
            server = preferences.getString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}");
            //show toast for loading data
            Toast
                    .makeText(getApplicationContext(), "Loading subscriptions", Toast.LENGTH_SHORT)
                    .show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (preferences.getBoolean("debugging", false))
                android.os.Debug.waitForDebugger();

            RestTemplate restTemplate = new RestTemplate();
            //get data from webservice
            ArrayList<LocalSubscription> subList = new ArrayList<>(LocalSubscription.listAll(LocalSubscription.class));
            studentenlijstArray = Subscription.transformLSubscription(subList);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);

            setupAdapters();

        }

    }

}



