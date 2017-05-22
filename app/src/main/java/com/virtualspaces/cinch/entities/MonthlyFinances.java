package com.virtualspaces.cinch.entities;

import android.text.format.Time;
import android.util.Log;

import com.orm.SugarRecord;

/**
 * Created by Lucas Leabres on 10/7/2016.
 */
public class MonthlyFinances extends SugarRecord {

    private String mName;
    private String mIncome;
    private String mExpenses;
    private String mSavingGoal;

    public String mTime;
    public int day;
    public int month;
    public int year;


    public MonthlyFinances(String name, String income, String expenses, String savingsGoal){
        mName = name;
        mIncome = income;
        mExpenses = expenses;
        mSavingGoal = savingsGoal;
        this.mTime = createStamp();
    }

    public String getmName() {
        return mName;
    }

    public String getmIncome() {
        return mIncome;
    }

    public String getmExpenses() {
        return mExpenses;
    }

    public String getmSavingGoal() {
        return mSavingGoal;
    }

    public String getmTime() {
        return mTime;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public int getYear(){
        return year;
    }

    private String createStamp() {
        Time time = new Time();
        time.setToNow();

        day = time.monthDay;
        month = time.month;
        year = time.year;

        Log.d("MonthlyFinances", "time = " +time.hour+":"+ time.minute+ " "+time.month+"/"+time.monthDay+"/"+time.year);
        return getTime(time)+ " "+time.month+"/"+time.monthDay+"/"+time.year;
    }

    private String getTime(Time time){
        String amPM = null;
        String minute = null;
        String hour = null;

        if(time.hour>=13){
            //it is PM
            hour = (time.hour - 12)+"";
            amPM = "PM";
        }else{
            //it is AM
            hour = time.hour+"";
            amPM = "AM";
        }

        if(time.minute<10){
            minute = "0"+time.minute;
        }else{
            minute = ""+time.minute;
        }

        return hour+":"+minute+" "+amPM;
    }


}
