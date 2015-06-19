package be.ehb.dt_app.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import be.ehb.dt_app.model.LocalSubscription;
import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 11/06/2015.
 */
public class Schooltotaal {
    private final HashMap<School, Short> scholen;
    private final ArrayList<LocalSubscription> students;
    private final RegioIndeling indeling;


    public Schooltotaal() {
        indeling = new RegioIndeling();
        scholen = new HashMap<School, Short>();
        students = new ArrayList(LocalSubscription.listAll(LocalSubscription.class));


    }

    private void optellen() {
        LocalSubscription student;
        School school;
        short aantal = 0;
        for (short i = 0; i < students.size(); i++) {
            student = students.get(i);
            school = student.getSchool();
            if (scholen.containsKey(school)) {
                aantal = scholen.get(school);
                aantal++;

            }
            scholen.put(student.getSchool(), aantal);

        }

    }

    private void toevoegenAanMap() {
        Iterator<School> i = scholen.keySet().iterator();
        Iterator<Short> j = scholen.values().iterator();
        School school;
        short aantal;
        while (i.hasNext()) {
            school = i.next();
            aantal = j.next();
            indeling.schoolToevoegenAanProvincie(school, aantal);

        }

        //scholen.clear();
        //students.clear();
    }

    public void gegevens() {
        Iterator<LocalSubscription> i = students.iterator();
        LocalSubscription ls;
        while (i.hasNext()) {
            ls = i.next();
            //Log.d("", "school: "+ ls.getSchool().getName());
            scholen.put(ls.getSchool(), (short) 1);
        }
        optellen();
        toevoegenAanMap();
    }

    public RegioIndeling getIndeling() {
        return indeling;
    }
}
