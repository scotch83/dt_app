package be.ehb.dt_app.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;
import be.ehb.dt_app.model.Teacher;
import be.ehb.dt_app.model.TeacherList;

public class BeheerActivity extends Activity implements View.OnClickListener {

    private SharedPreferences preferences;
    private Spinner docentSP, eventSP;
    private Button saveBTN;
    private HashMap<String, ArrayList> dataLists = new HashMap<>();
    private View progressOverlay;
    private Button syncBTN;
    private Spinner timelapseSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beheer);

        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        setUpDesign();

        initDataFromDB();
        setupAdapters();


    }

    private void initDataFromDB() {

        dataLists.put("events", new ArrayList<Event>(Event.listAll(Event.class)));
        dataLists.put("teachers", new ArrayList<Teacher>(Teacher.listAll(Teacher.class)));

    }

    private void setupAdapters() {
        ArrayAdapter<Event> eventArrayAdapter = new ArrayAdapter<Event>(
                getApplicationContext(),
                R.layout.ehb_spinner_list_item,
                R.id.sub_text_seen,
                dataLists.get("events")
        );
        ArrayAdapter<Teacher> teacherArrayAdapter = new ArrayAdapter<Teacher>(
                getApplicationContext(),
                R.layout.ehb_spinner_list_item,
                R.id.sub_text_seen,
                dataLists.get("teachers")
        );

        eventSP.setAdapter(eventArrayAdapter);
        docentSP.setAdapter(teacherArrayAdapter);

        String jsonTeacher = preferences.getString("Teacher", "(iets misgelopen. Neem contact met de ICT dienst.)");
        String jsonEvent = preferences.getString("Event", "(iets misgelopen. Neem contact met de ICT dienst.)");
        ObjectMapper jxson = new ObjectMapper();
        int i = 0;
        try {
            Teacher docent = jxson.readValue(jsonTeacher, Teacher.class);
            while (!dataLists.get("teachers").get(i).equals(docent))
                i++;
            docentSP.setSelection(i);
            Event event = jxson.readValue(jsonEvent, Event.class);
            i = 0;
            while (!dataLists.get("events").get(i).equals(event))
                i++;
            eventSP.setSelection(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveBTN.setOnClickListener(this);


        ArrayList<String> timelapseValues = new ArrayList<>();
        timelapseValues.add("5 minutes");
        timelapseValues.add("10 minutes");
        timelapseValues.add("15 minutes");

        ArrayAdapter<String> timelapseAdapter = new ArrayAdapter<String>(this, R.layout.ehb_spinner_list_item, R.id.sub_text_seen,
                timelapseValues);
        i = 0;
        while (!timelapseValues.get(i).equals(String.valueOf(preferences.getInt("Screensaver timelapse", 300000) / 60000) + " minutes")) {
            i++;
        }
        timelapseSP.setAdapter(timelapseAdapter);
        timelapseSP.setSelection(i);

        saveBTN.setEnabled(true);
        eventSP.setEnabled(true);
        docentSP.setEnabled(true);
        timelapseSP.setEnabled(true);
        syncBTN.setEnabled(true);
    }

    public void setUpDesign() {


        progressOverlay = findViewById(R.id.progress_overlay);
        //assign spinners for latter assignment of teachers and events lists
        docentSP = (Spinner) findViewById(R.id.sp_beheer_docent);
        eventSP = (Spinner) findViewById(R.id.sp_beheer_event);
        saveBTN = (Button) findViewById(R.id.btn_save);
        syncBTN = (Button) findViewById(R.id.btn_sync);
        timelapseSP = (Spinner) findViewById(R.id.sp_timelapse_screensaver);
        //lock all UI elements to avoid user interaction until data has not been load
        docentSP.setEnabled(false);
        eventSP.setEnabled(false);
        saveBTN.setEnabled(false);
        syncBTN.setEnabled(false);
        timelapseSP.setEnabled(false);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beheer, menu);
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

    public void syncDataWithBackEnd(View v) {

        if (Utils.isNetworkAvailable(this)) {

            //sync data
            ArrayList<LocalSubscription> listSubsClient = new ArrayList(LocalSubscription.find(LocalSubscription.class, "SERVER_ID IS NULL"));
            ArrayList<Subscription> listSubsServer = Subscription.transformLSubscription(listSubsClient);
            SubscriptionsList listToUpload = new SubscriptionsList();
            listToUpload.setSubscriptions(listSubsServer);
            Toast.makeText(this, "Data is aan het syncroniseren.", Toast.LENGTH_LONG);
            new SynsAsync().execute(listToUpload);

        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);


            builder.setMessage("Verbind met het internet om de data te syncroniseren.")
                    .setTitle("Geen internet!").setPositiveButton("Probeer opnieuw", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            AlertDialog dialog = builder.create();
            dialog.setIcon(getResources().getDrawable(R.mipmap.ic_alert));
            dialog.show();
        }

    }

    @Override
    public void onClick(View v) {
        Teacher docent = (Teacher) docentSP.getSelectedItem();
        Event event = (Event) eventSP.getSelectedItem();

        ObjectWriter jxson = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            preferences.edit().putString("Teacher", jxson.writeValueAsString(docent)).apply();
            preferences.edit().putString("Event", jxson.writeValueAsString(event)).apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        int timelapse;
        switch (timelapseSP.getSelectedItem().toString()) {
            case "5 minutes":
                timelapse = 300000;
                break;
            case "10 minutes":
                timelapse = 600000;
                break;
            case "15 minutes":
                timelapse = 900000;
                break;
            default:
                timelapse = 300000;
                break;
        }

        preferences.edit().putInt("Screensaver timelapse", timelapse).apply();
        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivity(i);

    }


    private class SynsAsync extends AsyncTask<SubscriptionsList, Void, HashMap<String, ArrayList>> {

        private RestTemplate restTemplate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
            restTemplate = new RestTemplate();
            docentSP.setEnabled(false);
            eventSP.setEnabled(false);
            saveBTN.setEnabled(false);
            syncBTN.setEnabled(false);

        }

        @Override
        protected HashMap<String, ArrayList> doInBackground(SubscriptionsList... params) {

            if (preferences.getBoolean("debugging", false))
                Debug.waitForDebugger();

            String server = preferences.getString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}");

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Subscription> entity;
            try {
                if (!params[0].getSubscriptions().isEmpty()) {
                    for (Subscription subscription : params[0].getSubscriptions()) {
                        entity = new HttpEntity<>(subscription, headers);
                        restTemplate.exchange(server, HttpMethod.POST, entity, Subscription.class, "subscription");
                    }
                }

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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return dataLists;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList> dataLists) {
            super.onPostExecute(dataLists);
            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);
            Utils.persistDownloadedData(dataLists);

            docentSP.setEnabled(true);
            eventSP.setEnabled(true);
            saveBTN.setEnabled(true);
            syncBTN.setEnabled(true);
        }
    }
}
