package be.ehb.dt_app.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Debug;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;
import be.ehb.dt_app.model.TeacherList;

/**
 * Created by Mattia on 08/06/15.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected)
            new HttpDataRequestTask().execute();


    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR DATA RETRIEVAL                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class HttpDataRequestTask extends AsyncTask<Void, Void, HashMap<String, ArrayList>> {

        private SubscriptionsList subList;
        private String server;
        private RestTemplate restTemplate;
        private ArrayList listSubsClient;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading wheel

            server = "http://vdabsidin.appspot.com/rest/{required_dataset}";
            listSubsClient = new ArrayList(LocalSubscription.find(LocalSubscription.class, "SERVER_ID IS NULL"));
            ArrayList<Subscription> listSubsServer = Subscription.transformLSubscription(listSubsClient);
            subList = new SubscriptionsList();
            subList.setSubscriptions(listSubsServer);

            restTemplate = new RestTemplate();


            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);
        }


        @Override
        protected HashMap<String, ArrayList> doInBackground(Void... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();

            HashMap<String, ArrayList> dataLists = new HashMap<>();
            dataLists.put(
                    "events",
                    restTemplate.getForObject(server, EventList.class, "events").getEvents()
            );

            dataLists.put(
                    "teachers",
                    restTemplate.getForObject(server, TeacherList.class, "teachers").getTeachers()
            );
            dataLists.put(
                    "schools",
                    restTemplate.getForObject(server, SchoolList.class, "schools").getSchools()
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Subscription> entity;
            String server = "http://vdabsidin.appspot.com/rest/{required_dataset}";
            if (!subList.getSubscriptions().isEmpty()) {
                for (Subscription subscription : subList.getSubscriptions()) {
                    entity = new HttpEntity<>(subscription, headers);
                    restTemplate.exchange(server, HttpMethod.POST, entity, Subscription.class, "subscription");

                }
            }

            dataLists.put(
                    "subscriptions",
                    restTemplate.getForObject(server, SubscriptionsList.class, "subscriptions").getSubscriptions()
            );

            return dataLists;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList> dataLists) {
            super.onPostExecute(dataLists);

            Utils.persistDownloadedData(dataLists);
            LocalSubscription.deleteAll(LocalSubscription.class, "SERVER_ID IS NULL");
        }

    }

}

