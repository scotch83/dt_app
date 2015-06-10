package be.ehb.dt_app.model;

import com.orm.SugarRecord;

public class School extends SugarRecord<School> {

    //@JsonIgnore
    private Long _id;

    private String name;
    private String gemeente;
    private short postcode;

    public School() {


    }

    public School(String name, String gemeente, short postcode) {
        this.name = name;
        this.gemeente = gemeente;
        this.postcode = postcode;
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

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public short getPostcode() {
        return postcode;
    }

    public void setPostcode(short postcode) {
        this.postcode = postcode;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gemeente='" + gemeente + '\'' +
                ", postcode=" + postcode +
                '}';
    }
}

