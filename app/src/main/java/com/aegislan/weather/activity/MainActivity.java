package com.aegisLan.weather.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
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
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aegisLan.weather.R;
import com.aegisLan.weather.adapter.CityAdapter;
import com.aegisLan.weather.model.WeatherInfoRequest;
import com.aegisLan.weather.model.City;
import com.aegisLan.weather.model.CityManager;
import com.aegisLan.weather.model.WeatherInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int ADD_CITY = 1;
    private final static int WEATHER_UPDATE_ID = 100;
    private LoaderManager loaderManager;
    private ListView cityView;
    private CityAdapter adapter;
    private List<WeatherInfo> mWeatherInfoList;
    protected PtrFrameLayout mPtrFrameLayout;
    private MainActivityHandler mHandler;
    private Set<String> mWait2RefreshCities;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        mHandler = new MainActivityHandler(this);
        mWait2RefreshCities = new HashSet<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnFloatingButtonClickListener());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /**************************初始化ptr与header**********************/
        mPtrFrameLayout = (PtrFrameLayout)findViewById(R.id.material_style_ptr_frame);
        initPtrFrameLayout();
        /**************************初始化listView**********************/
        cityView = (ListView) findViewById(R.id.city_view);
        mWeatherInfoList = new ArrayList<>();
        adapter = new CityAdapter(this,R.layout.layout_weatherinfo,mWeatherInfoList);
        cityView.setAdapter(adapter);
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //// TODO: 2016.1.22 添加点击打开具体天气函数
                Intent intent = new Intent(MainActivity.this, CityActivity.class);
                int size = mWeatherInfoList.size();
                int[] arrayId = new int[size];
                Iterator<WeatherInfo> it = mWeatherInfoList.iterator();
                for (int i = 0; i < size; ++i) {
                    arrayId[i] = it.next().getId();
                }
                intent.putExtra("position", position);
                intent.putExtra("arrayId", arrayId);
                startActivity(intent);
            }
        });
        cityView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int clickPosition = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.info);
                builder.setMessage(R.string.delete_info);
                builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WeatherInfo info = (WeatherInfo) cityView.getAdapter().getItem(clickPosition);
                        CityManager.RemoveCity(MainActivity.this, info.getId());
                        loaderManager.restartLoader(WEATHER_UPDATE_ID, null, callbacks);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });
        loaderManager = getLoaderManager();
        loaderManager.initLoader(WEATHER_UPDATE_ID,null,callbacks);
    }

    private void initPtrFrameLayout(){
        final MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.refresh_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, dp2px(15), 0, dp2px(10));
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setDurationToCloseHeader(1500);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                mWait2RefreshCities.clear();
                return true;
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                mPtrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!mWait2RefreshCities.isEmpty()) {
                            mPtrFrameLayout.refreshComplete();
                            Toast.makeText(MainActivity.this, "刷新超时，请重试...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 3000);
                //// TODO: 2016.1.24 完成刷新流程
                mWait2RefreshCities.clear();
                int size = mWeatherInfoList.size();
                int[] arrayId = new int[size];
                Iterator<WeatherInfo> it = mWeatherInfoList.iterator();
                for (int i = 0; i < size; ++i) {
                    WeatherInfo info = it.next();
                    arrayId[i] = info.getId();
                    mWait2RefreshCities.add(info.getName());
                }
                WeatherInfoRequest.RefreshWeatherInfo(MainActivityHandler.WEATHER_UPDATE, mHandler, arrayId);
            }
        });
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


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
                        WeatherInfoRequest.RefreshWeatherInfo(MainActivityHandler.ADD_NEW_CITY,mHandler,new int[]{id});
                        Toast.makeText(this, city.toString().trim(), Toast.LENGTH_SHORT).show();
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
                    Uri weatherUri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay");
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
                    Toast.makeText(MainActivity.this,"刷新天气信息...",Toast.LENGTH_SHORT).show();
                    mWeatherInfoList.clear();
                    while (cursor.moveToNext()) {
                        WeatherInfo info = new WeatherInfo();
                        info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        info.setName(cursor.getString(cursor.getColumnIndex("name")));
                        info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                        info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
                        info.setState(cursor.getString(cursor.getColumnIndex("state")));
                        info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
                        info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                        mWeatherInfoList.add(info);
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

    public void onWeatherUpdate() {
        loaderManager.restartLoader(WEATHER_UPDATE_ID, null, callbacks);
    }


    public static class MainActivityHandler extends Handler {
        public final static int WEATHER_UPDATE = 1;
        public final static int ADD_NEW_CITY = 2;
        WeakReference<MainActivity> mActivity;
        public MainActivityHandler(MainActivity activity) {
            super();
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            MainActivity theActivity = mActivity.get();
            switch (msg.what) {
                case WEATHER_UPDATE:
                    String name = msg.getData().getString("name");
                    theActivity.onWeatherUpdate();
                    theActivity.mWait2RefreshCities.remove(name);
                    if (theActivity.mWait2RefreshCities.isEmpty()) {
                        theActivity.mPtrFrameLayout.refreshComplete();
                    }
                    break;
                case ADD_NEW_CITY:
                    theActivity.onWeatherUpdate();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private void initToolbar() {
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.collapseActionView();
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        return true;
                    default:
                        return false;
                }
            }
        });
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

    @SuppressWarnings("StatementWithEmptyBody")
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

}
