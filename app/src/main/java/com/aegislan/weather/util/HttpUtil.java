package com.aegislan.weather.util;


import android.net.Uri;
import android.util.Log;

import com.aegislan.weather.WeatherApplication;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by AegisLan on 2016.1.16.
 */
public class HttpUtil {
    public static RequestQueue mQueue = Volley.newRequestQueue(WeatherApplication.getAppContext());
    public static void httpJsonGet(String url,Response.Listener<JSONObject> onResponseListener,Response.ErrorListener onErrorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,onResponseListener,onErrorListener);
        mQueue.add(jsonObjectRequest);
    }
}
