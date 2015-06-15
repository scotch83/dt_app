package be.ehb.dt_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;

public class Event extends SugarRecord<Event> {


    private Long serverId;
    private String name;
    private short acadyear;

    public Event() {

    }

    public Event(Long serverId, String name, short acadyear) {
        this.serverId = serverId;
        this.name = name;
        this.acadyear = acadyear;
    }

    public Event(String name, short acadyear) {
        this.name = name;
        this.acadyear = acadyear;
    }

    @JsonProperty("id")
    public Long getServerId() {
        return serverId;
    }

    @JsonProperty("id")
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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


    public String strinRap() {
        return "Event{" +
                "serverId=" + serverId +
                ", name='" + name + '\'' +
                ", acadyear=" + acadyear +
                '}';
    }
}
