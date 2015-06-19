package be.ehb.dt_app.aanvulling;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Bart on 10/06/2015.
 */
public final class PostHandler extends DefaultHandler {
    private StringBuilder builder;
    private ArrayList<Postcode> postcodes;
    private Postcode postcode;
    private short pc = 0;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        postcodes = new ArrayList<Postcode>();
        builder = new StringBuilder();
    }


    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        switch (localName) {
            case "city":
                postcode = new Postcode();
                break;
            case "zip":
                builder = new StringBuilder();
                break;
            case "name":
                builder = new StringBuilder();
                break;
        }
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        String gemeente = "";

        switch (localName) {
            case "city":
                postcodes.add(postcode);
                break;
            case "zip":
                pc = Short.parseShort(builder.toString());
                postcode.setPostcode(pc);
                break;

            case "name":
                gemeente = builder.toString();
                gemeente.replace("<![CDATA[", "");
                gemeente.replace("]]>", "");

                postcode.setGemeente(gemeente);
                break;

        }


    }


    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        builder.append(new String(ch, start, length));
    }

    protected ArrayList<Postcode> getPostcodes() {
        return postcodes;
    }

}
