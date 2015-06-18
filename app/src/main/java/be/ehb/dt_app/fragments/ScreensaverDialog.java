package be.ehb.dt_app.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

import be.ehb.dt_app.R;

/**
 * Created by Lorenz on 12/06/2015.
 */
public class ScreensaverDialog extends Dialog {
    public ImageView screensaverIV;
    public int[] imageArray;
    public ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
    int i = 0;

    public ScreensaverDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setBitmapArray(ArrayList<Bitmap> bitmapArray) {
        this.bitmapArray = bitmapArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_screensaver);
        screensaverIV = (ImageView) findViewById(R.id.iv_image);

        screensaverIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("slideshow", "screensaver dismiss");
                dismiss();
            }
        });
        fillImageArray();
        screensaverIV.setImageBitmap(bitmapArray.get(i));
    }

    public int nextImage() {
        i++;
        if (i > bitmapArray.size() - 1) i = 0;
        return i;
    }

    public void fillImageArray() {

        String path = getContext()
                .getSharedPreferences(
                        "EHB App SharedPreferences",
                        Context.MODE_PRIVATE)
                .getString(
                        "Images path",
                        "data/data/be.ehb.dt_app/app_presentation_images");

        File f = new File(path);

        File file[] = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        Bitmap bitmap = null;
        for (int i = 0; i < file.length; i++) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file[i]), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            bitmapArray.add(bitmap);

        }
    }

}
