package com.nearsoft.reminder.main.models;

import android.text.format.Time;

/**
 * Created by Baniares on 4/3/14.
 */
public class Reminder {
    private int id = 0,userID;
    private String title,description;
    private Time startDate,endDate;

    public Reminder(){
        this.startDate = new Time();
        this.endDate = new Time();
    }

    public Reminder(int userID,String title,String description,
                    long startDate,long endDate){
        this.startDate = new Time();
        this.endDate = new Time();
        this.userID=userID;
        this.title = title;
        this.description=description;
        this.startDate = new Time();
        this.endDate = new Time();
        this.startDate.set(startDate);
        this.endDate.set(endDate);
    }
    public Reminder(int userID,String title,String description,
                    Time startDate,Time endDate){
        this.userID=userID;
        this.title = title;
        this.description=description;
        this.startDate = new Time();
        this.endDate = new Time();
        this.startDate.set(startDate);
        this.endDate.set(endDate);
    }
    //**getters and setters**
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartDateToMillis() {
        return startDate.toMillis(false);
    }

    public void setStartDate(long startDate) {
        this.startDate.set(startDate);
    }

    public Time getEndDate() {
        return endDate;
    }

    public Time getStartDate() {

        return startDate;
    }

    public void setStartDate(Time startDate){
        this.startDate.set(startDate);
    }

    public long getEndDateToMillis() {
        return endDate.toMillis(false);
    }


    public void setEndDate(long endDate) {
        this.endDate.set(endDate);
    }

    public void setEndDate(Time endDate){
        this.endDate.set(endDate);
    }

}
