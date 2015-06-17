package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.Image;
import be.ehb.dt_app.model.ImageList;
import be.ehb.dt_app.model.Teacher;
import be.ehb.dt_app.model.TeacherList;


public class MainActivity extends Activity {


    //In the "EHB App SharedPreferences" preferences are saved with the following names
    //"Image Version Number"
    //"debugging"
    //"server"
    //"Images path"
    //
    //When the login is performed, also
    //"Teacher"
    //and
    //"Event"
    //are saved for the selected "Teacher" and "Event". These are saved as a Json string, to be deserialized later
    //to objects without having to access the DB.

    private View progressOverlay;
    private Spinner docentSP, eventSP;
    private Button loginBTN;

    private SharedPreferences preferences;
    private ArrayList<Event> eventList;
    private ArrayList<Teacher> teacherList;

    //DEBUG APPLICATION
    private boolean debugging = Debug.isDebuggerConnected();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrieve SharedPreferences in global var
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("debugging", Debug.isDebuggerConnected());
        preferences.edit().putString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}");
        preferences.getBoolean("greetings", true);
        //setup needed design elements
        setUpDesign();
        //try to initializate dataLists with existing data in DB
        eventList = (ArrayList<Event>) Event.listAll(Event.class);
        teacherList = (ArrayList<Teacher>) Teacher.listAll(Teacher.class);
        //if network is available, load data from server
        if (Utils.isNetworkAvailable(this)) {
            new HttpDataRequestTask().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Er kon geen internet verbinding gemaakt worden. Data kan niet geupdate zijn.", Toast.LENGTH_SHORT);
        }
    }


    public void setUpDesign() {

        progressOverlay = findViewById(R.id.progress_overlay);
        //assign spinners for latter assignment of teachers and events lists
        docentSP = (Spinner) findViewById(R.id.sp_docent);
        eventSP = (Spinner) findViewById(R.id.sp_event);
        loginBTN = (Button) findViewById(R.id.btn_login);
        //lock all UI elements to avoid user interaction until data has not been load
        docentSP.setEnabled(false);
        eventSP.setEnabled(false);
        loginBTN.setEnabled(false);

    }

    private void setupLoginAdapters() {
        //setup adapters for interface
        ArrayAdapter<Event> eventAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.ehb_spinner_list_item,
                R.id.sub_text_seen,
                eventList
        );

        ArrayAdapter<Teacher> teacherAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.ehb_spinner_list_item,
                R.id.sub_text_seen,
                teacherList
        );


        eventSP.setAdapter(eventAdapter);
        docentSP.setAdapter(teacherAdapter);

    }

    public void loginClicked(View v) {

        Teacher docent = (Teacher) docentSP.getSelectedItem();
        Event event = (Event) eventSP.getSelectedItem();

        ObjectWriter jxson = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            preferences.edit().putString("Teacher", jxson.writeValueAsString(docent)).apply();
            preferences.edit().putString("Event", jxson.writeValueAsString(event)).apply();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivity(i);

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR DATA RETRIEVAL                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private class HttpDataRequestTask extends AsyncTask<String, Void, HashMap<String, ArrayList>> {


        private Integer serverversion;
        private int ourversion;
        private String server;
        private RestTemplate restTemplate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading wheel
            Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
            //show toast for loading data
            Toast
                    .makeText(getApplicationContext(), "Please wait while loading data", Toast.LENGTH_SHORT)
                    .show();
            server = preferences.getString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}");

            restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);
        }


        @Override
        protected HashMap<String, ArrayList> doInBackground(String... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (debugging)
                android.os.Debug.waitForDebugger();


            HashMap<String, ArrayList> dataLists = new HashMap<>();


            eventList = restTemplate.getForObject(server, EventList.class, "events").getEvents();
            dataLists.put("events", eventList);

            teacherList = restTemplate.getForObject(server, TeacherList.class, "teachers").getTeachers();
            dataLists.put("teachers", teacherList);




            //get own image version from SharedPreferences
            ourversion = preferences.getInt("Images Version Number", 1);
            //get Image version from the server to state if images need to be downloaded
            serverversion = restTemplate.getForObject(server, Integer.class, "imagesversion");

            return dataLists;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayList> dataLists) {
            super.onPostExecute(dataLists);

            setupLoginAdapters();

            String toastMessage = "";


            if (!(eventList.isEmpty() || teacherList.isEmpty())) {
                toastMessage = "Please select a teacher and event to login";
            } else
                toastMessage = "Er is een fout opgetreden bij het ophalen van de gegevens. Neem contact op met de ICT dienst.";

            Toast
                    .makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT)
                    .show();



            if (ourversion < serverversion) {
                //if Image version lower than the one on the server, download images
                new ImageAsyncDownload().execute();
                preferences.edit().putInt("Image Version Number", serverversion).apply();
            }

            persistDownloadedData(dataLists);


            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);
            loginBTN.setEnabled(true);
            eventSP.setEnabled(true);
            docentSP.setEnabled(true);

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

                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR IMAGES RETRIEVAL                        ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class ImageAsyncDownload extends AsyncTask<Void, Void, String> {
        private RestTemplate restTemplate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            preferences.edit().putString("Images path", s);

        }

        @Override
        protected String doInBackground(Void... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (debugging)
                android.os.Debug.waitForDebugger();

            ImageList imageList = restTemplate.getForObject(preferences.getString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}"), ImageList.class, "images");
            for (Image item : imageList.getImages()) {
                Bitmap imageToSave = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                Utils.saveImage(String.valueOf("EHBpicture_id_" + item.getId()) + ".jpg", imageToSave, getApplicationContext());
            }

            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            return cw.getDir("presentation_images", Context.MODE_PRIVATE).toString();
        }


    }
}