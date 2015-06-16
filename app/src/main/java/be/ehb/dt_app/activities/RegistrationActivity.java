package be.ehb.dt_app.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.HashMap;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.ZoomOutPageTransformer;
import be.ehb.dt_app.fragments.RegistrationFragment;
import be.ehb.dt_app.fragments.RegistrationFragment2;
import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.Teacher;

public class RegistrationActivity extends ActionBarActivity {

    protected Fragment form1, form2;
    private ViewPager mPagerRegistratie;
    private PagerAdapter mPagerAdapter;
    private ImageView img_page1, img_page2;
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);

        form1 = new RegistrationFragment();
        form2 = new RegistrationFragment2();

        initializeDesign();

        initializePager();


    }

    private void initializeDesign() {
        img_page1 = (ImageView) findViewById(R.id.iv_page1);
        img_page2 = (ImageView) findViewById(R.id.iv_page2);
    }

    public void sendData(View v) {
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
        Spinner tempSp = (Spinner) findViewById(R.id.sp_secundaire_school);
        School school = new School("My school for test", "Gent", (short) 9000);
        newSubscription.setSchool(school);

        //INTERESTS
        HashMap<String, String> interests = new HashMap<>();
        interests.put("Dig-X", "true");
        interests.put("Multec", "false");
        interests.put("Werkstuden", "true");
        newSubscription.setInterests(interests);

        //Teacher and event
        Gson gson = new Gson();
        newSubscription.setTeacher(gson.fromJson(preferences.getString("Teacher", "None"), Teacher.class));
        newSubscription.setEvent(gson.fromJson(preferences.getString("Event", "None"), Event.class));

        Log.d("xxx", gson.toJson(newSubscription));
    }

    private void initializePager() {

        mPagerRegistratie = (ViewPager) findViewById(R.id.pager_registratie);

        mPagerAdapter = new RegistratiePagerAdapter(getSupportFragmentManager(), form1, form2);

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
                        img_page1.setImageResource(R.drawable.dotsselected);
                        img_page2.setImageResource(R.drawable.dotsunselected);

                        break;

                    case 1:
                        img_page1.setImageResource(R.drawable.dotsunselected);
                        img_page2.setImageResource(R.drawable.dotsselected);

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    public void sendSubscriptionClicked(View v) {

    }

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
}
