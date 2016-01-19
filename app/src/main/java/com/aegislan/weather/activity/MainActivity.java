package com.aegislan.weather.activity;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aegislan.weather.R;
import com.aegislan.weather.controller.WeatherInfoRequest;
import com.aegislan.weather.model.City;
import com.aegislan.weather.model.CityManager;
import com.aegislan.weather.model.WeatherInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static int ADD_CITY = 1;
    private final static int CITY_LOAD_ID = 100;
    private LoaderManager loaderManager;
    private ListView cityView;
    private CityAdapter adapter;
    private List<WeatherInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnFloatingButtonClickListener());
        cityView = (ListView) findViewById(R.id.city_view);
        list = new ArrayList<>();
        adapter = new CityAdapter(this,R.layout.layout_weatherinfo,list);
        cityView.setAdapter(adapter);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(CITY_LOAD_ID, null, callbacks);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class OnFloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddCityActivity.class);
            startActivityForResult(intent, ADD_CITY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh) {
            // TODO: 2016.1.19 添加刷新机制
            int size = list.size();
            int[] arrayId = new int[size];
            Iterator<WeatherInfo> it = list.iterator();
            for(int i = 0; i < size; ++i) {
                arrayId[i] = it.next().getId();
            }
            WeatherInfoRequest.RefreshWeatherInfo(arrayId);
        }
        return super.onOptionsItemSelected(item);
    }

    public class CityAdapter extends ArrayAdapter<WeatherInfo> {
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
        public int getPosition(WeatherInfo item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
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
            builder.append(info.getTemp());
            builder.append("℃");
            builder.append("|天气状况：");
            builder.append(info.getState());
            holder.cityWeather.setText(builder.toString());
            return view;
        }
    }
    private class ViewHolder {
        TextView cityName;
        TextView cityWeather;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_CITY:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int id = data.getIntExtra("id", 0);
                        String name = data.getStringExtra("name");
                        String pinyin = data.getStringExtra("pinyin");
                        String province = data.getStringExtra("province");
                        City city = new City(id, name, pinyin, province);
                        CityManager.AddCity(this, city);
                        loaderManager.restartLoader(CITY_LOAD_ID, null, callbacks);
                        Toast.makeText(this, city.toString().trim(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            Uri uri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/CurrentCity");
            CursorLoader loader = new CursorLoader(MainActivity.this, uri, null, null, null, null);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            list.clear();
            while (cursor.moveToNext()) {
                WeatherInfo info = new WeatherInfo();
                info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                info.setName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(info);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    public final static int WEATHERUPDATE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEATHERUPDATE:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public Handler getHandler() {
        return handler;
    }
}
