package be.ehb.dt_app.aanvulling;

import android.app.Activity;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

import be.ehb.dt_app.model.School;

/**
 * Created by Bart on 17/06/2015.
 */
public class SchoolHandler extends DefaultHandler {
    private ArrayList<School> scholen;
    private School school;
    private StringBuilder builder;
    private Activity activity;
    private short pc;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        switch (localName) {
            case "School":
                school = new School();
                break;
            case "Naam":
                builder = new StringBuilder();
                break;
            case "Postcode":
                builder = new StringBuilder();
                break;
            case "Gemeente":
                builder = new StringBuilder();
                break;
        }
    }


    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        scholen = new ArrayList<School>();
        builder = new StringBuilder();
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String gemeente = "";

        switch (localName) {
            case "School":
                scholen.add(school);
                break;
            case "Naam":
                ;
                school.setName(builder.toString());
                break;
            case "Postcode":
                pc = Short.parseShort(builder.toString());
                school.setPostcode(pc);
                break;
            case "Gemeente":
                school.setGemeente(builder.toString());
                break;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(new String(ch, start, length));
    }


    public ArrayList<School> getScholen() {
        return scholen;
    }
}
