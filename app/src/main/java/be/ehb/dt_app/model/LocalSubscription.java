package be.ehb.dt_app.model;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
    private Long serverId;


    public LocalSubscription() {
    }

    public LocalSubscription(Subscription subscription) {

        this.serverId = subscription.getServerId();
        this.firstName = subscription.getFirstName();
        this.lastName = subscription.getLastName();
        this.email = subscription.getEmail();
        this.street = subscription.getStreet();
        this.streetNumber = subscription.getStreetNumber();
        this.zip = subscription.getZip();
        this.city = subscription.getCity();
        this.setInterests(subscription.getInterests());
        this.timestamp = subscription.getTimestamp();
        this.isNew = subscription.isNew();
        this.setTeacher(subscription.getTeacher());
        this.setEvent(subscription.getEvent());
        this.setSchool(subscription.getSchool());
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

    private void setInterests(HashMap<String, String> interests) {

        Interests interestsLijst = new Interests();


        interestsLijst.setDigx(interests.get("digx"));
        interestsLijst.setMultec(interests.get("multec"));
        interestsLijst.setWerkstudent(interests.get("werkstudent"));

        interestsLijst.save();
        this.interests = interestsLijst;

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
        List<Teacher> localTeacher = Teacher.find(Teacher.class, "SERVER_ID=?", String.valueOf(teacher.getServerId()));
        if (localTeacher.isEmpty()) {
            teacher.save();
            localTeacher = Teacher.find(Teacher.class, "SERVER_ID=?", String.valueOf(teacher.getServerId()));
            Log.d("xxx", "Event saved with id=" + localTeacher.get(0).getId());
        }
        this.teacher = localTeacher.get(0);
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {

        List<Event> localEvent = Event.find(Event.class, "SERVER_ID=?", String.valueOf(event.getServerId()));
        if (localEvent.isEmpty()) {
            event.save();
            localEvent = Event.find(Event.class, "SERVER_ID=?", String.valueOf(event.getServerId()));
            Log.d("xxx", "Event saved with id=" + localEvent.get(0).getId());

        }

        this.event = localEvent.get(0);

    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {

        List<School> localSchool = School.find(School.class, "SERVER_ID=?", String.valueOf(school.getServerId()));
        if (localSchool.isEmpty()) {
            school.save();
            localSchool = School.find(School.class, "SERVER_ID=?", String.valueOf(school.getServerId()));
            Log.d("xxx", "School saved with id=" + localSchool.get(0).getId());

        }
        this.school = localSchool.get(0);


    }

    public Long getServerId() {
        return serverId;
    }
}
