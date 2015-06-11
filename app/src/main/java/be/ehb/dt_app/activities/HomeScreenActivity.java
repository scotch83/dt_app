package be.ehb.dt_app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import be.ehb.dt_app.R;


public class HomeScreenActivity extends ActionBarActivity {


    private boolean once_only = true;
    private View lay;
    private ImageButton registrationBTN, datalistBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupDesign();
        if (once_only) {
            Toast.makeText(getApplicationContext(), "Welkom " + getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE).getString("Teacher", "(teacher not set)"), Toast.LENGTH_LONG).show();
            once_only = false;
        }

        datalistBTN = (ImageButton) findViewById(R.id.ib_todaylijst);
        datalistBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DataListActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupDesign() {

        lay = findViewById(R.id.rl_homescreen);
        lay.setBackgroundResource(R.drawable.achtergrond2);

        int pic = R.drawable.achtergrond2;
        lay.setBackgroundResource(pic);


    }

    public void registrationClicked(View v) {
        Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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
}
