package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.lang.String;


public class Event extends SugarRecord<Event> {
	@Ignore
    private long _id;

    private String naam;
    private short academiejaar;
    
    public Event(){
    	
    }

    public Event(String naam, short academiejaar) {
        this.naam = naam;
        this.academiejaar = academiejaar;
    }

    public short getAcademiejaar() {
        return academiejaar;
    }

    public void setAcademiejaar(short academiejaar) {
        this.academiejaar = academiejaar;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
