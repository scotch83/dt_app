package be.ehb.dt_app.model;


import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class TeacherList  {
    private List<Teacher> teachers;


    public TeacherList() {


    }

    public List<Teacher> getTeachers() {
        return teachers;
    }


    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public String toString() {
        return "TeacherList{" +
                "teachers=" + teachers +
                '}';
    }
}
