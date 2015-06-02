package be.ehb.dt_app.model;

import android.widget.ArrayAdapter;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Bart on 1/06/2015.
 */
public class StudentDAO extends SugarRecord<Student> {


    public void insertStudent(String voornaam, String achternaam, String straat, String nummer,
        int postcode, String gemeente, String email){
        Student s = new Student();
        s.setFirstName(voornaam);
        s.setLastName(achternaam);
        s.setStreet(straat);
        s.setZip((short) postcode);
        s.setCity(gemeente);
        s.setEmail(email);
        s.save();
    }

    public void deleteStudentByEmail(String email){
        Student s = (Student) Student.find(Student.class, "email = ?", email);
        s.delete();
    }

    public void deleteStudentByLastName(String familienaam){
        Student s = (Student) Student.find(Student.class, "lastName = ?", familienaam);
        s.delete();
    }

    public void updateStudentByEmail(String voornaam, String familienaam, String straat, String nummer,
                                     int postcdode, String gemeente, String email){
        Student s = (Student) Student.find(Student.class, "email = ?", email);
        s.setFirstName(voornaam);
        s.setStreet(straat);
        s.setStreetNumber(nummer);
        s.setZip((short) postcdode);
        s.setCity(gemeente);
        s.setEmail(email);
        s.save();
    }

    public void updateStudentByName(String voornaam, String familienaam, String straat, String nummer,
                                    int postcdode, String gemeente, String email) {
        Student s = (Student) Student.find(Student.class, "lastName = ?", familienaam);
        s.setFirstName(voornaam);
        s.setStreet(straat);
        s.setStreetNumber(nummer);
        s.setZip((short) postcdode);
        s.setCity(gemeente);
        s.setEmail(email);
        s.save();
    }

    public ArrayList<Student> getStudententen(){
        ArrayList<Student> students = (ArrayList<Student>) Student.listAll(Student.class);
        return students;
    }


    public School updateSchool(){
        return null;
    }
}
