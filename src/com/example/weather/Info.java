package com.example.weather;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Date: 19.11.13
 *
 * @author Margarita Markina
 */
public class Info implements Serializable{
    public int hours;
    public int minutes;
    public int month;
    public int year;
    public int day;

    public Boolean isNight;
    public int temp;
    public String mess;
    public int hum;
    public int mm;
    public int sp;
    public String vecL;

}
