package be.ehb.dt_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orm.SugarRecord;


@JsonIgnoreProperties({"id", "sqlName", "tableFields"})
public class School extends SugarRecord<School> {


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

    public School(long l, String s, String jette, int i) {

        this.name = s;
        this.gemeente = jette;
        this.postcode = (short) i;

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
                "name='" + name + '\'' +
                ", gemeente='" + gemeente + '\'' +
                ", postcode=" + postcode +
                '}';
    }
}

