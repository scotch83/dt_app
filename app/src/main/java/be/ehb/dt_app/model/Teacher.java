package be.ehb.dt_app.model;

public class Teacher {

    //@JsonIgnore
    private Long id;

    private String name;
    private short acadyear;

    public Teacher() {
        this.name = "Unknown teacher";
        this.acadyear = 0;
    }

    public Teacher(String name, short acadyear) {
        this.name = name;
        this.acadyear = acadyear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAcadyear() {
        return acadyear;
    }

    public void setAcadyear(short acadyear) {
        this.acadyear = acadyear;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", acadyear=" + acadyear +
                '}';
    }
}
