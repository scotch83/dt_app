package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class StudentList extends SugarRecord<Student> {
    private List<Student> students;

    public StudentList(){

    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public String toString() {
        return "StudentList{" +
                "students=" + students +
                '}';
    }
}
