package com.nearsoft.reminder.main.objects;

import java.util.Calendar;

/**
 * Created by Baniares on 4/3/14.
 */
public class Reminder {
    private int id,userID;
    private String tittle,description;
    private Calendar startDate,endDate;

    public Reminder(){
        this.id = 0;
    }

    public Reminder(int userID,String tittle,String description,
                    Calendar startDate,Calendar endDate){
        this.userID=userID;
        this.tittle=tittle;
        this.description=description;
        this.startDate=startDate;
        this.endDate=endDate;
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

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getStartDate() {
        return startDate.getTimeInMillis();
    }

    public void setStartDate(long startDate) {
        this.startDate.setTimeInMillis(startDate);
    }

    public long getEndDate() {
        return endDate.getTimeInMillis();
    }

    public void setEndDate(long endDate) {
        this.endDate.setTimeInMillis(endDate);
    }
}
