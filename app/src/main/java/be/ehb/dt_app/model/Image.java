package be.ehb.dt_app.model;


import java.util.Arrays;

public class Image{

    //@JsonIgnore
    private Long id;

    private short priority;
    private byte[] image;

    public Image() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", priority=" + priority +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
