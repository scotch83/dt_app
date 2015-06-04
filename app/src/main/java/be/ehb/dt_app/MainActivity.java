package be.ehb.dt_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.TeacherList;


public class MainActivity extends Activity {

    private String username = "bert.developman@gmail.com";
    private String password = "mobapp1234ehb";
    private String url = "http://vdabsidin.appspot.com/rest/{variable}";
    private Event test_event;
    private String urlSubAdd = "http://vdabsidin.appspot.com/rest/event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new HttpRequestTask().execute();
        // Create a new RestTemplate instance


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

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            android.os.Debug.waitForDebugger();
            try {

//

//                random_event.setId(5153049148391424l);
//                message.setEvent(random_event);
//                message.setIsNew(true);
//                Teacher random_teacher = new Teacher();
//                random_teacher.setId(6283097188335616l);
//                random_teacher.setAcadyear((short)1415);
//                random_teacher.setName("Jan Alen");
//                message.setTeacher(random_teacher);

//                HttpHeaders requestHeaders = new HttpHeaders();
//                requestHeaders.setContentType(new MediaType("application","json"));
//                HttpEntity<Subscription> requestEntity = new HttpEntity<Subscription>(message, requestHeaders);
//
//// Create a new RestTemplate instance
//                RestTemplate restTemplate = new RestTemplate();
//
//// Make the HTTP POST request, marshaling the request to JSON, and the response to a String
//                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//                String greetings = responseEntity.getBody();
//                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                //opmaak van het object dat moet door gestuurd worden

//                Event message = new Event("JSON annotation event", (short) 1516);

// Create a new RestTemplate instance

                RestTemplate restTemplate = new RestTemplate();

                TeacherList teacherList = restTemplate.getForObject(
                        url, TeacherList.class, "teachers"
                );

                EventList eventList = restTemplate.getForObject(
                        url,
                        EventList.class,
                        "events"
                );
                Log.d("TEACHERS", teacherList.toString());
                Log.d("EVENTS", eventList.toString());
                HashMap<String, String> interests = new HashMap<>();
                interests.put("Dig-X", "true");
//                public Subscription(
// String firstName,
// String lastName,
// String email,
// String street,
// String streetNumber,
// String zip,
// String city,
// HashMap<String, String> interests,
// Date timestamp,
// Teacher teacher,
// Event event,
// boolean isNew,
// School school)

                Subscription message = new Subscription(
                        "Ciao",
                        "Sono uno studente",
                        "jos.dh@gmail.com",
                        "Acacialaan",
                        "153",
                        "1070",
                        "Gent",
                        interests,
                        new Date(),
                        teacherList.getTeachers().get(0),
                        eventList.getEvents().get(0),
                        true,
                        new School(
                                "Sint-Jan Berchmanscollege",

                                "Brussel-Stad", (short) 1000)

                );


                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application", "json"));
                HttpEntity<Subscription> requestEntity = new HttpEntity<Subscription>(message, requestHeaders);

//// Make the HTTP POST request, marshaling the request to JSON, and the response to a StringHash
                HashMap<String, String> vars = new HashMap<>();
                vars.put("variable", "subscription");
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class, "subscription");
                String event = "Something went wrong";
                if (responseEntity.hasBody())
                    event = responseEntity.getBody();
                else if (responseEntity.getStatusCode() == HttpStatus.OK)
                    event = "Upload succesful";
//                Log.d("XXXX",message.toString());
//                Log.d("XXXX",teacherList.getTeachers().get(0).toString());
//                Log.d("XXXX", eventList.getEvents().get(0).toString());
                return event;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return "testing the app";
        }

        @Override
        protected void onPostExecute(String greetings) {
            android.os.Debug.waitForDebugger();
            TextView test_txt = (TextView) findViewById(R.id.test_txt);
            test_txt.setText(greetings);

        }

    }

}
