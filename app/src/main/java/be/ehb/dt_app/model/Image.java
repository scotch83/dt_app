package be.ehb.dt_app.model;


import com.orm.SugarRecord;

public class Image extends SugarRecord<Image> {


    private short priority;
    private byte[] image;

    public Image() {

    }



    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}
