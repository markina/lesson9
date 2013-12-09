package com.example.weather;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Date: 21.11.13
 *
 * @author Margarita Markina
 */
public class XMLParserForTown {
    String answer = "";
    MySaxParserForTown msp;

    public void putAnswer(String s) throws ParserConfigurationException, SAXException, IOException {
        answer = s;
        InputStream is = new ByteArrayInputStream(s.getBytes());
        msp = new MySaxParserForTown();
        SAXParser saxParser = (SAXParserFactory.newInstance()).newSAXParser();
        saxParser.parse(is, msp);
    }

    public VarTowns getTowns() {
        VarTowns ans = msp.getTowns();
        return ans;
    }


}
