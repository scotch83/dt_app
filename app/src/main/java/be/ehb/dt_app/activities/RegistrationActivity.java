package be.ehb.dt_app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.ArrayList;
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

    protected Fragment form1, form2;
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
//        stad_secundaire_onderwijs = (AutoCompleteTextView) findViewById(R.id.et_stad_secundaireschool);
//        stad_secundaire_onderwijs.setThreshold(Integer.MAX_VALUE);
//        //stad_secundaire_onderwijs.setLi
    }

    public void sendData(View v) {


        ArrayList<String> dataToSend = new ArrayList<>();

        Subscription newSubscription = new Subscription();

        EditText temp;
        //fields from first form
        temp = (EditText) findViewById(R.id.et_voornaam);
        newSubscription.setFirstName(temp.getText().toString());

        temp = (EditText) findViewById(R.id.et_achternaam);
        newSubscription.setLastName(temp.getText().toString());

        temp = (EditText) findViewById(R.id.et_email);
        newSubscription.setEmail(temp.getText().toString());

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
        School school = new School("Sint-Jan Berchmanscollege", "Brussel-Stad", (short) 1000, 5969014819913728l);
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
        ObjectMapper om = new ObjectMapper();
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

        if (Utils.isNetworkAvailable(this)) {
            new SaveAsynctask().execute(newSubscription);
        } else {
            LocalSubscription subToStore = new LocalSubscription(newSubscription);
            subToStore.save();
        }
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

//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        lastUsed = System.currentTimeMillis();
//    }

//    public void startScreensaverThread() {
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                long idle = 0;
////                Log.d("started", "the screensaver has started");
//                do {
//                    idle = System.currentTimeMillis() - lastUsed;
////                    Log.d("something", "Application is idle for " + idle + " ms");
//
//                    if (idle > 5000) {
//                        if (!screensaverDialog.isShowing()) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    screensaverDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                        @Override
//                                        public void onDismiss(DialogInterface dialog) {
//                                            stopScreenSaver = false;
//                                        }
//                                    });
//                                    screensaverDialog.show();
//                                }
//                            });
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    screensaverDialog.screensaverIV.setImageBitmap(screensaverDialog.bitmapArray.get(screensaverDialog.nextImage()));
//                                }
//                            });
//
//                        }
//                    }
//                    try {
//                        Thread.sleep(5000); //check every 5 seconds
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                while (!stopScreenSaver);
//            }
//
//        });
//        thread.start();
//
//    }


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
                android.os.Debug.waitForDebugger();


            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<Subscription> entity = new HttpEntity<>(params[0], headers);
            restTemplate.exchange(server, HttpMethod.POST, entity, Subscription.class, "subscription");
            return null;
        }
    }
}
