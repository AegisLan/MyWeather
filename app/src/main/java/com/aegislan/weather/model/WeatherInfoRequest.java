package com.aegislan.weather.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.aegislan.weather.WeatherApplication;
import com.aegislan.weather.util.HttpUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            }
            if(heJsonBean == null)
            {
                Toast.makeText(WeatherApplication.getAppContext(),"刷新失败...",Toast.LENGTH_LONG).show();
                return;
            }
            WeatherInfo info = new WeatherInfo();
            int id = Integer.parseInt(heJsonBean.getBasic().getId().substring(2));
            info.setId(id);
            info.setName(heJsonBean.getBasic().getCity());
            info.setTemp(heJsonBean.getNow().getTmp());
            info.setState(heJsonBean.getNow().getCond().getTxt());
            info.setWind(heJsonBean.getNow().getWind().getDir());
            info.setWindStrong(heJsonBean.getNow().getWind().getSc() + "级");
            info.setTime(heJsonBean.getBasic().getUpdate().getLoc());
            try {
                int count = WeatherManager.UpdateCityWeather(WeatherApplication.getAppContext(),info);
                if(count == 0) {
                    /***************更新影响0条记录，意味着不存在该记录，则添加该记录*************/
                    WeatherManager.AddCityWeather(WeatherApplication.getAppContext(), info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = requestCode;
            message.arg1 = id;
            handler.sendMessage(message);
        }
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(WeatherApplication.getAppContext(),"刷新失败...",Toast.LENGTH_LONG).show();
            Log.e("TAG", error.getMessage(), error);
        }
    }
}
