package be.ehb.dt_app.model;


import java.util.ArrayList;

/**
 * Created by Bart on 2/06/2015.
 */
public class SchoolList {
    private ArrayList<School> schools;

    public SchoolList(){

    }

    public ArrayList<School> getSchools() {
        return schools;
    }

    @Override
    public String toString() {
        return "SchoolList{" +
                "schools=" + schools +
                '}';
    }
}
