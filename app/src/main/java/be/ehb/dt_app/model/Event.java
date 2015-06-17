package be.ehb.dt_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.orm.SugarRecord;

@JsonIgnoreProperties({"id", "sqlName", "tableFields"})
public class Event extends SugarRecord<Event> {


    private String name;
    private short acadyear;

    public Event() {

    }



    public Event(String name, short acadyear) {
        this.name = name;
        this.acadyear = acadyear;
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
        return name;
    }
}
