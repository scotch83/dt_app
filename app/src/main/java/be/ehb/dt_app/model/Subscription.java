package be.ehb.dt_app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


@JsonIgnoreProperties({"id", "sqlName", "tableFields"})
public class Subscription {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String streetNumber;
    private String zip;
    private String city;
    private HashMap<String, String> interests;
    private Date timestamp;
    private boolean isNew;
    private Teacher teacher;
    private Event event;
    private School school;


    public Subscription() {
    }

    public Subscription(LocalSubscription localSubscription) {

        this.firstName = localSubscription.getFirstName();
        this.lastName = localSubscription.getLastName();
        this.email = localSubscription.getEmail();
        this.street = localSubscription.getStreet();
        this.streetNumber = localSubscription.getStreetNumber();
        this.zip = localSubscription.getZip();
        this.city = localSubscription.getCity();
        this.setInterests(localSubscription.getInterests());
        this.timestamp = localSubscription.getTimestamp();
        this.isNew = localSubscription.isNew();
        this.teacher = localSubscription.getTeacher();
        this.event = localSubscription.getEvent();
        this.school = localSubscription.getSchool();
    }

    public static ArrayList<Subscription> transformLSubscription(ArrayList<LocalSubscription> localSubscriptions)
    {
        ArrayList<Subscription> result = new ArrayList<>();

        for(LocalSubscription lSub : localSubscriptions)
        {
            Subscription temp = new Subscription(lSub);
            result.add(temp);
        }
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return interests;
    }

    @JsonProperty("interests")
    public void setInterests(HashMap<String, String> interests) {
        this.interests = interests;
    }

    @JsonIgnore
    public void setInterests(Interests interests) {

        HashMap<String,String> intMap = new HashMap<>();
        intMap.put("digx",interests.getDigx());
        intMap.put("multec",interests.getMultec());
        intMap.put("werkstudent",interests.getWerkstudent());

        this.interests = intMap;
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

    @JsonProperty("new")
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

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", interests=" + interests +
                ", timestamp=" + timestamp +
                ", isNew=" + isNew +
                ", teacher=" + teacher +
                ", event=" + event +
                ", school=" + school +
                '}';
    }
}
