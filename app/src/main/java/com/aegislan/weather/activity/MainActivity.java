package com.aegislan.weather.activity;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aegislan.weather.R;
import com.aegislan.weather.model.City;
import com.aegislan.weather.model.CityManager;
import com.aegislan.weather.provider.WeatherInfoProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private final static int ADD_CITY = 1;
    private final static int CITY_LOAD_ID = 100;
    private LoaderManager loaderManager;
    private ListView cityView;
    private CityAdapter adapter;
    private List<CityInfo> list;
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
        CityInfo info = new CityInfo();
        info.setCityName("成都");
        list.add(info);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public class CityAdapter extends ArrayAdapter<CityInfo> {
        private int resourceId;
        private List<CityInfo> list;
        public CityAdapter(Context context, int resource, List<CityInfo> objects) {
            super(context, resource, objects);
            resourceId = resource;
            list = objects;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public int getPosition(CityInfo item) {
            return super.getPosition(item);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CityInfo info = getItem(position);
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
            holder.cityName.setText(info.getCityName());
            StringBuilder builder = new StringBuilder("当前温度：");
            builder.append(info.getCityTemp());
            builder.append("°");
            builder.append("风：");
            builder.append(info.getWind());
            builder.append(",");
            builder.append(info.getWindStrong());
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
                CityInfo info = new CityInfo();
                info.setCityName(cursor.getString(cursor.getColumnIndex("name")));
                list.add(info);
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private class CityInfo {
        private String cityName;
        private String cityTemp;
        private String wind;
        private String windStrong;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityTemp() {
            return cityTemp;
        }

        public void setCityTemp(String cityTemp) {
            this.cityTemp = cityTemp;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }

        public String getWindStrong() {
            return windStrong;
        }

        public void setWindStrong(String windStrong) {
            this.windStrong = windStrong;
        }
    }
}
