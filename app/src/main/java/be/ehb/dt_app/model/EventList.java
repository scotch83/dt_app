package be.ehb.dt_app.model;


import java.util.ArrayList;

/**
 * Created by Mattia on 01/06/15.
 */
public class EventList {
    private ArrayList<Event> events;

    public EventList() {
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "EventList{" +
                "events=" + events +
                '}';
    }


}
