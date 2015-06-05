package be.ehb.dt_app.model;

public class Event {

    //@JsonIgnore
    private Long id;
    private String name;
    private short acadyear;

    public Event() {

    }

    public Event(String name, short acadyear) {
        this.name = name;
        this.acadyear = acadyear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(short acadyear) {
        this.acadyear = acadyear;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acadyear=" + acadyear +
                '}';
    }
}
