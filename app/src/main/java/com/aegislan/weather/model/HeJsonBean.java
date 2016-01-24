package com.aegisLan.weather.model;

/**
 * Created by AegisLan on 2016.1.20.
 */
public class HeJsonBean {
    private Basic basic;
    private String status;
    private Aqi aqi;
    private Alarms alarms;
    private Now now;
    private Daily_forecast[] daily_forecast;
    private Hourly_forecast[] hourly_forecast;
    private Suggestion suggestion;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Alarms getAlarms() {
        return alarms;
    }

    public void setAlarms(Alarms alarms) {
        this.alarms = alarms;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public Daily_forecast[] getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(Daily_forecast[] daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public Hourly_forecast[] getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(Hourly_forecast[] hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }

    public static class Basic {
        private String city;
        private String cnty;
        private String id;
        private float lat;
        private float lon;
        private Update update;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLon() {
            return lon;
        }

        public void setLon(float lon) {
            this.lon = lon;
        }

        public Update getUpdate() {
            return update;
        }

        public void setUpdate(Update update) {
            this.update = update;
        }

        public static class Update {
            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }
    }
    public static class Aqi {
        private City city;

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public static class City {
            private int aqi;
            private int co;
            private int no2;
            private int o3;
            private int pm10;
            private int pm25;
            private String qlty;
            private int so2;

            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            public int getCo() {
                return co;
            }

            public void setCo(int co) {
                this.co = co;
            }

            public int getNo2() {
                return no2;
            }

            public void setNo2(int no2) {
                this.no2 = no2;
            }

            public int getO3() {
                return o3;
            }

            public void setO3(int o3) {
                this.o3 = o3;
            }

            public int getPm10() {
                return pm10;
            }

            public void setPm10(int pm10) {
                this.pm10 = pm10;
            }

            public int getPm25() {
                return pm25;
            }

            public void setPm25(int pm25) {
                this.pm25 = pm25;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public int getSo2() {
                return so2;
            }

            public void setSo2(int so2) {
                this.so2 = so2;
            }
        }
    }
    public static class Alarms {
        private String level;
        private String stat;
        private String title;
        private String txt;
        private String type;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public static class Now {
        private Cond cond;
        private int f1;
        private int hum;
        private float pcpn;
        private int pres;
        private int tmp;
        private int vis;
        private Wind wind;

        public Cond getCond() {
            return cond;
        }

        public void setCond(Cond cond) {
            this.cond = cond;
        }

        public int getF1() {
            return f1;
        }

        public void setF1(int f1) {
            this.f1 = f1;
        }

        public int getHum() {
            return hum;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public float getPcpn() {
            return pcpn;
        }

        public void setPcpn(float pcpn) {
            this.pcpn = pcpn;
        }

        public int getPres() {
            return pres;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public int getTmp() {
            return tmp;
        }

        public void setTmp(int tmp) {
            this.tmp = tmp;
        }

        public int getVis() {
            return vis;
        }

        public void setVis(int vis) {
            this.vis = vis;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public static class Cond {
            private int code;
            private String txt;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
        public static class Wind {
            private int deg;
            private String dir;
            private String sc;
            private int spd;

            public int getDeg() {
                return deg;
            }

            public void setDeg(int deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public int getSpd() {
                return spd;
            }

            public void setSpd(int spd) {
                this.spd = spd;
            }
        }
    }
    public static class Daily_forecast {
        private String date;
        private Astro astro;
        private Cond cond;
        private int hum;
        private float pcpn;
        private int pop;
        private int pres;
        private Temp tmp;
        private int vis;
        private Wind wind;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Astro getAstro() {
            return astro;
        }

        public void setAstro(Astro astro) {
            this.astro = astro;
        }

        public Cond getCond() {
            return cond;
        }

        public void setCond(Cond cond) {
            this.cond = cond;
        }

        public int getHum() {
            return hum;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public float getPcpn() {
            return pcpn;
        }

        public void setPcpn(float pcpn) {
            this.pcpn = pcpn;
        }

        public int getPop() {
            return pop;
        }

        public void setPop(int pop) {
            this.pop = pop;
        }

        public int getPres() {
            return pres;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public Temp getTmp() {
            return tmp;
        }

        public void setTmp(Temp tmp) {
            this.tmp = tmp;
        }

        public int getVis() {
            return vis;
        }

        public void setVis(int vis) {
            this.vis = vis;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public static class Astro {
            private String sr;
            private String ss;

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }
        }
        public static class Cond {
            private int code_d;
            private int code_n;
            private String txt_d;
            private String txt_n;

            public int getCode_d() {
                return code_d;
            }

            public void setCode_d(int code_d) {
                this.code_d = code_d;
            }

            public int getCode_n() {
                return code_n;
            }

            public void setCode_n(int code_n) {
                this.code_n = code_n;
            }

            public String getTxt_d() {
                return txt_d;
            }

            public void setTxt_d(String txt_d) {
                this.txt_d = txt_d;
            }

            public String getTxt_n() {
                return txt_n;
            }

            public void setTxt_n(String txt_n) {
                this.txt_n = txt_n;
            }
        }
        public static class Temp {
            private int max;
            private int min;

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }
        }
        public static class Wind {
            private int deg;
            private String dir;
            private String sc;
            private int spd;

            public int getDeg() {
                return deg;
            }

            public void setDeg(int deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public int getSpd() {
                return spd;
            }

            public void setSpd(int spd) {
                this.spd = spd;
            }
        }
    }
    public static class Hourly_forecast {
        private String date;
        private int hum;
        private float pop;
        private int pres;
        private int tmp;
        private Wind wind;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getHum() {
            return hum;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public float getPop() {
            return pop;
        }

        public void setPop(float pop) {
            this.pop = pop;
        }

        public int getPres() {
            return pres;
        }

        public void setPres(int pres) {
            this.pres = pres;
        }

        public int getTmp() {
            return tmp;
        }

        public void setTmp(int tmp) {
            this.tmp = tmp;
        }

        public Wind getWind() {
            return wind;
        }

        public void setWind(Wind wind) {
            this.wind = wind;
        }

        public static class Wind {
            private int deg;
            private String dir;
            private String sc;
            private int spd;

            public int getDeg() {
                return deg;
            }

            public void setDeg(int deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public int getSpd() {
                return spd;
            }

            public void setSpd(int spd) {
                this.spd = spd;
            }
        }
    }
    public static class Suggestion {
        private Sug comf;
        private Sug cw;
        private Sug drsg;
        private Sug flu;
        private Sug sport;
        private Sug trav;
        private Sug uv;

        public Sug getComf() {
            return comf;
        }

        public void setComf(Sug comf) {
            this.comf = comf;
        }

        public Sug getCw() {
            return cw;
        }

        public void setCw(Sug cw) {
            this.cw = cw;
        }

        public Sug getDrsg() {
            return drsg;
        }

        public void setDrsg(Sug drsg) {
            this.drsg = drsg;
        }

        public Sug getFlu() {
            return flu;
        }

        public void setFlu(Sug flu) {
            this.flu = flu;
        }

        public Sug getSport() {
            return sport;
        }

        public void setSport(Sug sport) {
            this.sport = sport;
        }

        public Sug getTrav() {
            return trav;
        }

        public void setTrav(Sug trav) {
            this.trav = trav;
        }

        public Sug getUv() {
            return uv;
        }

        public void setUv(Sug uv) {
            this.uv = uv;
        }

        public static class Sug {
            private String brf;
            private String txt;

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }
        }
    }
}
