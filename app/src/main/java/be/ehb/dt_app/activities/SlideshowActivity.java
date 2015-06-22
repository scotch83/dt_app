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
    private int i;
    private Slideshow slideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        //get the path to the images downloaded from the server

        path = this
                .getSharedPreferences(
                        "EHB App SharedPreferences",
                        Context.MODE_PRIVATE)
                .getString(
                        "Images path",
                        "data/data/be.ehb.dt_app/app_presentation_images");

        //retrieve the layout and set a listener to finish activity if user touch the screen
        relativeLayout = (RelativeLayout) findViewById(R.id.slideshowLayout);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //start the slide show

        slideshow = new Slideshow();

        AsyncTask.Status status = slideshow.getStatus();
        slideshow.execute();
    }


    public class Slideshow extends AsyncTask<Void, Integer, Void>{


        File dir = null;
        String fileNames[] = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dir = new File(path);
            fileNames = dir.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jpg");
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {

                try {
                    for (i = 0; i < fileNames.length; i++) {
                        if (i == fileNames.length)
                            i = 0;
                        else {
                            publishProgress(i);
                            Thread.sleep(5000l);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            relativeLayout.setBackgroundDrawable(Drawable.createFromPath(path + "/" + fileNames[values[0]]));
            cancel(true);
        }
    }
}
