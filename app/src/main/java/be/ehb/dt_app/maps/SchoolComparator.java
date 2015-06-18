package be.ehb.dt_app.maps;

import java.io.Serializable;
import java.util.Comparator;

import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 17/06/2015.
 */
public class SchoolComparator implements Comparator<School>, Serializable {


    public SchoolComparator() {

    }

    @Override
    public int compare(School lhs, School rhs) {

        return lhs.getName().compareTo(rhs.getName());
    }
}
