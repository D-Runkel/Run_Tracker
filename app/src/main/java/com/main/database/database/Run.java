package com.main.database.database;

import java.time.*;

public class Run {

    private final long id;
    private long userID;
    private float miles;
    private float km;
    private Duration runTime;
    private LocalDate runDate;

    public Run(long id, long userID, float miles, float km, Duration runTime, LocalDate runDate){
        this.id = id;
        this.miles=miles;
        this.km=km;
        this.runTime=runTime;
        this.runDate=runDate;

    }

    public Run(){
        id = -1;
        userID = -1;
        miles = 0;
        km = 0;
        runTime = null;
        runDate = null;
    }

    public long getUserID() {
        return userID;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles){
        this.miles = miles;
        this.km = (float) (1.609 * miles);
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km){
        this.km = km;
        this.miles = (float) (0.621 * km);
    }

    public Duration getRunTime() {
        return runTime;
    }

    public void setRunTime(Duration runTime) {
        this.runTime = runTime;
    }

    public LocalDate getRunDate() {
        return runDate;
    }

    public void setRunDate(LocalDate runDate) {
        this.runDate = runDate;
    }
}
