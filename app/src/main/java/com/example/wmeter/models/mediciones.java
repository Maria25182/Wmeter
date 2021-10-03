package com.example.wmeter.models;

public class mediciones {

    private Float sensor;
    private String time;

    public mediciones() {
    }

    public mediciones(Float sensor, String time) {
        this.sensor = sensor;
        this.time = time;
    }

    public Float getSensor() {
        return sensor;
    }

    public void setSensor(Float sensor) {
        this.sensor = sensor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}