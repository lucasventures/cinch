package lucas.ventures.cinch.entities;

import android.text.format.Time;

import com.orm.SugarRecord;

/**
 * Created by LUCASVENTURES on 7/4/2016.
 */
public class Note extends SugarRecord {

    public static final String TAG = "NOTE POJO";

    public String title;
    public String body;
    public String time;

    public int day;
    public int month;
    public int year;

    public Note() {
    }



    public Note(String title, String body, int day, int month, int year) {
        //month parameter should range from 1-12
        this.title = title;
        this.body = body;

        this.day = day;
        this.month = month;
        this.year = year;

        this.time = timeDateFromInt(day, month, year);
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return time;
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
