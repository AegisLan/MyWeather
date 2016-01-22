package com.aegislan.weather.activity;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aegislan.weather.R;
import com.aegislan.weather.model.WeatherInfoRequest;
import com.aegislan.weather.model.City;
import com.aegislan.weather.model.CityManager;
import com.aegislan.weather.model.WeatherInfo;
import com.aegislan.weather.model.WeatherManager;
import com.baoyz.swipemenulistview.IBaseSwipable;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int ADD_CITY = 1;
    private final static int WEATHER_UPDATE_ID = 100;
    private LoaderManager loaderManager;
    private SwipeMenuListView cityView;
    private CityAdapter adapter;
    private List<WeatherInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnFloatingButtonClickListener());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        cityView = (SwipeMenuListView) findViewById(R.id.city_view);
        list = new ArrayList<>();
        adapter = new CityAdapter(this,R.layout.layout_weatherinfo,list);
        cityView.setAdapter(adapter);
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //// TODO: 2016.1.22 添加点击打开具体天气函数
                WeatherInfo info = (WeatherInfo) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this,CityActivity.class);
                intent.putExtra("id",info.getId());
                intent.putExtra("name",info.getName());
                startActivity(intent);
            }
        });
        InitSwipeMenu(cityView);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(WEATHER_UPDATE_ID,null,callbacks);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void InitSwipeMenu(final SwipeMenuListView listView) {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());// create "delete" item
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));// set item background
                deleteItem.setWidth(dp2px(90));// set item width
                deleteItem.setIcon(R.drawable.ic_delete);// set a icon
                menu.addMenuItem(deleteItem);// add to menu
            }
        };
        listView.setMenuCreator(creator);// set creator
        /**************step 2. listener item click event*******************/
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        WeatherInfo info = (WeatherInfo) listView.getAdapter().getItem(position);
                        WeatherManager.RemoveCityWeather(MainActivity.this,info);
                        loaderManager.restartLoader(WEATHER_UPDATE_ID, null, callbacks);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        listView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });
        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());
    }

    private class OnFloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int size = list.size();
            int[] arrayId = new int[size];
            Iterator<WeatherInfo> it = list.iterator();
            for(int i = 0; i < size; ++i) {
                arrayId[i] = it.next().getId();
            }
            WeatherInfoRequest.RefreshWeatherInfo(WEATHERUPDATE,handler,arrayId);
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
        } else if (id == R.id.action_add_city) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, AddCityActivity.class);
            startActivityForResult(intent, ADD_CITY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    public class CityAdapter extends ArrayAdapter<WeatherInfo> implements IBaseSwipable {
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

        public boolean getSwipEnableByPosition(int position) {
            return true;
        }
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
                        loaderManager.restartLoader(WEATHER_UPDATE_ID, null, callbacks);
                        WeatherInfoRequest.RefreshWeatherInfo(WEATHERUPDATE,handler,new int[]{id});
                        Toast.makeText(this, city.toString().trim(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            CursorLoader loader = null;
            switch (id) {
                case WEATHER_UPDATE_ID:
                    Uri weatherUri = Uri.parse("content://com.aegislan.weather.provider.WeatherInfoProvider/WeatherDay");
                    loader = new CursorLoader(MainActivity.this, weatherUri, null, null, null, null);
                    break;
            }
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case WEATHER_UPDATE_ID:
                    if(cursor == null) {
                        return;
                    }
                    Toast.makeText(MainActivity.this,"刷新天气信息...",Toast.LENGTH_LONG).show();
                    list.clear();
                    while (cursor.moveToNext()) {
                        WeatherInfo info = new WeatherInfo();
                        info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        info.setName(cursor.getString(cursor.getColumnIndex("name")));
                        info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                        info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
                        info.setState(cursor.getString(cursor.getColumnIndex("state")));
                        info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
                        info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                        list.add(info);
                    }
                    adapter.notifyDataSetChanged();
                    cursor.close();
                    break;
            }
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
                    loaderManager.restartLoader(WEATHER_UPDATE_ID, null, callbacks);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public Handler getHandler() {
        return handler;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
