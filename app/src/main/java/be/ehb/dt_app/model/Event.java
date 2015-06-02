package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;



public class Event extends SugarRecord<Event> {
    @Ignore
    private long _id;

    private String name;
    private short acadyear;
    
    public Event(){
    	
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

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
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
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", acadyear=" + acadyear +
                '}';
    }
}
