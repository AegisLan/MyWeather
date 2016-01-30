package com.aegisLan.weather.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aegisLan.weather.R;
import com.aegisLan.weather.model.DayForecastInfo;
import com.aegisLan.weather.util.CalendarTools;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.26.
 */
public class DayWeatherAdapter extends ArrayAdapter<DayForecastInfo> {
    private class ViewHolder {
        TextView time;
        TextView temp;
        ImageView imageView;
        TextView state;
    }
    private int resourceId;
    private List<DayForecastInfo> list;
    private Context context;
    public DayWeatherAdapter(Context context, int resource, List<DayForecastInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
        list = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DayForecastInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(DayForecastInfo item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayForecastInfo info = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.time = (TextView) view.findViewById(R.id.tv_dayTime);
            holder.temp = (TextView) view.findViewById(R.id.tv_dayTemp);
            holder.state = (TextView) view.findViewById(R.id.tv_dayState);
            holder.imageView = (ImageView) view.findViewById(R.id.iv_dayState);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        if(CalendarTools.isToday(info.getTime())) {
            holder.time.setText("今天");
        } else {
            holder.time.setText(CalendarTools.getDayOfWeekByDateTime(info.getTime()));
        }
        int code = info.getDayStateCode();
        Field field = null;
        Bitmap bitmap = null;
        try {
            field = R.drawable.class.getDeclaredField("state" + code);
            int id = Integer.parseInt(field.get(null).toString());
            bitmap = BitmapFactory.decodeResource(context.getResources(),id);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        holder.imageView.setImageBitmap(bitmap);
        holder.state.setText(info.getDayStateText());
        holder.temp.setText(info.getTempMax() + "～" + info.getTempMin() + "℃");
        return view;
    }

}
