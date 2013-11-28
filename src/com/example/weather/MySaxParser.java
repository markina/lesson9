package com.example.weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class MySaxParser extends DefaultHandler {
    Info info = new Info();
    int d = 0;


    static String TEMP_C = "temp_C#";
    static String WEATHER_DESC = "weatherDesc#";
    static String WIND_SPEED_KMPH = "windspeedKmph#";
    static String PRESSURE = "pressure#";
    static String HUMIDITY = "humidity#";
    static String WIND_DIR_16_POINT = "winddir16Point#";
    static String LOCAL_TIME = "localObsDateTime#";
    static String TEMP_MIN_C = "tempMinC#";
    static String TEMP_MAX_C = "tempMaxC#";
    static String WEATHER = "date#";

    String lastTag = "";
    String st = "";

    public Info getInfo() {
        return info;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(d <= 3){
            lastTag = qName + "#";
        } else {
            lastTag = "y";
        }
    }

    public void endElement(String uri, String localName, String qName) {

        if (lastTag.equals(TEMP_C)) {
            info.temp = Integer.parseInt(st);
        }
        if (lastTag.equals(TEMP_MAX_C)) {
            switch (d) {
                case 1 :
                    info.maxTemp1 = Integer.parseInt(st);
                    break;
                case 2:
                    info.maxTemp2 = Integer.parseInt(st);
                    break;
                case 3:
                    info.maxTemp3 = Integer.parseInt(st);
                    break;
            }
        }
        if (lastTag.equals(TEMP_MIN_C)) {
            switch (d) {
                case 1 :
                    info.minTemp1 = Integer.parseInt(st);
                    break;
                case 2:
                    info.minTemp2 = Integer.parseInt(st);
                    break;
                case 3:
                    info.minTemp3 = Integer.parseInt(st);
                    break;
            }
        }
        if (lastTag.equals(WEATHER_DESC)) {
            switch (d) {
                case 0 :
                    info.mess = st;
                    break;
                case 1 :
                    info.mess1 = st;
                    break;
                case 2 :
                    info.mess2 = st;
                    break;
                case 3 :
                    info.mess3 = st;
                    break;

            }
        }
        if (lastTag.equals(WIND_DIR_16_POINT)) {
            switch (d) {
                case 0 :
                    info.vecL = st;
                    break;
                case 1 :
                    info.vecL1 = st;
                    break;
                case 2 :
                    info.vecL2 = st;
                    break;
                case 3 :
                    info.vecL3 = st;
                    break;
            }
        }
        if (lastTag.equals(WIND_SPEED_KMPH)) {
            double sp = Integer.parseInt(st);
            sp = sp * 1000 / 3600;
            switch (d) {
                case 0 :
                    info.sp = (int)sp;
                    break;
                case 1 :
                    info.sp1 = (int)sp;
                    break;
                case 2 :
                    info.sp2 = (int)sp;
                    break;
                case 3 :
                    info.sp3 = (int)sp;
                    break;
            }
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
            y = "" + st.charAt(17) + st.charAt(18);
            info.timeL = " " + y;
            if((y.equals(" AM") && info.hours < 7) || (y.equals(" PM") && info.hours >= 10))
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
                ||lastTag.equals(LOCAL_TIME)
                ||lastTag.equals(TEMP_MIN_C)
                ||lastTag.equals(TEMP_MAX_C))
        {
            st += e;
        }
        if(lastTag.equals(WEATHER)) {
            d++;
        }
    }

}
