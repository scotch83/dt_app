package be.ehb.dt_app.maps;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import be.ehb.dt_app.model.School;
import be.ehb.dt_app.model.Subscription;

/**
 * Created by Bart on 11/06/2015.
 */
public class Schooltotaal {
    private final HashMap<School, Short> scholen;
    private final ArrayList<Subscription> students;
    private final RegioIndeling indeling;


    public Schooltotaal() {
        indeling = new RegioIndeling();
        scholen = new HashMap<School, Short>();
        students = new ArrayList<Subscription>();


    }

    private void optellen() {
        Subscription student;
        School school;
        short aantal = 0;
        for (short i = 0; i < students.size(); i++) {
            student = students.get(i);
            school = student.getSchool();
            if (scholen.containsKey(school)) {
                aantal = scholen.get(school);
                aantal++;
                Log.d("", "Studenten van school" + school.getName() + " aangepast");
            }
            scholen.put(student.getSchool(), aantal);
            Log.d("", "Nieuw aantal toegegoegd");
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
            Log.d("", "School toegevoegd");
        }

        //scholen.clear();
        //students.clear();
    }

    public void gegevensUitArray(School[] schools, Subscription[] studs) {
        for (short i = 0; i < studs.length; i++) {
            short s = 0;
            students.add(studs[i]);
        }

        for (short i = 0; i < schools.length; i++) {
            short s = 0;
            scholen.put(schools[i], s);
        }

        optellen();
        toevoegenAanMap();
    }

    public RegioIndeling getIndeling() {
        return indeling;
    }
}
