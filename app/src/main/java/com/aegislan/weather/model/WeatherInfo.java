package com.aegisLan.weather.model;

import java.io.Serializable;

/**
 * Created by AegisLan on 2016.1.14.
 */

public class WeatherInfo implements Serializable{
    private int id;
    private String name;
    private int temp;
    private String state;
    private String wind;
    private String windStrong;
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWindStrong() {
        return windStrong;
    }

    public void setWindStrong(String windStrong) {
        this.windStrong = windStrong;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
