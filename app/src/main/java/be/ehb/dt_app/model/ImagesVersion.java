package be.ehb.dt_app.model;

import com.orm.SugarRecord;

/**
 * Created by Mattia on 09/06/15.
 */
public class ImagesVersion extends SugarRecord<ImagesVersion> {
    private Long _id;
    private Integer version;

    public ImagesVersion() {
    }

    public ImagesVersion(Long _id, int version) {
        this._id = _id;
        this.version = version;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ImagesVersion{" +
                "_id=" + _id +
                ", version=" + version +
                '}';
    }
}
