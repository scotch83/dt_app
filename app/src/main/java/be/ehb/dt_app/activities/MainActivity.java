package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.Image;
import be.ehb.dt_app.model.ImageList;
import be.ehb.dt_app.model.ImageVersion;
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
    LinearLayout lgnCenterLayout;
    View lay, progressOverlay;
    Spinner docentSP, eventSP;
    EventList eventList;
    TeacherList teacherList;
    SchoolList schoolList;
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


        setUpDesign();


        if (Utils.isNetworkAvailable(this)) {

            restTemplate = new RestTemplate();
            new HttpRequestEventsTask().execute("teachers", "events", "schools", "subscriptions");

            SharedPreferences preferences = this.getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);
            int ourversion = preferences.getInt("Images Version Number", 1);
            int serverversion = restTemplate.getForObject(SERVER, ImageVersion.class, "imageversion").getVersion();
            if (ourversion > serverversion)
                new ImageAsyncDownload().execute();
        }

    }


    public void setUpDesign() {

        lgnCenterLayout = (LinearLayout) findViewById(R.id.lgn_screen_layout);
        progressOverlay = findViewById(R.id.progress_overlay);
        //assign spinners for latter assignment of teachers and events lists
        docentSP = (Spinner) findViewById(R.id.sp_docent);
        eventSP = (Spinner) findViewById(R.id.sp_event);


        //set ehb picture as background in de main layout element
        lay = findViewById(R.id.lgn_main_layout);
        lay.setBackgroundResource(R.drawable.achtergrond2);

        //set transparency for logo image
        ImageView logo = (ImageView) findViewById(R.id.iv_logo);
        logo.setAlpha(1f);

        //retrieve and hide action bar if present
//        getActionBar().hide();

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
            Class convertClass;
            ArrayAdapter dataAdapter = null;


            for (String requestedData : params) {

                switch (requestedData) {
                    case "events":
                        EventList eventList;
                        eventList = restTemplate.getForObject(SERVER, EventList.class, requestedData);
                        eventAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, eventList.getEvents());
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
                        adaptersList.put(requestedData, teacherAdapter);
                        for (School item : schoolList.getSchools()) {
                            if (School.findById(School.class, item.getId()) == null)
                                item.save();
                        }
                        break;
                    case "subscriptions":
                        SubscriptionsList subscriptionsList;
                        subscriptionsList = restTemplate.getForObject(SERVER, SubscriptionsList.class, requestedData);
                        subscriptionAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, subscriptionsList.getSubscriptions());
                        adaptersList.put(requestedData, teacherAdapter);
                        for (Subscription item : subscriptionsList.getSubscriptions()) {
                            if (Subscription.findById(Subscription.class, item.getId()) == null)
                                item.save();
                        }
                        break;

                    default:

                        break;
                }

            }


            adaptersList.put("events", eventAdapter);
            adaptersList.put("teachers", teacherAdapter);
            adaptersList.put("subscriptions", subscriptionAdapter);
            adaptersList.put("schools", schoolAdapter);

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

    private class ImageAsyncDownload extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            ImageVersion version_check = restTemplate.getForObject(SERVER, ImageVersion.class, "imageversion");


            ImageList imageList = restTemplate.getForObject(SERVER, ImageList.class, "images");
            for (Image item : imageList.getImages()) {
                Bitmap imageToSave = BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length);
                saveToInternalStorage(String.valueOf("EHBpicture_id_" + item.getId()) + ".jpg", imageToSave);
            }


            return null;
        }

        private String saveToInternalStorage(String fileName, Bitmap bitmapImage) {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            File mypath = new File(directory, fileName);

            Bitmap scaledImageToSave = Utils.scaleImage(bitmapImage, getApplicationContext());

            FileOutputStream fos = null;
            try {

                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                scaledImageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return directory.getAbsolutePath();
        }


    }
}