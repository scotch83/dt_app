package be.ehb.dt_app.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.Utils;
import be.ehb.dt_app.controller.ZoomOutPageTransformer;
import be.ehb.dt_app.fragments.RegistrationFragment;
import be.ehb.dt_app.fragments.RegistrationFragment2;
import be.ehb.dt_app.fragments.ScreensaverDialog;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.Teacher;

public class RegistrationActivity extends ActionBarActivity {

    protected RegistrationFragment form1;
    protected RegistrationFragment2 form2;
    long lastUsed = System.currentTimeMillis();
    boolean stopScreenSaver;
    ScreensaverDialog screensaverDialog;
    private ViewPager mPagerRegistratie;
    private PagerAdapter mPagerAdapter;
    private ImageView img_page1, img_page2;
    private TextView page1TV, page2TV;
    private LinearLayout vpindicatorLL;
    private SharedPreferences preferences;
    private String server;
    private AutoCompleteTextView stad_secundaire_onderwijs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        form1 = new RegistrationFragment();
        form2 = new RegistrationFragment2();

        initializeDesign();

        initializePager();

        server = preferences.getString("server", "http://vdabsidin.appspot.com/rest/{required_dataset}");


    }

    private void initializeDesign() {
        img_page1 = (ImageView) findViewById(R.id.iv_page1);
        img_page2 = (ImageView) findViewById(R.id.iv_page2);
        page1TV = (TextView) findViewById(R.id.tv_pagina1);
        page2TV = (TextView) findViewById(R.id.tv_pagina2);

    }

    public void sendData(View v) {




        Subscription newSubscription = new Subscription();

        EditText temp;

        temp = (EditText) findViewById(R.id.et_email);
        String email = temp.getText().toString();
        if (Subscription.isValidEmail(email)) {

            //fields from first form
            newSubscription.setEmail(email);
            temp = (EditText) findViewById(R.id.et_voornaam);
            newSubscription.setFirstName(temp.getText().toString());


            temp = (EditText) findViewById(R.id.et_achternaam);
            newSubscription.setLastName(temp.getText().toString());


            temp = (EditText) findViewById(R.id.et_straatnaam);
            newSubscription.setStreet(temp.getText().toString());

            temp = (EditText) findViewById(R.id.et_straatnummer);
            newSubscription.setStreetNumber(temp.getText().toString());

            temp = (EditText) findViewById(R.id.et_stad);
            newSubscription.setCity(temp.getText().toString());

            temp = (EditText) findViewById(R.id.et_postcode);
            newSubscription.setZip(temp.getText().toString());

            //fields from second form

            //SCHOOL
//        EditText tempSp = (EditText) findViewById(R.id.sp_secundaire_school);
            School school = new School();
            temp = (EditText) findViewById(R.id.et_stad_secundaireschool);

            school.setGemeente(String.valueOf(temp.getText()));

            school.setPostcode(form2.getSchoolzoeker().getPostcode());
            school.setServerId(form2.getSchoolzoeker().getSchoolId());

            temp = (EditText) findViewById(R.id.sp_secundaire_school);
            school.setName(String.valueOf(temp.getText()));

            newSubscription.setSchool(school);

            //INTERESTS
            HashMap<String, String> interests = new HashMap<>();
            CheckBox cbTemp = (CheckBox) findViewById(R.id.cb_digx);
            interests.put("digx", String.valueOf(cbTemp.isChecked()));
            cbTemp = (CheckBox) findViewById(R.id.cb_multex);
            interests.put("multec", String.valueOf(cbTemp.isChecked()));
            Switch wrkStud = (Switch) findViewById(R.id.sw_werkstudent);
            interests.put("werkstudent", String.valueOf(wrkStud.isChecked()));
            newSubscription.setInterests(interests);

            //Teacher and event
            String jsonTeacher = preferences.getString("Teacher", "(iets misgelopen. Neem contact met de ICT dienst.)");
            String jsonEvent = preferences.getString("Event", "(iets misgelopen. Neem contact met de ICT dienst.)");

            ObjectMapper jxson = new ObjectMapper();
            try {
                Teacher docent = jxson.readValue(jsonTeacher, Teacher.class);
                Event event = jxson.readValue(jsonEvent, Event.class);

                newSubscription.setTeacher(docent);
                newSubscription.setEvent(event);

            } catch (IOException e) {
                e.printStackTrace();
            }

            newSubscription.setTimestamp(new Date());

            //if fields are empty nothing happens and an error is shown
            if (newSubscription.isValidForSaving()) {
                if (Utils.isNetworkAvailable(this)) {
                    new SaveAsynctask().execute(newSubscription);
                } else {
                    LocalSubscription subToStore = new LocalSubscription(newSubscription);

                    subToStore.save();
                }
                //finish();

            } else {
                showError("Lege velden", "Sommige velden in het formulier zijn leeg", "Terug");

            }
        } else {
            showError("Email onjuist", "Jouwe email is onjuist. Geef een geldige email in, dank u.", "Terug");
        }
    }

    private void showError(String title, String msg, String btn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setTitle(title).setPositiveButton(btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializePager() {

        mPagerRegistratie = (ViewPager) findViewById(R.id.pager_registratie);

        mPagerAdapter = new RegistratiePagerAdapter(getSupportFragmentManager(), form1, form2);
        vpindicatorLL = (LinearLayout) findViewById(R.id.ll_viewpagerindicator);

        mPagerRegistratie.setAdapter(mPagerAdapter);
        mPagerRegistratie.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerRegistratie.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        img_page1.setImageResource(R.drawable.dotselectedblack);
                        page1TV.setTextColor(Color.RED);
                        page2TV.setTextColor(Color.BLACK);
                        img_page2.setImageResource(R.drawable.dotunselectedblack);

                        break;

                    case 1:
                        img_page1.setImageResource(R.drawable.dotunselectedblack);
                        img_page2.setImageResource(R.drawable.dotselectedblack);
                        page2TV.setTextColor(Color.RED);
                        page1TV.setTextColor(Color.BLACK);

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        screensaverDialog = new ScreensaverDialog(this, R.style.screensaver_dialog);
//        startScreensaverThread();

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


    public void scrollIndicator() {

    }

    /**
     * private class RegistratiePagerAdapter geeft het juiste fragment voor de positie waarin de viewpager zich bevindt.
     */
    private class RegistratiePagerAdapter extends FragmentStatePagerAdapter {

        Fragment formPart1, formPart2;

        public RegistratiePagerAdapter(FragmentManager supportFragmentManager, Fragment formPart1, Fragment formPart2) {
            super(supportFragmentManager);
            this.formPart1 = formPart1;
            this.formPart2 = formPart2;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return formPart1;
                case 1:
                    return formPart2;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class SaveAsynctask extends AsyncTask<Subscription, Void, Void> {


        @Override
        protected Void doInBackground(Subscription... params) {
            if (preferences.getBoolean("debuggin", false))
                Debug.waitForDebugger();


            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Subscription> entity = new HttpEntity<>(params[0], headers);
            restTemplate.exchange(server, HttpMethod.POST, entity, Subscription.class, "subscription");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(i);
        }
    }
}
