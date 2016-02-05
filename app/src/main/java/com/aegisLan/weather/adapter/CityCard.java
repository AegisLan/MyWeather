package com.aegisLan.weather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import com.aegisLan.weather.R;
import com.aegisLan.weather.model.WeatherInfo;


import java.util.Random;
import it.gmariotti.cardslib.library.internal.Card;


/**
 * Created by AegisLan on 2016.1.31.
 */
public class CityCard extends Card {
    private final static int CARD_RESOURCE_ID = R.layout.native_card_item_layout;
    private Context context;
    private WeatherInfo info;
    private int colorId;
    private TextView textViewCity;
    private TextView textViewTime;
    private ImageView imageViewState;
    private TextView textViewTempNow;
    private TextView textViewTemp;

    public CityCard(Context context,WeatherInfo info) {
        super(context,CARD_RESOURCE_ID);
        this.context = context;
        this.info = info;
        setTitle(info.getName());
        Random random = new Random();
        int color = random.nextInt(100) % 10;
        int[] colorArray = context.getResources().getIntArray(R.array.city_card_colors);
        colorId = colorArray[color];
    }

    public WeatherInfo getInfo() {
        return info;
    }

    public void setInfo(WeatherInfo info) {
        this.info = info;
    }

    private int getStateImage(int code) {
        int index = R.drawable.cloud;
        if(code == 100) {
            index = R.drawable.sunraysmedium;
        }else if (code == 101) {
            index = R.drawable.cloud;
        }else if (code == 102) {
            index = R.drawable.cloud;
        }else if (code == 103) {
            index = R.drawable.sunrayssmallcloud;
        }else if (code == 104) {
            index = R.drawable.clouddark;
        }else if (code / 100 == 1) {
            index = R.drawable.cloud;
        }else if (code / 100 == 3) {
            index = R.drawable.cloudrain;
        }else if (code / 100 == 4) {
            index = R.drawable.cloudsnow;
        }
        return index;
    }



    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        //Retrieve elements
        textViewCity = (TextView) parent.findViewById(R.id.tv_city);
        textViewTime = (TextView) parent.findViewById(R.id.tv_info);
        imageViewState = (ImageView) parent.findViewById(R.id.iv_state);
        textViewTempNow = (TextView) parent.findViewById(R.id.tv_temp);
        textViewTemp = (TextView) parent.findViewById(R.id.tv_temp_area);

        if(textViewCity != null) {
            textViewCity.setText(info.getName());
        }
        if(textViewTime != null) {
            if(info.getTime() != null && !info.getTime().equals("")) {
                String[] times = info.getTime().split(" ");
                if(times.length > 1) {
                    textViewTime.setText(times[1]);
                }else {
                    textViewTime.setText("");
                }
            }else {
                textViewTime.setText("");
            }
        }
        if(textViewTempNow != null) {
            textViewTempNow.setText(info.getTemp() + "℃");
        }
        if(textViewTemp != null) {
            textViewTemp.setText(info.getTempMax() + "～" + info.getTempMin() + "℃");
        }
        if(imageViewState != null) {
            int imageId = getStateImage(info.getStateCode());
            imageViewState.setImageResource(imageId);
        }
        parent.setBackgroundColor(colorId);
    }

    public void updateWeather(WeatherInfo info) {
        this.info = info;
        notifyDataSetChanged();
    }
}

