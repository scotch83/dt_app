package be.ehb.dt_app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;

@JsonIgnoreProperties({"sqlName", "tableFields"})
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

    @JsonProperty("id")
    public Long getServerId() {
        return serverId;
    }

    @JsonProperty("id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        if (acadyear != teacher.acadyear) return false;
        return name.equals(teacher.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) acadyear;
        return result;
    }
}
