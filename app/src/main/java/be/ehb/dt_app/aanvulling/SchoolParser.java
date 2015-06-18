package be.ehb.dt_app.aanvulling;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;

import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 17/06/2015.
 */
public class SchoolParser {
    private Activity activity;
    private ArrayList<School> scholen;

    public SchoolParser(Activity a) {
        scholen = new ArrayList<School>();
        activity = a;
        Toast.makeText(a.getApplicationContext(), "Test", Toast.LENGTH_SHORT);
    }


    public void parsen() {
//        try {

//            SAXParserFactory saxFabriek = SAXParserFactory.newInstance();
//            SAXParser saxMaker = saxFabriek.newSAXParser();
//            XMLReader lezer = saxMaker.getXMLReader();
//
//
//            InputStream inTekst = activity.getResources().openRawResource(R.raw.school);
//            SchoolHandler handelaar = new SchoolHandler();
//            lezer.setContentHandler(handelaar);
//            lezer.parse(new InputSource(inTekst));

        scholen = new ArrayList(School.listAll(School.class));
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
//            Toast.makeText(activity.getApplicationContext(), "Ophalen scholen is mislukt", Toast.LENGTH_SHORT).show();
//        }
    }


    public ArrayList<School> getScholen() {
        if (scholen == null) {
            Toast.makeText(activity.getApplicationContext(), "Postcode niet geparset", Toast.LENGTH_SHORT);
            scholen = new ArrayList<School>();
        }
        return scholen;
    }
}
