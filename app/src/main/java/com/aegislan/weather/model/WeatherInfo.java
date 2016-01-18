package com.aegislan.weather.model;

/**
 * Created by AegisLan on 2016.1.14.
 */
public class WeatherInfo {
    private int cityId;
    private String city;
    private int temp;
    private String wind;
    private String windStrong;
    private String humidity;
    private String time;
    private boolean isRadar;
    private String Radar;
    private String njd;
    private String qy;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
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

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isRadar() {
        return isRadar;
    }

    public void setIsRadar(boolean isRadar) {
        this.isRadar = isRadar;
    }

    public String getRadar() {
        return Radar;
    }

    public void setRadar(String radar) {
        Radar = radar;
    }

    public String getNjd() {
        return njd;
    }

    public void setNjd(String njd) {
        this.njd = njd;
    }

    public String getQy() {
        return qy;
    }

    public void setQy(String qy) {
        this.qy = qy;
    }

}
