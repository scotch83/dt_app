package be.ehb.dt_app.model;

import com.orm.SugarRecord;

/**
 * Created by Mattia on 17/06/15.
 */
public class Interests extends SugarRecord<Interests> {


    String digx, multec, werkstudent;


    public Interests() {
    }


    public String getDigx() {
        return digx;
    }

    public void setDigx(String digx) {
        this.digx = digx;
    }

    public String getMultec() {
        return multec;
    }

    public void setMultec(String multec) {
        this.multec = multec;
    }

    public String getWerkstudent() {
        return werkstudent;
    }

    public void setWerkstudent(String werkstudent) {
        this.werkstudent = werkstudent;
    }
}
