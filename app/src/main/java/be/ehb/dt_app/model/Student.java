package be.ehb.dt_app.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.Date;
import java.util.HashMap;

public class Student extends SugarRecord<Student> {
	
	@Ignore
    private long id;

    private String firstName, lastName, street, city, email, streetNumber;
    private Date timestamp;
    private School school;
    private HashMap<String, Boolean> interests;
    private short  zip;
    private boolean isNew;
    private Teacher docent;
    
    public Student(){
        timestamp = new Date();
        interessesInstellen(false, false, false);
    }

    public Student(String firstName, String lastName, String street,
                   String streetNumber, short zip, String city, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.email = email;
        this.streetNumber = streetNumber;
        this.zip = zip;
        timestamp = new Date();
        interessesInstellen(false, false, false);
    }

    private void interessesInstellen(boolean digx, boolean multec, boolean werk){
        interests = new HashMap<String, Boolean>();
        interests.put("Dig-X", digx);
        interests.put("Multec", multec);
        interests.put("Werkstudent", werk);
    }
    
    private void bepaalDocent(){
    	docent = null;
    }

    private void bepaalSchool(){

    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public HashMap<String, Boolean> getInterests() {
        return interests;
    }

    public void setInterests(HashMap<String, Boolean> interests) {
        this.interests = interests;
    }

    public short getZip() {
        return zip;
    }

    public void setZip(short zip) {
        this.zip = zip;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Teacher getDocent() {
        return docent;
    }

    public void setDocent(Teacher docent) {
        this.docent = docent;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
