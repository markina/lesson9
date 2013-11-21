package com.example.weather;

import android.util.Pair;

/**
 * Date: 21.11.13
 *
 * @author Margarita Markina
 */
public class ForMap {
    public Pair<Integer, Integer> pair;
    public String mess;

    ForMap(String s, Pair<Integer,Integer> p) {
        mess = s;
        pair = p;
    }
}
