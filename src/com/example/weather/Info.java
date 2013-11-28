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
    public String timeL;
    public int month;
    public int year;
    public int day;

    public Boolean isNight;
    public int temp, minTemp1, maxTemp1, minTemp2, maxTemp2, minTemp3, maxTemp3;
    public String mess, mess1, mess2, mess3;
    public int hum;
    public int mm;
    public int sp, sp1, sp2, sp3;
    public String vecL, vecL1, vecL2, vecL3;

}
