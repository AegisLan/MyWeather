package com.aegislan.weather.controller;

import android.util.Log;

import com.aegislan.weather.util.HttpUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AegisLan on 2016.1.19.
 */
public class WeatherInfoRequest {
    private final static String apiKey = "&key=b252e6c27ee843e5b017e6bc4de8c6e5";
    private static HeWeatherListener heWeatherListener = new HeWeatherListener();
    public static void RefreshWeatherInfo(int[] arrayId) {
        String url = "https://api.heweather.com/x3/weather?cityid=CN";
        for (int i : arrayId) {
            String httpUrl = url + i + apiKey;
            HttpUtil.httpJsonGet(httpUrl,heWeatherListener,heWeatherListener);
        }
    }

    /**
     * 和风天气JSON解析
     */
    public static class HeWeatherListener implements Response.Listener<JSONObject>,Response.ErrorListener {
        public HeWeatherListener() {
        }

        @Override
        public void onResponse(JSONObject response) {
            String basic = null;
            try {
                basic = response.getString("HeWeather data service 3.0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("TAG", basic);
            System.out.print(basic);
    }
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", error.getMessage(), error);
        }
    }
}
