package be.ehb.dt_app.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import be.ehb.dt_app.R;

public class RegistrationActivity extends ActionBarActivity {

    private Button volgende, vorige;
    private ViewSwitcher registratieVS;
    private Animation naarRechts, naarLinks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        volgende = (Button) findViewById(R.id.btn_nextpage);
        vorige = (Button) findViewById(R.id.btn_previouspage);

        registratieVS = (ViewSwitcher) findViewById(R.id.viewswitcher_registratie);

        naarLinks = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        naarRechts = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        volgende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registratieVS.showNext();
            }
        });

        vorige.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registratieVS.showPrevious();
            }
        });

        registratieVS.setAnimation(naarLinks);
        registratieVS.setAnimation(naarRechts);

        setUpDesign();


    }

    public void setUpDesign(){

        View lay = findViewById(R.id.rl_registratie);
        lay.setBackgroundResource(R.drawable.achtergrond2);

        int pic = R.drawable.achtergrond2;
        lay.setBackgroundResource(pic);

        ImageView logo = (ImageView) findViewById(R.id.iv_ehbletter);
        logo.setAlpha(1f);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
