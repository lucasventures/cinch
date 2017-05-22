package com.virtualspaces.cinch.entities;

import android.text.format.Time;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by LUCASVENTURES on 6/2/2016.
 */


public class MyTransaction extends SugarRecord {

    public static final String TAG = "Transaction POJO";

    public String mCategory;
    public String mValue;
    public String mDetail;
    public String mTime;

    public int day;
    public int month;
    public int year;

    public MyTransaction() {
    }

    public MyTransaction(String type, String value, String detail, int day, int month, int year) {
        //month parameter should range from 1-12
        this.mCategory = type;
        this.mValue = value;
        this.mDetail = detail;

        this.day = day;
        this.month = month;
        this.year = year;


        this.mTime = timeDateFromInt(day, month, year);
    }

    public String getCategory() {
        return mCategory;
    }

    public String getValue() {
        return mValue;
    }

    public String getDetail() {
        return mDetail;
    }

    public String getTime() {
        return mTime;
    }


    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    //month should be in 0-11 format
    private String timeDateFromInt(int d, int m, int y) {
        Time time = new Time();
        time.setToNow();

        return getTime(time) + " " + monthFromInt(m) + " " + d + ", " + y;

    }

    private String monthFromInt(int month) {
        String[] months = {
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        };
        return months[month];
    }

    private String getTime(Time time) {
        String amPM = null;
        String minute = null;
        String hour = null;

        if (time.hour >= 13) {
            //it is PM
            hour = (time.hour - 12) + "";
            amPM = "PM";
        } else {
            //it is AM
            hour = time.hour + "";
            amPM = "AM";
        }

        if (time.minute < 10) {
            minute = "0" + time.minute;
        } else {
            minute = "" + time.minute;
        }

        return hour + ":" + minute + " " + amPM;
    }

}
