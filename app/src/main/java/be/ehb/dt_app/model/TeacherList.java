package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class TeacherList extends SugarRecord<Teacher> {
    private List<Teacher> teachers;

    public TeacherList(){

    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    @Override
    public String toString() {
        return "TeacherList{" +
                "teachers=" + teachers +
                '}';
    }
}
