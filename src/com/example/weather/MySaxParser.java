package com.example.weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MySaxParser extends DefaultHandler {
    Info info = new Info();


    static String TEMP_C = "temp_C#";
    static String WEATHER_DESC = "weatherDesc#";
    static String WIND_SPEED_KMPH = "windspeedKmph#";
    static String PRESSURE = "pressure#";
    static String HUMIDITY = "humidity#";
    static String WIND_DIR_16_POINT = "winddir16Point#";
    static String LOCAL_TIME = "localtime#";

    String lastTag = "";
    String st = "";

    public Info getInfo() {
        return info;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        lastTag = qName + "#";
    }

    public void endElement(String uri, String localName, String qName) {

        if (lastTag.equals(TEMP_C)) {
            info.temp = Integer.parseInt(st);
        }
        if (lastTag.equals(WEATHER_DESC)) {
            info.mess = st;
        }
        if (lastTag.equals(WIND_DIR_16_POINT)) {
            info.vecL = st;
        }
        if (lastTag.equals(WIND_SPEED_KMPH)) {
            double sp = Integer.parseInt(st);
            sp = sp * 1000 / 3600;
            info.sp = (int)sp;
        }
        if (lastTag.equals(PRESSURE)) {
            double mm = Double.parseDouble(st);
            mm *= 0.75;
            info.mm = (int)mm;
        }
        if (lastTag.equals(HUMIDITY)) {
            info.hum = Integer.parseInt(st);
        }
        if (lastTag.equals(LOCAL_TIME)) {
            String t = st;
            String y = "" + st.charAt(0) + st.charAt(1) + st.charAt(2) + st.charAt(3);
            info.year = Integer.parseInt(y);
            y = "" + st.charAt(5) + st.charAt(6);
            info.month = Integer.parseInt(y);
            y = "" + st.charAt(8) + st.charAt(9);
            info.day = Integer.parseInt(y);
            y = "" + st.charAt(11) + st.charAt(12);
            info.hours = Integer.parseInt(y);
            y = "" + st.charAt(14) + st.charAt(15);
            info.minutes = Integer.parseInt(y);
            if(info.hours >= 0 && info.hours < 7
                    || info.hours >= 22)
                info.isNight = true;
            else
                info.isNight = false;

        }



        lastTag = qName;
        st = "";
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String e = new String(ch, start, length);
        if (lastTag.equals(TEMP_C)
                ||lastTag.equals(WEATHER_DESC)
                ||lastTag.equals(WIND_SPEED_KMPH)
                ||lastTag.equals(HUMIDITY)
                ||lastTag.equals(PRESSURE)
                ||lastTag.equals(WIND_DIR_16_POINT)
                ||lastTag.equals(LOCAL_TIME))
        {
            st += e;
        }
    }

}
