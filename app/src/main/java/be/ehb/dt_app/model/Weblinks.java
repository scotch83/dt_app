package be.ehb.dt_app.model;

/**
 * Created by Bart on 1/06/2015.
 */
public class Weblinks {
    public static long id, acadyear;

    public static final String TEACHER  = "http://vdabsidin.appspot.com/rest/teachers/" + id;
    public static final String TEACHERS = "http://vdabsidin.appspot.com/rest/teachers";
    public static final String TEACHERS_ACADYEAR = "http://vdabsidin.appspot.com/rest/teachers/" + acadyear;
    public static final String TEACHERS_POST = "http://vdabsidin.appspot.com/rest/teacher";

    public static final String SCHOOL = "http://vdabsidin.appspot.com/rest/school/" + id;
    public static final String SCHOOLS = "http://vdabsidin.appspot.com/rest/schools";
    public static final String SCHOOLS_ACADYEAR = "http://vdabsidin.appspot.com/rest/schools/" + acadyear;
    public static final String SCHOOLS_POST = "http://vdabsidin.appspot.com/rest/school";

    public static final String EVENT = "http://vdabsidin.appspot.com/rest/event/" + id;
    public static final String EVENTS = "http://vdabsidin.appspot.com/rest/events";
    public static final String EVENTS_POST = "http://vdabsidin.appspot.com/rest/event";

    public static final String SUBSCRIPTION  = "http://vdabsidin.appspot.com/rest/subscription/" + id;
    public static final String SUBSCRIPTIONS  = "http://vdabsidin.appspot.com/rest/subscriptions";
    public static final String SUBSCRIPTIONS_POST  = "http://vdabsidin.appspot.com/rest/subscription";

    public static final String IMAGE  = "http://vdabsidin.appspot.com/rest/image/" + id;
    public static final String IMAGES  = "http://vdabsidin.appspot.com/rest/images";
    public static final String IMAGE_POST  = "http://vdabsidin.appspot.com/rest/image";

}
