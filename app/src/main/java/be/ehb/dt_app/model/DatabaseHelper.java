package be.ehb.dt_app.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bart on 2/06/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String CREATE = "CTREAT TABLE";
    private final static String PRIMARYKEY = "PRIMARY KEY";
    private final static String FOREIGNKEY = "FOREIGN KEY REFERENCES ";
    private final static String INTEGER = "INTEGER";
    private final static String TEXT = "TEXT";
    private final static String KOMMA = ", ";

    public final static String _ID = "Id";
    public final static String TABLE_STUDENTS = "TABLE STUDENTS";
    public final static String FIRSTNAME = "First name";
    public final static String LASTNAME = "Last name";
    public final static String STREET = "Street";
    public final static String NUMBER = "Number";
    public final static String ZIP = "Zip";
    public final static String CITY = "City";
    public final static String EMAIL = "Email";
    public final static String INTEREREST = "Interests";
    public final static String ISNEW = "IsNew";
    public final static String TEACHER = "Teacher";
    public final static String EVENT = "Event";
    public final static String SCHOOL = "School";


    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    private void maakTabelStudenten(SQLiteDatabase db){

    }
}
