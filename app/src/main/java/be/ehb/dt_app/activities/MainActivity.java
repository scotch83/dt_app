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
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
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

    View progressOverlay, lay;
    Spinner docentSP, eventSP;
    SharedPreferences preferences;
    private RestTemplate restTemplate;
    private ArrayAdapter<Event> eventAdapter;
    private ArrayAdapter<Teacher> teacherAdapter;
    private ArrayAdapter<School> schoolAdapter;
    private ArrayAdapter<Subscription> subscriptionAdapter;
    //DEBUG APPLICATION
    private boolean debugging = Debug.isDebuggerConnected();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);


        setUpDesign();

        lay = findViewById(R.id.lgn_main_layout);


        if (Utils.isNetworkAvailable(this)) {

            restTemplate = new RestTemplate();
            new HttpRequestEventsTask().execute("teachers", "events", "schools");

        }

    }

    public void loginClicked(View v) {
        Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);

        preferences.edit().putString("Teacher", docentSP.getSelectedItem().toString()).commit();

        preferences.edit().putString("Event", eventSP.getSelectedItem().toString()).commit();
        startActivity(i);
    }

    public void setUpDesign() {

        progressOverlay = findViewById(R.id.progress_overlay);
        //assign spinners for latter assignment of teachers and events lists
        docentSP = (Spinner) findViewById(R.id.sp_docent);
        eventSP = (Spinner) findViewById(R.id.sp_event);

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


    private class HttpRequestEventsTask extends AsyncTask<String, Void, HashMap<String, ArrayAdapter>> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
            Toast.makeText(getApplicationContext(), "Please wait while loading data", Toast.LENGTH_LONG);
            docentSP.setEnabled(false);
            eventSP.setEnabled(false);
        }


        @Override
        protected HashMap<String, ArrayAdapter> doInBackground(String... params) {
            //if running in debug mode waitForDebugger to debug thread
            if (debugging)
                android.os.Debug.waitForDebugger();


            HashMap<String, ArrayAdapter> adaptersList = new HashMap<>();

            for (String requestedData : params) {

                switch (requestedData) {
                    case "events":
                        EventList eventList;
                        eventList = restTemplate.getForObject(SERVER, EventList.class, requestedData);
                        eventAdapter = new ArrayAdapter<>(getApplicationContext(),
                                android.R.layout.simple_spinner_item,
                                eventList.getEvents());
                        adaptersList.put(requestedData, eventAdapter);
                        for (Event item : eventList.getEvents()) {
                            if (Event.findById(Event.class, item.getId()) == null)
                                item.save();
                        }
                        break;
                    case "teachers":
                        TeacherList teacherList;
                        teacherList = restTemplate.getForObject(SERVER, TeacherList.class, requestedData);
                        teacherAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, teacherList.getTeachers());
                        adaptersList.put(requestedData, teacherAdapter);
                        for (Teacher item : teacherList.getTeachers()) {
                            if (Teacher.findById(Teacher.class, item.getId()) == null)
                                item.save();
                        }
                        break;
                    case "schools":
                        SchoolList schoolList;
                        schoolList = restTemplate.getForObject(SERVER, SchoolList.class, requestedData);
                        schoolAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, schoolList.getSchools());
                        adaptersList.put(requestedData, schoolAdapter);
                        for (School item : schoolList.getSchools()) {
                            if (School.findById(School.class, item.getId()) == null)
                                item.save();
                        }
                        break;
                    case "subscriptions":
                        SubscriptionsList subscriptionsList;
                        subscriptionsList = restTemplate.getForObject(SERVER, SubscriptionsList.class, requestedData);
                        subscriptionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, subscriptionsList.getSubscriptions());
                        adaptersList.put(requestedData, subscriptionAdapter);
                        for (Subscription item : subscriptionsList.getSubscriptions()) {
                            if (Subscription.findById(Subscription.class, item.getId()) == null)
                                item.save();
                        }
                        break;

                    default:

                        break;
                }

            }

            int ourversion = preferences.getInt("Images Version Number", 1);
            int serverversion = restTemplate.getForObject(SERVER, Integer.class, "imagesversion");
            if (ourversion < serverversion) {
                new ImageAsyncDownload().execute();
                preferences.edit().putInt("Image Version Number", serverversion);
            }

            return adaptersList;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayAdapter> arrayAdapters) {
            super.onPostExecute(arrayAdapters);
            String toastMessage = "";
            if (arrayAdapters.isEmpty()) {
                toastMessage = "There has been a problem downloading data. Please check your internet connection";
            } else {
                eventSP.setAdapter(arrayAdapters.get("events"));
                eventSP.setEnabled(true);

                docentSP.setAdapter(arrayAdapters.get("teachers"));
                docentSP.setEnabled(true);
                toastMessage = "Please select a teacher and event to proceed";
            }

            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);

            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);

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
//            lay.setBackground(Drawable.createFromPath(s));
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