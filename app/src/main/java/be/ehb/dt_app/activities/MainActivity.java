package be.ehb.dt_app.activities;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.Image;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.Teacher;
import be.ehb.dt_app.model.TeacherList;


public class MainActivity extends Activity {

    //DATA SOURCES URL CONFIGURATION
    private static final String SERVER = "http://vdabsidin.appspot.com/rest/{required_dataset}";
    private static final String EVENTS_LIST_URL = "events";
    private static final String TEACHERS_LIST_URL = "teachers";
    private static final String SCHOOLS_LIST_URL = "schools";
    private static final String SUBSCRIPTIONS_LIST_URL = "subscriptions";
    //GLOBAL VARIABLES FOR PRACTICAL USE
    protected boolean just_started = true;
    //GRAPHICAL ELEMENTS AND DATA LISTS FOR ADAPTERS DECLARATION
    LinearLayout lgnCenterLayout;
    View lay,progressOverlay;
    Spinner docentSP, eventSP;
    EventList eventList;
    TeacherList teacherList;
    SchoolList schoolList;
    ArrayAdapter<Event> eventAdapter;
    private ArrayAdapter<Teacher> teacherAdapter;
    private ArrayAdapter<School> schoolAdapter;
    //DEBUG APPLICATION
    private boolean debugging = Debug.isDebuggerConnected();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setUpDesign();
        List<Event> events = new ArrayList<>();
        eventList = new EventList();
        eventList.setEvents(events);

        if (Utils.isNetworkAvailable(this))
            new HttpRequestEventsTask().execute("teachers", "events");


        //eventSP.setAdapter(eventAdapter);




    }



    public void setUpDesign(){

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
            if (just_started) {
                Utils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
                Toast.makeText(getApplicationContext(), "Please wait while loading data", Toast.LENGTH_LONG);
                docentSP.setEnabled(false);
                eventSP.setEnabled(false);
            }
        }

        @Override
        protected HashMap<String, ArrayAdapter> doInBackground(String... params) {
            if (debugging)
                android.os.Debug.waitForDebugger();


            HashMap<String,ArrayAdapter> adaptersList = new HashMap<>();
            Class convertClass;
            ArrayAdapter dataAdapter = null;
            RestTemplate restTemplate = new RestTemplate();

            for (String requestedData : params) {

                switch (requestedData) {
                    case "events":
                        EventList eventList;
                        eventList = restTemplate.getForObject(SERVER, EventList.class, requestedData);
                        eventAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.ehb_spinner_list_item, R.id.tv_spinneritem, eventList.getEvents());
                        adaptersList.put(requestedData, eventAdapter);
                        for (Event item : eventList.getEvents()) {
                            item.save();
                        }
                        break;
                    case "teachers":
                        TeacherList teacherList;
                        teacherList = restTemplate.getForObject(SERVER, TeacherList.class, requestedData);
                        teacherAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, teacherList.getTeachers());
                        adaptersList.put(requestedData, teacherAdapter);
                        for (Teacher item : teacherList.getTeachers()) {
                            item.save();
                        }
                        break;
                    case "schools":
                        SchoolList schoolList;
                        schoolList = restTemplate.getForObject(SERVER, SchoolList.class, requestedData);
                        schoolAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, schoolList.getSchools());
                        adaptersList.put(requestedData, teacherAdapter);
                        for (School item : schoolList.getSchools()) {
                            item.save();
                        }
                        break;
                    case "subscriptions":
                        convertClass = Subscription.class;
                        break;
                    case "images":
                        convertClass = Image.class;
                        break;
                    default:
                        convertClass = null;
                        dataAdapter = null;
                        break;
                }

            }


            adaptersList.put("events", eventAdapter);
            adaptersList.put("teachers", teacherAdapter);

            return adaptersList;
        }

        @Override
        protected void onPostExecute(HashMap<String, ArrayAdapter> arrayAdapters) {
            super.onPostExecute(arrayAdapters);
            String toastMessage = "";
            if(!(arrayAdapters.containsKey("events")|| arrayAdapters.containsKey("teachers"))) {
                toastMessage = "There has been a problem downloading data. Please check your internet connection";
            }
            else
            {
                eventSP.setAdapter(arrayAdapters.get("events"));
                eventSP.setEnabled(true);

                docentSP.setAdapter(arrayAdapters.get("teachers"));
                docentSP.setEnabled(true);
                toastMessage = "Please select a teacher and event to proceed";
            }

            Utils.animateView(progressOverlay, View.GONE, 0.4f, 200);


            Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG);
            just_started = false;
        }


    }


}