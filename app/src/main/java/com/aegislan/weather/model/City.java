package com.aegislan.weather.model;

/**
 * Created by AegisLan on 2016.1.14.
 */
public class City {
    private int id;
    private String name;
    private String pinyin;
    private String province;


    public City(int id, String name, String pinyin, String province) {
        this.id = id;
        this.name = name;
        this.pinyin = pinyin;
        this.province = province;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

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

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
