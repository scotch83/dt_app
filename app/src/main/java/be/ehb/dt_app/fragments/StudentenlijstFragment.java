package be.ehb.dt_app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import be.ehb.dt_app.R;
import be.ehb.dt_app.adapters.StudentenlijstAdapter;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;


public class StudentenlijstFragment extends Fragment {

    public StudentenlijstFragment studentenlijstFragment = null;
    public ArrayList<Subscription> studentenlijstArray = new ArrayList<Subscription>();
    ListView studentenlijstLV;
    StudentenlijstAdapter slAdapter;
    private View progressOverlay;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_studentenlijst, container, false);

        preferences = getActivity().getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);


        studentenlijstLV = (ListView) v.findViewById(R.id.lv_studentenlijst);
        progressOverlay = v.findViewById(R.id.progress_overlay);


        new HttpDataRequestTask().execute();


        return v;
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
                    .makeText(getActivity().getApplicationContext(), "Loading subscriptions", Toast.LENGTH_SHORT)
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

            slAdapter = new StudentenlijstAdapter(getActivity(), studentenlijstArray);

            studentenlijstLV.setAdapter(slAdapter);
            persistDownloadedData(studentenlijstArray);
        }

        private void persistDownloadedData(ArrayList<Subscription> dataLists) {
            for (Subscription subscription : dataLists)
                if (Subscription.findById(Subscription.class, subscription.getId()) == null) {
                    subscription.save();
                }
        }
    }


}
