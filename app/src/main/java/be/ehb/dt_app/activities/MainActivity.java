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

import com.google.gson.Gson;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.CustomGsonHttpMessageConverter;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.Image;
import be.ehb.dt_app.model.ImageList;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;
import be.ehb.dt_app.model.Teacher;
import be.ehb.dt_app.model.TeacherList;


public class MainActivity extends Activity {

    //DATA SOURCES URL CONFIGURATION and RestTemplate INITIALIZATION
    private static final String SERVER = "http://vdabsidin.appspot.com/rest/{required_dataset}";
    //GRAPHICAL ELEMENTS AND DATA LISTS FOR ADAPTERS DECLARATION

    private View progressOverlay;
    private Spinner docentSP, eventSP;
    private SharedPreferences preferences;
    private RestTemplate restTemplate;
    //    private ArrayAdapter<Event> eventAdapter;
//    private ArrayAdapter<Teacher> teacherAdapter;
//    private ArrayAdapter<School> schoolAdapter;
//    private ArrayAdapter<Subscription> subscriptionAdapter;
    private ArrayList<School> schoolList;
    private ArrayList<Event> eventList;
    private ArrayList<Subscription> subscriptionsList;
    private ArrayList<Teacher> teacherList;

    //DEBUG APPLICATION
    private boolean debugging = Debug.isDebuggerConnected();
    private Button loginBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);


        setUpDesign();


        if (Utils.isNetworkAvailable(this)) {

            restTemplate = new RestTemplate();

            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new CustomGsonHttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);
            new HttpRequestEventsTask().execute("teachers", "events"/*, "schools", "subscriptions"*/);
        } else {

            Toast.makeText(getApplicationContext(), "Er kon geen internet verbinding gemaakt worden. Data kan niet geupdate zijn.", Toast.LENGTH_SHORT);
        }

    }

    public void loginClicked(View v) {

        Teacher docent = (Teacher) docentSP.getSelectedItem();
        Event event = (Event) eventSP.getSelectedItem();

        Gson gson = new Gson();

        preferences.edit().putString("Teacher", gson.toJson(docent)).apply();
        preferences.edit().putString("Event", gson.toJson(event)).apply();

        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivity(i);

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


    private class HttpRequestEventsTask extends AsyncTask<String, Void, Void> {


        private Integer serverversion;
        private int ourversion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading wheel
            Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);


            //show toast for loading data
            Toast
                    .makeText(getApplicationContext(), "Please wait while loading data", Toast.LENGTH_SHORT)
                    .show();
        }


        @Override
        protected Void doInBackground(String... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (debugging)
                android.os.Debug.waitForDebugger();


            HashMap<String, ArrayAdapter> adaptersList = new HashMap<>();

            for (String requestedData : params) {

                switch (requestedData) {
                    case "events":
                        //get data from webservice
                        eventList = restTemplate.getForObject(SERVER, EventList.class, requestedData).getEvents();
                        for (Event event : eventList) {
                            if (Event.findById(Event.class, event.getId()) == null) {
                                event.save();
                            }
                        }
                        break;
                    case "teachers":
                        //get data from webservice
                        teacherList = restTemplate.getForObject(SERVER, TeacherList.class, requestedData).getTeachers();
                        for (Teacher teacher : teacherList) {
                            if (Teacher.findById(Teacher.class, teacher.getId()) == null)
                                teacher.save();
                        }
                        break;
                    case "schools":
                        //get data from webservice
                        schoolList = restTemplate.getForObject(SERVER, SchoolList.class, requestedData).getSchools();
                        for (School school : schoolList) {
                            if (School.findById(School.class, school.getId()) == null)
                                school.save();
                        }
                        break;
                    case "subscriptions":
                        //get data from webservice
                        subscriptionsList = restTemplate.getForObject(SERVER, SubscriptionsList.class, requestedData).getSubscriptions();
                        for (Subscription subscription : subscriptionsList)
                            if (Subscription.findById(Subscription.class, subscription.getId()) == null)
                                subscription.save();
                        break;
                    default:
                        break;
                }

            }

            //get own image version from SharedPreferences
            ourversion = preferences.getInt("Images Version Number", 1);
            //get Image version from the server to state if images need to be downloaded
            serverversion = restTemplate.getForObject(SERVER, Integer.class, "imagesversion");

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            String toastMessage = "";
            //setup adapters for interface
            ArrayAdapter<Event> eventAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.ehb_spinner_list_item,
                    R.id.sub_text_seen,
                    eventList
            );

            eventSP.setAdapter(eventAdapter);
            ArrayAdapter<Teacher> teacherAdapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.ehb_spinner_list_item,
                    R.id.sub_text_seen,
                    teacherList
            );

            eventSP.setAdapter(eventAdapter);

            docentSP.setAdapter(teacherAdapter);

            for (Event event : eventList) {


                if (Event.findById(Event.class, event.getId()) == null) {
                    event.save();
                }
            }

            if (!(eventList.isEmpty() || teacherList.isEmpty()))
                toastMessage = "Please select a teacher and event to proceed";
            else
                toastMessage = "Er is een fout optreden bij het ophalen van de gegevens. Neem contact op met de ICT dienst.";

            if (ourversion < serverversion) {
                //if Image version lower than the one on the server, download images
                //new ImageAsyncDownload().execute();
//                preferences.edit().putInt("Image Version Number", serverversion).apply();
            }

            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);

            Toast
                    .makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT)
                    .show();

            loginBTN.setEnabled(true);
            eventSP.setEnabled(true);
            docentSP.setEnabled(true);

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        ASYNC TASKS FOR IMAGES RETRIEVAL                        ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class ImageAsyncDownload extends AsyncTask<Void, Void, String> {

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

            ImageList imageList = restTemplate.getForObject(SERVER, ImageList.class, "images");
            for (Image item : imageList.getImages()) {
                Bitmap imageToSave = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                Utils.saveImage(String.valueOf("EHBpicture_id_" + item.getId()) + ".jpg", imageToSave, getApplicationContext());
            }

            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            return cw.getDir("presentation_images", Context.MODE_PRIVATE).toString();
        }


    }
}