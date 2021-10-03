package com.example.wmeter.models;

public class prueba {
    //X and Y Values----
    Long fecha;
    Double sensor;

    public prueba() {
    }

    public prueba(Long fecha, Double sensor) {
        this.fecha = fecha;
        this.sensor = sensor;
    }

    public Long getFecha() {
        return fecha;
    }

    public Double getSensor() {
        return sensor;
    }
}

