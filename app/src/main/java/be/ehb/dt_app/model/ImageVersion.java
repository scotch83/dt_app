package be.ehb.dt_app.model;

import com.orm.SugarRecord;

/**
 * Created by Mattia on 09/06/15.
 */
public class ImageVersion extends SugarRecord<ImageVersion> {
    private Long _id;
    private int version;

    public ImageVersion() {
    }

    public ImageVersion(Long _id, int version) {
        this._id = _id;
        this.version = version;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ImageVersion{" +
                "_id=" + _id +
                ", version=" + version +
                '}';
    }
}
