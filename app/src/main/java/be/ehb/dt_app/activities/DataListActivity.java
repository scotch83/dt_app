package be.ehb.dt_app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.StudentenlijstAdapter;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;

public class DataListActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    //    protected Fragment form1, form2;
//    private ViewPager mPagerRegistratie;
//    private PagerAdapter mPagerAdapter;
    private ImageView img_page1, img_page2;
    private View progressOverlay;
    private SharedPreferences preferences;
    private ListView studentenlijstLV;
    private ArrayList<Subscription> studentenlijstArray = new ArrayList<>();
    private StudentenlijstAdapter slAdapter;
    private SearchView mStudententSV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        setupDesign();

        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        if (Utils.isNetworkAvailable(this))
            new HttpDataRequestTask().execute();
        else {
            //studentenlijstArray = new ArrayList<>(Subscription.listAll(Subscription.class));
            setupAdapters();
        }


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
            studentenlijstArray = restTemplate.getForObject(server, SubscriptionsList.class, "subscriptions").getSubscriptions();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);

            setupAdapters();
            persistDownloadedData(studentenlijstArray);
        }

        private void persistDownloadedData(ArrayList<Subscription> dataLists) {
//            for (Subscription subscription : dataLists)
//                if (Subscription.findById(Subscription.class, subscription.getId()) == null) {
//                    subscription.save();
//                }
        }
    }
}
