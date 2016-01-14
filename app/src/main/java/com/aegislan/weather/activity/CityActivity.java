package com.aegislan.weather.activity;

import android.app.Activity;
import android.os.Bundle;
import com.aegislan.weather.R;

import in.srain.cube.views.ptr.header.MaterialHeader;

public class CityActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
    }
}
