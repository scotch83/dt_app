package be.ehb.dt_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orm.SugarRecord;


@JsonIgnoreProperties({"id"})
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


}

