package com.example.weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MySaxParserForTown extends DefaultHandler {
    VarTowns towns = new VarTowns();
    int d = 0;

    static String FORMATTED_ADDRESS = "formatted_address#";
    static String LAT = "lat#";
    static String LNG = "lng#";
    static String RESULT = "result#";
    static String STATUS = "status#";

    Town rTown = new Town();
    String lastTag = "";
    String st = "";

    Boolean fst = true;
    public VarTowns getTowns() {
        return towns;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        lastTag = qName + "#";
        if(lastTag.equals(FORMATTED_ADDRESS)) {
            d = 0;
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (lastTag.equals(FORMATTED_ADDRESS)) {
            rTown.Name = st;
        }
        if (d == 0 && lastTag.equals(LAT)) {
            rTown.Lat = st;
        }
        if(d == 0 && lastTag.equals(LNG)) {
            rTown.Lng = st;
            towns.towns.add(rTown);
            rTown = new Town();
            d++;
        }
        if (lastTag.equals(STATUS)) {
            if(!st.equals("OK")) {
                fst = false;
                rTown.Name = "bad";
                rTown.Lng = " ";
                rTown.Lat = " ";
                towns.towns.add(rTown);
            }
        }

        lastTag = qName;
        st = "";
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String e = new String(ch, start, length);
        if (lastTag.equals(FORMATTED_ADDRESS)
                ||lastTag.equals(LAT)
                ||lastTag.equals(LNG)
                ||lastTag.equals(STATUS))
        {
            st += e;
        }

    }

}
