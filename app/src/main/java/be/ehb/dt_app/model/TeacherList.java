package be.ehb.dt_app.model;


import java.util.ArrayList;

/**
 * Created by Bart on 2/06/2015.
 */
public class TeacherList  {
    private ArrayList<Teacher> teachers;


    public TeacherList() {


    }

    public ArrayList<Teacher> getTeachers() {
        return teachers;
    }


    public void setTeachers(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public String toString() {
        return "TeacherList{" +
                "teachers=" + teachers +
                '}';
    }
}
