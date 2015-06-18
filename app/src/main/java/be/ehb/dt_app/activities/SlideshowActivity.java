package be.ehb.dt_app.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FilenameFilter;

import be.ehb.dt_app.R;

public class SlideshowActivity extends Activity {

    String path;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        path = this
                .getSharedPreferences(
                        "EHB App SharedPreferences",
                        Context.MODE_PRIVATE)
                .getString(
                        "Images path",
                        "data/data/be.ehb.dt_app/app_presentation_images");

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Slideshow().execute();
    }

    public class Slideshow extends AsyncTask<Void, Integer, Void>{
        boolean slideshow;
        int i;
        File f = new File(path);
        File file[]= f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });

        @Override
        protected Void doInBackground(Void... params) {

            do {
                if(++i +1 > file.length) i = 0;
                publishProgress(i);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (!slideshow);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            relativeLayout.setBackground(Drawable.createFromPath(String.valueOf(file[values[0]])));
        }
    }
}
