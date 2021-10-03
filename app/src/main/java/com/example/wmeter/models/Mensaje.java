package com.example.wmeter.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensaje {

    private Float sensor;
    private String time;


    public Mensaje (){

    }

    public Mensaje (Float sensor,String time){
    this.sensor = sensor;
    this.time = time;
    }


    public  Float getsensor(){
    return sensor;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public  String gettime(){
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy-hh:mm:ss");
        Date date = new Date();

        try {
            date = sd.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long milisegs = date.getTime();

        return time;
    }


}