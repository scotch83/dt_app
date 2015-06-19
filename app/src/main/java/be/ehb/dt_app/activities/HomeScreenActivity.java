package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import be.ehb.dt_app.R;
import be.ehb.dt_app.fragments.ScreensaverDialog;
import be.ehb.dt_app.model.Teacher;


public class HomeScreenActivity extends Activity {


    private View lay;

    private SharedPreferences preferences;

    private long lastUsed = System.currentTimeMillis();
    private boolean stopScreenSaver;
    private ScreensaverDialog screensaverDialog;
    private ImageButton ehb_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setupDesign();
        preferences = getSharedPreferences("EHB App SharedPreferences", Context.MODE_PRIVATE);
        if (preferences.getBoolean("greetings", true)) {
            String jsonTeacher = preferences.getString("Teacher", "(iets misgelopen. Neem contact met de ICT dienst.)");

            ObjectMapper jxson = new ObjectMapper();
            try {
                Teacher docent = jxson.readValue(jsonTeacher, Teacher.class);
                Toast.makeText(getApplicationContext(), "Welkom " + docent.getName(), Toast.LENGTH_SHORT).show();
                preferences.edit().putBoolean("greetings", false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public void goToSubscriptionsList(View v) {
        Intent i = new Intent(getApplicationContext(), DataListActivity.class);
        startActivity(i);
    }

    public void goToPDFList(View v) {
        Intent i = new Intent(getApplicationContext(), PdfActivity.class);
        startActivity(i);
    }

    public void registrationClicked(View v) {
        Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(i);
    }

    public void goToSettings(View v) {
        Intent i = new Intent(getApplicationContext(), BeheerActivity.class);
        startActivity(i);
    }


    private void setupDesign() {

        lay = findViewById(R.id.rl_homescreen);
        lay.setBackgroundResource(R.drawable.achtergrond2);

        int pic = R.drawable.achtergrond2;
        lay.setBackgroundResource(pic);
        ehb_btn = (ImageButton) findViewById(R.id.ib_startslideshow);
        ehb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SlideshowActivity.class);
                startActivity(i);
            }
        });


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
//
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
//                Log.d("started", "the screensaver has started");
//                do {
//                    idle = System.currentTimeMillis() - lastUsed;
//                    Log.d("something", "Application is idle for " + idle + " ms");
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
}
