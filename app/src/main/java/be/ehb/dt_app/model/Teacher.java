package be.ehb.dt_app.model;

import com.orm.SugarRecord;

public class Teacher extends SugarRecord<Teacher> {


    private String name;
    private short acadyear;
    private Long serverId;

    public Teacher() {

    }

    public Teacher(String name, short acadyear) {
        this.name = name;
        this.acadyear = acadyear;
    }


    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
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

    @Override
    public String toString() {
        return name;
    }
}
