package com.aegisLan.weather.model;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class DayForecastInfo {
    int id;
    private String time;
    private String dayStateText;
    private int dayStateCode;
    private String nightStateText;
    private int nightStateCode;
    private int tempMax;
    private int tempMin;
    private int hum;
    private int rainRate;
    private WindInfo windInfo;

    public DayForecastInfo() {
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

    public String getDayStateText() {
        return dayStateText;
    }

    public void setDayStateText(String dayStateText) {
        this.dayStateText = dayStateText;
    }

    public int getDayStateCode() {
        return dayStateCode;
    }

    public void setDayStateCode(int dayStateCode) {
        this.dayStateCode = dayStateCode;
    }

    public String getNightStateText() {
        return nightStateText;
    }

    public void setNightStateText(String nightStateText) {
        this.nightStateText = nightStateText;
    }

    public int getNightStateCode() {
        return nightStateCode;
    }

    public void setNightStateCode(int nightStateCode) {
        this.nightStateCode = nightStateCode;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
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
