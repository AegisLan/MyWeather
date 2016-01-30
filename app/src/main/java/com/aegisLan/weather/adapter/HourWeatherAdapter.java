package com.aegisLan.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aegisLan.weather.R;
import com.aegisLan.weather.model.HourForecastInfo;

import java.util.List;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class HourWeatherAdapter extends ArrayAdapter<HourForecastInfo> {
    private class ViewHolder {
        TextView time;
        TextView temp;
        TextView rainRate;
        TextView hum;
    }
    private int resourceId;
    private List<HourForecastInfo> list;
    public HourWeatherAdapter(Context context, int resource, List<HourForecastInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HourForecastInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(HourForecastInfo item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HourForecastInfo info = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.time = (TextView) view.findViewById(R.id.tv_hourTime);
            holder.temp = (TextView) view.findViewById(R.id.tv_hourTemp);
            holder.rainRate = (TextView) view.findViewById(R.id.tv_hourRainRate);
            holder.hum = (TextView) view.findViewById(R.id.tv_hourHum);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        String[] time = info.getTime().split(" ");
        holder.time.setText(time[1]);
        holder.temp.setText(info.getTemp() + "℃");
        StringBuilder builder = new StringBuilder();
        builder.append("降雨率: ").append(info.getRainRate()).append("%");
        holder.rainRate.setText(builder.toString());
        builder.delete(0,builder.length());
        builder.append("湿度: ").append(info.getHum()).append("%");
        holder.hum.setText(builder.toString());
        return view;
    }
}
