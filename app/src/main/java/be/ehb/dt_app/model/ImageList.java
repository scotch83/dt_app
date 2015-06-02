package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class ImageList extends SugarRecord<Image> {
    private List<Image> images;

    public ImageList(){

    }

    public List<Image> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "ImageList{" +
                "images=" + images +
                '}';
    }
}
