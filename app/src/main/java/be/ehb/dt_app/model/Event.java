package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.lang.String;


public class Event extends SugarRecord<Event> {
    @Ignore
    private long id;

    private String name;
    private short acadyear;

    public Event() {

    }

    public Event(String naam, short academiejaar) {
        this.name = naam;
        this.acadyear = academiejaar;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
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
}
