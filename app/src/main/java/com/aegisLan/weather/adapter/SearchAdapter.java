package com.aegisLan.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aegisLan.weather.R;

import java.util.List;

/**
 * Created by AegisLan on 2016.1.24.
 */
public class SearchAdapter extends ArrayAdapter<String> {
    private class ViewHolder {
        TextView cityName;
    }
    private int resourceId;
    private List<String> list;
    public SearchAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resourceId = resource;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(String item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position);
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            holder = new ViewHolder();
            holder.cityName = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.cityName.setText(name);
        return view;
    }
}
