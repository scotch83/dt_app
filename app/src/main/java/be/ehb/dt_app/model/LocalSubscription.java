package be.ehb.dt_app.model;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mattia on 17/06/15.
 */
public class LocalSubscription extends SugarRecord<LocalSubscription> {


    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String streetNumber;
    private String zip;
    private String city;
    private Interests interests = null;
    private Date timestamp;
    private boolean isNew;
    private Teacher teacher;
    private Event event;
    private School school;


    public LocalSubscription() {
    }

    public LocalSubscription(Subscription subscription) {
        this.id = subscription.getId();
        this.firstName = subscription.getFirstName();
        this.lastName = subscription.getLastName();
        this.email = subscription.getEmail();
        this.street = subscription.getStreet();
        this.streetNumber = subscription.getStreetNumber();
        this.zip = subscription.getZip();
        this.city = subscription.getCity();
        this.setInterests(subscription.getId(), subscription.getInterests());
        this.timestamp = subscription.getTimestamp();
        this.isNew = subscription.isNew();
        this.teacher = subscription.getTeacher();
        this.event = subscription.getEvent();
        this.school = subscription.getSchool();

    }


    public LocalSubscription(String firstName, String lastName, String email, String street, String streetNumber, String zip, String city, Interests interests, Date timestamp, boolean isNew, Teacher teacher, Event event, School school) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.street = street;
        this.streetNumber = streetNumber;
        this.zip = zip;
        this.city = city;
        this.interests = interests;
        this.timestamp = timestamp;
        this.isNew = isNew;
        this.teacher = teacher;
        this.event = event;
        this.school = school;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public HashMap<String, String> getInterests() {
        HashMap<String, String> interestsMap = new HashMap<>();
        interestsMap.put("digx", this.interests.getDigx());
        interestsMap.put("multec", this.interests.getMultec());
        interestsMap.put("werkstudent", this.interests.getWerkstudent());
        return interestsMap;
    }

    public void setInterests(Interests interests) {
        this.interests = interests;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    private void setInterests(Long id, HashMap<String, String> interests) {

        Interests interestsLijst = new Interests();


        interestsLijst.setDigx(interests.get("digx"));
        interestsLijst.setMultec(interests.get("multec"));
        interestsLijst.setWerkstudent(interests.get("multec"));
//        interestsLijst.setId(id);
        interestsLijst.save();
        this.interests = interestsLijst;

    }


}
