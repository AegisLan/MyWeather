package com.aegisLan.weather.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.aegisLan.weather.WeatherApplication;
import com.aegisLan.weather.util.CalendarTools;
import com.aegisLan.weather.util.HttpUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.19.
 */
public class WeatherInfoRequest {
    private final static String apiKey = "&key=b252e6c27ee843e5b017e6bc4de8c6e5";
    private static HeWeatherListener heWeatherListener = new HeWeatherListener();
    public static void RefreshWeatherInfo(int requestCode,Handler handler,int[] arrayCityId) {
        heWeatherListener.setRequestCodeAndHandler(requestCode, handler);
        String url = "https://api.heweather.com/x3/weather?cityid=CN";
        for (int i : arrayCityId) {
            String httpUrl = url + i + apiKey;
            HttpUtil.httpJsonGet(httpUrl, heWeatherListener, heWeatherListener);
        }
    }

    /**
     * 和风天气JSON解析
     */
    public static class HeWeatherListener implements Response.Listener<JSONObject>,Response.ErrorListener {
        private int requestCode;
        private Handler handler;
        public HeWeatherListener() {
        }
        public void setRequestCodeAndHandler(int requestCode,Handler handler) {
            this.requestCode = requestCode;
            this.handler = handler;
        }

        private static List<HourForecastInfo> updateHourForecastDB(HeJsonBean heJsonBean) {
            List<HourForecastInfo> list = new ArrayList<>();
            if(heJsonBean == null) return list;
            HeJsonBean.Hourly_forecast[] hourly_forecasts = heJsonBean.getHourly_forecast();
            if(hourly_forecasts == null || hourly_forecasts.length == 0) return list;
            int id = Integer.parseInt(heJsonBean.getBasic().getId().substring(2));
            for(int i = 0; i < hourly_forecasts.length; ++i) {
                HeJsonBean.Hourly_forecast forecast = hourly_forecasts[i];
                HourForecastInfo info = new HourForecastInfo();
                info.setId(id);
                info.setTime(forecast.getDate());
                info.setTemp(forecast.getTmp());
                info.setHum(forecast.getHum());
                info.setRainRate(forecast.getPop());
                info.getWindInfo().setDir(forecast.getWind().getDir());
                info.getWindInfo().setDescribe(forecast.getWind().getSc());
                info.getWindInfo().setSpeed(forecast.getWind().getSpd());
                list.add(info);
            }
            return list;
        }

        private static List<DayForecastInfo> updateDayForecastDB(HeJsonBean heJsonBean) {
            List<DayForecastInfo> list = new ArrayList<>();
            if(heJsonBean == null) return list;
            HeJsonBean.Daily_forecast[] daily_forecasts = heJsonBean.getDaily_forecast();
            if(daily_forecasts == null || daily_forecasts.length == 0) return list;
            int id = Integer.parseInt(heJsonBean.getBasic().getId().substring(2));
            for(int i = 0; i < daily_forecasts.length; ++i) {
                HeJsonBean.Daily_forecast forecast = daily_forecasts[i];
                DayForecastInfo info = new DayForecastInfo();
                info.setId(id);
                info.setTime(forecast.getDate());
                info.setTempMax(forecast.getTmp().getMax());
                info.setTempMin(forecast.getTmp().getMin());
                info.setDayStateCode(forecast.getCond().getCode_d());
                info.setNightStateCode(forecast.getCond().getCode_n());
                info.setDayStateText(forecast.getCond().getTxt_d());
                info.setNightStateText(forecast.getCond().getTxt_n());
                info.setHum(forecast.getHum());
                info.setRainRate(forecast.getPop());
                info.getWindInfo().setDir(forecast.getWind().getDir());
                info.getWindInfo().setDescribe(forecast.getWind().getSc());
                info.getWindInfo().setSpeed(forecast.getWind().getSpd());
                list.add(info);
            }
            return list;
        }

        private static WeatherInfo updateWeatherInfoDB(HeJsonBean heJsonBean) {
            WeatherInfo info = null;
            if(heJsonBean == null) return info;
            info = new WeatherInfo();
            int id = Integer.parseInt(heJsonBean.getBasic().getId().substring(2));
            info.setId(id);
            info.setName(heJsonBean.getBasic().getCity());
            info.setTemp(heJsonBean.getNow().getTmp());
            info.setState(heJsonBean.getNow().getCond().getTxt());
            info.setStateCode(heJsonBean.getNow().getCond().getCode());
            info.setWind(heJsonBean.getNow().getWind().getDir());
            info.setWindStrong(heJsonBean.getNow().getWind().getSc() + "级");
            info.setTime(heJsonBean.getBasic().getUpdate().getLoc());
            HeJsonBean.Daily_forecast[] daily_forecasts = heJsonBean.getDaily_forecast();
            for(int i = 0; i < daily_forecasts.length; ++i) {
                HeJsonBean.Daily_forecast forecast = daily_forecasts[i];
                if(CalendarTools.isToday(forecast.getDate())) {
                    info.setTempMax(forecast.getTmp().getMax());
                    info.setTempMin(forecast.getTmp().getMin());
                    break;
                }
            }
            return info;
        }

        @Override
        public void onResponse(JSONObject response) {
            HeJsonBean heJsonBean = null;
            try {
                JSONArray array = response.getJSONArray("HeWeather data service 3.0");
                JSONObject HeJsonObject = array.getJSONObject(0);
                Gson gson = new Gson();
                heJsonBean = gson.fromJson(HeJsonObject.toString(), HeJsonBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if(heJsonBean == null)
            {
                Toast.makeText(WeatherApplication.getAppContext(),"刷新失败...",Toast.LENGTH_LONG).show();
                return;
            }
            WeatherInfo info = updateWeatherInfoDB(heJsonBean);
            if(info == null) return;

            try {
                int count = WeatherManager.UpdateCityWeather(WeatherApplication.getAppContext(),info);
                if(count == 0) {
                    Log.e("UpdateCityWeather","未知的城市：" + info.getName());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<HourForecastInfo> hourList = updateHourForecastDB(heJsonBean);
            try {
                int count = WeatherManager.UpdateHourForecast(WeatherApplication.getAppContext(), info.getId(), hourList);
                if(count != hourList.size()) {
                    Log.e("UpdateHourForecast", "更新条目数不匹");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<DayForecastInfo> dayList = updateDayForecastDB(heJsonBean);
            try {
                int count = WeatherManager.UpdateDayForecast(WeatherApplication.getAppContext(), info.getId(), dayList);
                if(count != dayList.size()) {
                    Log.e("UpdateDayForecast","更新条目数不匹配");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name", info.getName());
            message.what = requestCode;
            message.arg1 = info.getId();
            message.setData(bundle);
            handler.sendMessage(message);
        }
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", error.getMessage(), error);
        }
    }
}
