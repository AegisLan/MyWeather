package com.aegisLan.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aegisLan.weather.R;
import com.aegisLan.weather.model.WeatherInfo;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.24.
 */
public class CityAdapter extends ArrayAdapter<WeatherInfo>{
    private class ViewHolder {
        TextView cityName;
        TextView cityWeather;
    }
    private int resourceId;
    private List<WeatherInfo> list;
    public CityAdapter(Context context, int resource, List<WeatherInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public WeatherInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(WeatherInfo item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherInfo info = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.cityName = (TextView) view.findViewById(R.id.tv_cityName);
            holder.cityWeather = (TextView) view.findViewById(R.id.tv_info);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.cityName.setText(info.getName());
        StringBuilder builder = new StringBuilder("当前温度：");
        builder.append(info.getTemp()).append("℃").append("  天气状况：").append(info.getState());
        holder.cityWeather.setText(builder.toString());
        return view;
    }
}
