package be.ehb.dt_app.activities;

import android.content.SharedPreferences;
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
import android.widget.ImageView;

import be.ehb.dt_app.R;
import be.ehb.dt_app.controller.ZoomOutPageTransformer;
import be.ehb.dt_app.fragments.HeatmapFragment;
import be.ehb.dt_app.fragments.StudentenlijstFragment;

public class DataListActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    protected Fragment form1, form2;
    private ViewPager mPagerRegistratie;
    private PagerAdapter mPagerAdapter;
    private ImageView img_page1, img_page2;
    private View progressOverlay;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);


        setupDesign();

    }

    private void setupDesign() {
        form1 = new StudentenlijstFragment();
        form2 = new HeatmapFragment();

        img_page1 = (ImageView) findViewById(R.id.iv_page1_data);
        img_page2 = (ImageView) findViewById(R.id.iv_page2_data);


        mPagerRegistratie = (ViewPager) findViewById(R.id.pager_datalist);
        mPagerAdapter = new DatalistPagerAdapter(getSupportFragmentManager(), form1, form2);
        mPagerRegistratie.setAdapter(mPagerAdapter);
        mPagerRegistratie.setPageTransformer(true, new ZoomOutPageTransformer());

        mPagerRegistratie.setOnPageChangeListener(this);

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////                                                                                ////////////////
    ////////////////                        DATALIST PAGER                                          ////////////////
    ////////////////                                                                                ////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class DatalistPagerAdapter extends FragmentStatePagerAdapter {

        Fragment formPart1, formPart2;

        public DatalistPagerAdapter(FragmentManager supportFragmentManager, Fragment formPart1, Fragment formPart2) {
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
