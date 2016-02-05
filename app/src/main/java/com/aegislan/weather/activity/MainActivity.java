package com.aegisLan.weather.activity;

import android.app.AlertDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.LoaderManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;

import com.aegisLan.weather.R;
import com.aegisLan.weather.adapter.CityCard;
import com.aegisLan.weather.adapter.CityCardAdapter;
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

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardListView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final static int ADD_CITY = 1;
    private LoaderManager loaderManager;
    private CardListView cardListView;
    private List<Card> mCityCardList;
    private List<WeatherInfo> mWeatherInfoList;
    private CityCardAdapter mCityCardAdapter;
    protected PtrFrameLayout mPtrFrameLayout;
    private MainActivityHandler mHandler;
    private Set<String> mWait2RefreshCities;
    private Toolbar mToolbar;
    private OnCardClickListener mOnCardClickListener;
    private OnCardLongClickListener mOnCardLongClickListener;

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
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.material_style_ptr_frame);
        initPtrFrameLayout();
        /**************************初始化listView**********************/
        cardListView = (CardListView) findViewById(R.id.mListView);
        mCityCardList = new ArrayList<>();
        mWeatherInfoList = new ArrayList<>();
        mCityCardAdapter = new CityCardAdapter(this, mCityCardList);
        cardListView.setAdapter(mCityCardAdapter);
        mOnCardClickListener = new OnCardClickListener();
        mOnCardLongClickListener = new OnCardLongClickListener();
        loaderManager = getLoaderManager();
        loaderManager.initLoader(WEATHER_INIT_ID, null, callbacks);
    }

    private void initPtrFrameLayout() {
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
        mPtrFrameLayout.setInterceptEventWhileWorking(true);

        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                mWait2RefreshCities.clear();
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
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
                }, 6000);
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
                        WeatherInfo info = new WeatherInfo();
                        info.setId(id);
                        info.setName(name);
                        addCard(info);
                        WeatherInfoRequest.RefreshWeatherInfo(MainActivityHandler.ADD_NEW_CITY, mHandler, new int[]{id});
                        Toast.makeText(this, city.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private final static int WEATHER_INIT_ID = 100;

    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
            CursorLoader loader = null;
            Uri weatherUri = null;
            switch (id) {
                case WEATHER_INIT_ID:
                    weatherUri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay");
                    loader = new CursorLoader(MainActivity.this, weatherUri, null, null, null, null);
                    break;
                default:
                    weatherUri = Uri.parse("content://com.aegisLan.weather.provider.WeatherInfoProvider/WeatherDay/" + id);
                    loader = new CursorLoader(MainActivity.this, weatherUri, null, null, null, null);
                    break;
            }
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            switch (loader.getId()) {
                case WEATHER_INIT_ID:
                    if (cursor == null) {
                        return;
                    }
                    mWeatherInfoList.clear();
                    while (cursor.moveToNext()) {
                        WeatherInfo info = new WeatherInfo();
                        info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        info.setName(cursor.getString(cursor.getColumnIndex("name")));
                        info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                        info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
                        info.setState(cursor.getString(cursor.getColumnIndex("state")));
                        info.setStateCode(cursor.getInt(cursor.getColumnIndex("stateCode")));
                        info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
                        info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                        info.setTempMax(cursor.getInt(cursor.getColumnIndex("tempMax")));
                        info.setTempMin(cursor.getInt(cursor.getColumnIndex("tempMin")));
                        mWeatherInfoList.add(info);
                    }
                    initCards(mWeatherInfoList);
                    break;
                default:
                    if (cursor == null) {
                        return;
                    }
                    if (cursor.moveToNext()) {
                        WeatherInfo info = new WeatherInfo();
                        info.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        info.setName(cursor.getString(cursor.getColumnIndex("name")));
                        info.setWind(cursor.getString(cursor.getColumnIndex("wind")));
                        info.setWindStrong(cursor.getString(cursor.getColumnIndex("windStrong")));
                        info.setState(cursor.getString(cursor.getColumnIndex("state")));
                        info.setStateCode(cursor.getInt(cursor.getColumnIndex("stateCode")));
                        info.setTemp(cursor.getInt(cursor.getColumnIndex("temp")));
                        info.setTime(cursor.getString(cursor.getColumnIndex("time")));
                        info.setTempMax(cursor.getInt(cursor.getColumnIndex("tempMax")));
                        info.setTempMin(cursor.getInt(cursor.getColumnIndex("tempMin")));
                        updateCard(info);
                    }
                    break;
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

    };

    private void initCards(List<WeatherInfo> list) {
        Log.d("initCards","initCards");
        if (list != null && list.size() != 0) {
            List<Card> cards = new ArrayList<>();
            for (WeatherInfo info : list) {
                CityCard card = new CityCard(this, info);
                card.setOnClickListener(mOnCardClickListener);
                card.setOnLongClickListener(mOnCardLongClickListener);
                cards.add(card);
            }
            mCityCardAdapter.clear();
            mCityCardAdapter.addAll(cards);
        }
    }

    private void updateCard(WeatherInfo info) {
        Log.d("updateCard",info.getName());
        CityCard card = (CityCard) mCityCardAdapter.getCarByTag(info.getName());
        if(card != null) {
            card.updateWeather(info);
        }
    }

    private void addCard(WeatherInfo info) {
        CityCard card = new CityCard(this, info);
        card.setOnClickListener(mOnCardClickListener);
        card.setOnLongClickListener(mOnCardLongClickListener);
        mCityCardAdapter.add(card);
    }

    private void deleteCard(String name) {
        CityCard card = (CityCard) mCityCardAdapter.getCarByTag(name);
        if(card != null) {
            mCityCardAdapter.remove(card);
        }
    }

    public void onWeatherUpdate(int id) {
        loaderManager.restartLoader(id, null, callbacks);
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
                    theActivity.onWeatherUpdate(msg.arg1);
                    theActivity.mWait2RefreshCities.remove(name);
                    if (theActivity.mWait2RefreshCities.isEmpty()) {
                        theActivity.mPtrFrameLayout.refreshComplete();
                        Toast.makeText(theActivity,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADD_NEW_CITY:
                    theActivity.onWeatherUpdate(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private class OnCardClickListener implements Card.OnCardClickListener {
        @Override
        public void onClick(Card card, View view) {
            int position = mCityCardAdapter.getPosition(card);
            Intent intent = new Intent(MainActivity.this, CityActivity.class);
            int size = mCityCardList.size();
            int[] arrayId = new int[size];
            Iterator<Card> it = mCityCardList.iterator();
            for (int i = 0; i < size; ++i) {
                CityCard cityCard = (CityCard) it.next();
                arrayId[i] = cityCard.getInfo().getId();
            }
            intent.putExtra("position", position);
            intent.putExtra("arrayId", arrayId);
            startActivity(intent);
        }
    }

    private class OnCardLongClickListener implements Card.OnLongCardClickListener {
        @Override
        public boolean onLongClick(Card card, View v) {
            final int position = mCityCardAdapter.getPosition(card);
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.info);
            builder.setMessage(R.string.delete_info);
            builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CityCard card = (CityCard) cardListView.getAdapter().getItem(position);
                    WeatherInfo info = card.getInfo();
                    CityManager.RemoveCity(MainActivity.this, info.getId());
                    deleteCard(info.getName());
                    mWeatherInfoList.remove(info);
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
    }

    private void initToolbar() {
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                switch (item.getItemId()) {
                    case R.id.action_refresh:
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
