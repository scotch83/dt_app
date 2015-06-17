package be.ehb.dt_app.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Debug;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;
import be.ehb.dt_app.model.Teacher;
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


        private Integer serverversion;
        private int ourversion;
        private String server;
        private RestTemplate restTemplate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading wheel

            server = "http://vdabsidin.appspot.com/rest/{required_dataset}";

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
            dataLists.put(
                    "subscriptions",
                    restTemplate.getForObject(server, SubscriptionsList.class, "subscriptions").getSubscriptions()
            );

            return dataLists;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList> dataLists) {
            super.onPostExecute(dataLists);

            persistDownloadedData(dataLists);
        }

        private void persistDownloadedData(HashMap<String, ArrayList> dataLists) {

            Iterator it = dataLists.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                switch (pair.getKey().toString()) {
                    case "events":
                        for (Event event : (ArrayList<Event>) pair.getValue())
                            if (Event.findById(Event.class, event.getId()) == null) {
                                event.save();
                            }

                        break;
                    case "teachers":
                        for (Teacher teacher : (ArrayList<Teacher>) pair.getValue())
                            if (Teacher.findById(Teacher.class, teacher.getId()) == null) {

                                teacher.save();
                            }
                        break;
                    case "subscriptions":
                        for (Subscription subscription : (ArrayList<Subscription>) pair.getValue()) {
                            LocalSubscription lSub = new LocalSubscription(subscription);
                            if (LocalSubscription.findById(LocalSubscription.class, lSub.getId()) == null) {
                                lSub.save();
                            }
                        }
                        break;
                    case "schools":
                        for (School school : (ArrayList<School>) pair.getValue())
                            if (School.findById(School.class, school.getId()) == null) {

                                school.save();
                            }
                        break;

                }
            }
        }
    }


}

