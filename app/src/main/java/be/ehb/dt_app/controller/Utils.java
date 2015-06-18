package be.ehb.dt_app.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import be.ehb.dt_app.model.Event;
import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;
import be.ehb.dt_app.model.SubscriptionsList;
import be.ehb.dt_app.model.Teacher;

/**
 * Created by Mattia on 07/06/15.
 */
public class Utils {

    public static void animateView(final View view, final int toVisibility, float toAlpha, int duration) {
        boolean show = toVisibility == View.VISIBLE;
        if (show) {
            view.setAlpha(0);
        }
        view.setVisibility(View.VISIBLE);
        view.animate()
                .setDuration(duration)
                .alpha(show ? toAlpha : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(toVisibility);
                    }
                });
    }

    public static boolean isNetworkAvailable(Activity activity) {

        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }


    public static Bitmap scaleImage(Bitmap image, Context context) {
        // Get current dimensions AND the desired bounding box
        int width = image.getWidth();
        int height = image.getHeight();
        int bounding = dpToPx(250, context);

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        Bitmap scaledBitmap = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
        return scaledBitmap;
    }

    private static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static void saveImage(String fileName, Bitmap imageToSave, Context applicationContext) {

        ContextWrapper cw = new ContextWrapper(applicationContext);

        File directory = cw.getDir("presentation_images", Context.MODE_PRIVATE);

        File mypath = new File(directory, fileName);

        Bitmap scaledImageToSave = Utils.scaleImage(imageToSave, applicationContext);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            scaledImageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//            return directory.getAbsolutePath();
    }

    public static void persistDownloadedData(HashMap<String, ArrayList> dataLists) {

        Iterator it = dataLists.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            switch (pair.getKey().toString()) {
                case "events":
                    for (Event event : (ArrayList<Event>) pair.getValue())
                        if (Event.find(Event.class, "SERVER_ID=?", String.valueOf(event.getServerId())).isEmpty()) {
                            event.save();
                        }
                    break;
                case "teachers":
                    for (Teacher teacher : (ArrayList<Teacher>) pair.getValue())
                        if (Teacher.find(Teacher.class, "SERVER_ID=?", String.valueOf(teacher.getServerId())).isEmpty()) {
                            teacher.save();
                        }
                    break;

                case "schools":
                    for (School school : (ArrayList<School>) pair.getValue())
                        if (School.find(School.class, "SERVER_ID=?", String.valueOf(school.getServerId())).isEmpty()) {
                            school.save();
                        }
                    break;
                case "subscriptions":
                    for (Subscription subscription : (ArrayList<Subscription>) pair.getValue()) {
                        LocalSubscription lSub = new LocalSubscription(subscription);
                        if (LocalSubscription.find(LocalSubscription.class, "SERVER_ID=?", String.valueOf(lSub.getServerId())).isEmpty()) {
                            lSub.save();
                        }
                    }
                    break;

            }
        }
    }

    public static void persistNewData(SubscriptionsList newData) {
        LocalSubscription.deleteAll(LocalSubscription.class);
        for (Subscription subscription : newData.getSubscriptions()) {
            LocalSubscription lSub = new LocalSubscription(subscription);
            lSub.save();
        }

    }
}
