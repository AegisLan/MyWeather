package com.aegisLan.weather.model;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class HourForecastInfo {
    int id;
    private String time;
    private int temp;
    private int hum;
    private int rainRate;
    private WindInfo windInfo;

    public HourForecastInfo() {
        windInfo = new WindInfo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getRainRate() {
        return rainRate;
    }

    public void setRainRate(int rainRate) {
        this.rainRate = rainRate;
    }

    public WindInfo getWindInfo() {
        return windInfo;
    }

    public void setWindInfo(WindInfo windInfo) {
        this.windInfo = windInfo;
    }
}
