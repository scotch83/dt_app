package be.ehb.dt_app.aanvulling;

import android.app.Activity;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import be.ehb.dt_app.R;

/**
 * Created by Bart on 10/06/2015.
 */
public final class PostcodeParser {
    private Activity activity;
    private ArrayList<Postcode> postcodes;

    public PostcodeParser(Activity a) {
        postcodes = new ArrayList<Postcode>();
        activity = a;
    }


    public void parsen() {
        try {

            SAXParserFactory saxFabriek = SAXParserFactory.newInstance();
            SAXParser saxMaker = saxFabriek.newSAXParser();
            XMLReader lezer = saxMaker.getXMLReader();


            InputStream inTekst = activity.getResources().openRawResource(R.xml.cities);
            PostHandler handelaar = new PostHandler();
            lezer.setContentHandler(handelaar);
            lezer.parse(new InputSource(inTekst));

            postcodes = handelaar.getPostcodes();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), "Ophalen postcodes is mislukt", Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Postcode> getPostcodes() {
        if (postcodes == null) {
            Toast.makeText(activity.getApplicationContext(), "Postcode niet geparset", Toast.LENGTH_SHORT);
            postcodes = new ArrayList<Postcode>();
        }
        return postcodes;
    }
}
