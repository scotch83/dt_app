package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.springframework.web.client.RestTemplate;

import be.ehb.dt_app.R;

public class DepartementLoginActivity extends Activity implements View.OnClickListener {

    private static final String SERVER_DEP = "http://deptcodes.appspot.com/deptcode/{variable}";
    private SharedPreferences preferences;
    private Button startBtn;
    private EditText codeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departement_login);
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);
        if (preferences.contains("server")) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        codeTv = (EditText) findViewById(R.id.et_departement_code);
        startBtn = (Button) findViewById(R.id.btn_import_data);
        startBtn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_departement_login, menu);
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

    @Override
    public void onClick(View v) {

        new AsyncRetrieveServerUrl().execute();
    }

    private class AsyncRetrieveServerUrl extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            RestTemplate restTemplate = new RestTemplate();
            String code = String.valueOf(codeTv.getText());
            String server_url = restTemplate.getForObject(SERVER_DEP, String.class, code);
            preferences.edit().putString("server", "http://" + server_url + "/rest/{required_dataset}").apply();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
