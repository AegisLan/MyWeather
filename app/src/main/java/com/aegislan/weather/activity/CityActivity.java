package com.aegisLan.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.aegisLan.weather.R;
import com.aegisLan.weather.adapter.DayWeatherAdapter;
import com.aegisLan.weather.adapter.HourWeatherAdapter;
import com.aegisLan.weather.model.DayForecastInfo;
import com.aegisLan.weather.model.HourForecastInfo;
import com.aegisLan.weather.model.WeatherInfo;
import com.aegisLan.weather.model.WeatherInfoRequest;
import com.aegisLan.weather.model.WeatherManager;

import java.lang.ref.WeakReference;
import java.util.List;


public class CityActivity extends BaseActivity {
    private int[] mArrayCityId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private CityActivityHandler mHandler;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        mHandler = new CityActivityHandler(this);
        Intent intent = getIntent();
        mArrayCityId = intent.getIntArrayExtra("arrayId");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.setArrayIndex(mArrayCityId);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(intent.getIntExtra("position", 0));
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_city);
        mToolbar.collapseActionView();
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        return true;
                    case R.id.action_refresh:

                    default:
                        return false;
                }
            }
        });
        //setSupportActionBar(mToolbar);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private final static String CITY_ID = "id";
        private final static String TAB_HOUR = "hour";
        private final static String TAB_DAY = "day";
        private WeatherInfo mWeatherInfo;
        private int mCityId;
        private TextView mTvCityName;
        private TextView mTvCityTemp;
        private TextView mTvCityState;
        private TabHost mTabHost;
        private ListView mHourListView;
        private ListView mDayListView;
        private HourWeatherAdapter mHourAdapter;
        private DayWeatherAdapter mDayAdapter;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int cityId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.setCityId(cityId);
            Bundle args = new Bundle();
            args.putInt(CITY_ID, cityId);
            fragment.setArguments(args);
            return fragment;
        }

        private void setCityId(int cityId) {
            this.mCityId = cityId;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /***********************初始化UI*************************/
            View rootView = inflater.inflate(R.layout.fragment_city, container, false);
            mTvCityName = (TextView) rootView.findViewById(R.id.tv_city_name);
            mTvCityTemp = (TextView) rootView.findViewById(R.id.tv_temp);
            mTvCityState = (TextView) rootView.findViewById(R.id.tv_state);
            mTabHost =(TabHost) rootView.findViewById(R.id.tabHost);
            mTabHost.setup();
            mTabHost.addTab(mTabHost.newTabSpec(TAB_HOUR).setIndicator("小时").setContent(R.id.layout_hour));
            mTabHost.addTab(mTabHost.newTabSpec(TAB_DAY).setIndicator("天").setContent(R.id.layout_day));
            mHourListView = (ListView) rootView.findViewById(R.id.hour_view);
            mDayListView = (ListView) rootView.findViewById(R.id.day_view);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    if(tabId.equals(TAB_HOUR)) {

                    }else if(tabId.equals(TAB_DAY)) {

                    }
                }
            });
            /***********************初始化数据*************************/
            mWeatherInfo = WeatherManager.QueryCityWeather(getActivity(), mCityId);
            if (mWeatherInfo != null) {
                mTvCityName.setText(mWeatherInfo.getName());
                mTvCityTemp.setText(mWeatherInfo.getTemp() + "℃");
                mTvCityState.setText(mWeatherInfo.getState());
            }
            /***********************初始化小时预报数据*************************/
            List<HourForecastInfo> hourForecastInfoList = WeatherManager.QueryCityHourWeatherForecast(getActivity(), mCityId);
            mHourAdapter = new HourWeatherAdapter(getActivity(),R.layout.layout_hourforecast,hourForecastInfoList);
            mHourListView.setAdapter(mHourAdapter);
            /***********************初始化每天预报数据*************************/
            List<DayForecastInfo> dayForecastInfoList = WeatherManager.QueryCityDayWeatherForecast(getActivity(), mCityId);
            mDayAdapter = new DayWeatherAdapter(getActivity(),R.layout.layout_dayforecast,dayForecastInfoList);
            mDayListView.setAdapter(mDayAdapter);

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int[] arrayIndex;

        public void setArrayIndex(int[] arrayIndex) {
            this.arrayIndex = arrayIndex;
        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int cityId = arrayIndex[position];
            return PlaceholderFragment.newInstance(cityId);
        }

        @Override
        public int getCount() {
            if (arrayIndex != null) {
                return arrayIndex.length;
            } else {
                return 0;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int cityId = arrayIndex[position];
            WeatherInfo info = WeatherManager.QueryCityWeather(CityActivity.this, cityId);
            if (info != null) {
                return info.getName();
            }
            return null;
        }
    }
    public static class CityActivityHandler extends Handler {
        public final static int WEATHER_UPDATE = 1;
        WeakReference<CityActivity> mActivity;
        public CityActivityHandler(CityActivity activity) {
            super();
            mActivity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            CityActivity theActivity = mActivity.get();
            switch (msg.what) {
                case WEATHER_UPDATE:
                    int position = theActivity.mViewPager.getCurrentItem();
                    int cityId = theActivity.mArrayCityId[position];
                    if (cityId == msg.arg1) {
                        // TODO: 2016.1.23  刷新界面
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
