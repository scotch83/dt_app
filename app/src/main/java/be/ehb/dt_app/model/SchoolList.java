package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Bart on 2/06/2015.
 */
public class SchoolList extends SugarRecord<School> {
    private List<School> schools;

    public SchoolList(){

    }

    public List<School> getSchools() {
        return schools;
    }

    @Override
    public String toString() {
        return "SchoolList{" +
                "schools=" + schools +
                '}';
    }
}
