package be.ehb.dt_app.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.springframework.web.client.RestTemplate;

import be.ehb.dt_app.R;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.SchoolList;
import be.ehb.dt_app.model.TeacherList;


public class MainActivity extends Activity {
    LinearLayout horizontaalLogin;
    View lay;
    Spinner docentSP, eventSP;
    EventList eventList;
    TeacherList teacherList;
    SchoolList schoolList;
    private String server = "vdabsidin.appspot.com/rest/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setUpDesign();

        new HttpRequestTask();
    }



    public void setUpDesign(){


        horizontaalLogin = (LinearLayout) findViewById(R.id.LinearLayout_loginscreen);


        lay = findViewById(R.id.RelativeLayout);
        docentSP = (Spinner) findViewById(R.id.sp_docent);
        eventSP = (Spinner) findViewById(R.id.sp_event);

        lay.setBackgroundResource(R.drawable.achtergrond2);

        int pic = R.drawable.achtergrond2;
        lay.setBackgroundResource(pic);

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

    private class HttpRequestTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {

            android.os.Debug.waitForDebugger();

            RestTemplate restTemplate = new RestTemplate();

            eventList = restTemplate.getForObject(server,EventList.class,"events");
            teacherList = restTemplate.getForObject(server,TeacherList.class,"teachers");
            schoolList = restTemplate.getForObject(server,SchoolList.class,"schools");


            ArrayAdapter<Event> eventAdapter = new ArrayAdapter<Event>(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    eventList.getEvents());

            eventSP.setAdapter(eventAdapter);


            return null;
        }
    }
}