package be.ehb.dt_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.EventList;


public class MainActivity extends Activity {

    private String username = "bert.developman@gmail.com";
    private String password = "mobapp1234ehb";
    private String url = "http://vdabsidin.appspot.com/rest/events";
    private Event test_event;

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

    private class HttpRequestTask extends AsyncTask<Void, Void, EventList> {
        @Override
        protected EventList doInBackground(Void... params) {
            try {

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                EventList greetings = restTemplate.getForObject(url, EventList.class);
                return greetings;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(EventList greetings) {
            TextView test_txt = (TextView) findViewById(R.id.test_txt);
            test_txt.setText(greetings.toString());


        }

    }

}
