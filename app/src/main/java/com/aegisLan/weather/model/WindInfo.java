package com.aegisLan.weather.model;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class WindInfo {
    private String dir;
    private String describe;
    private int speed;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
