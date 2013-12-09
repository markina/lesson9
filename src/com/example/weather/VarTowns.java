package com.example.weather;

import java.io.Serializable;
import java.util.Vector;

/**
 * Date: 06.12.13
 *
 * @author Margarita Markina
 */
public class VarTowns implements Serializable {
    Vector<Town> towns= new Vector<Town>();

    public String[] getNames() {
        String[] strings = new String[towns.size()];
        for(int i = 0; i < towns.size(); i++) {
            strings[i] = towns.elementAt(i).Name;
        }
        return strings;
    }
}