package be.ehb.dt_app.model;


import java.util.List;

/**
 * Created by Mattia on 01/06/15.
 */
public class EventList {
    private List<Event> events;

    public EventList() {
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "EventList{" +
                "events=" + events +
                '}';
    }


}
