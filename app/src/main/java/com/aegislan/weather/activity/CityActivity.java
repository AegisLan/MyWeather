package com.aegisLan.weather.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.aegisLan.weather.R;
import com.aegisLan.weather.WeatherApplication;
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
                    case R.id.action_refresh:
                        int[] arrayId = new int[1];
                        SectionsPagerAdapter adapter = (SectionsPagerAdapter) mViewPager.getAdapter();
                        PlaceholderFragment fragment = adapter.getCurrentFragment();
                        arrayId[0] = fragment.mCityId;
                        WeatherInfoRequest.RefreshWeatherInfo(CityActivityHandler.WEATHER_UPDATE, mHandler, arrayId);
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
        private TextView mTvCityTime;
        private TextView mTvCityWind;
        private TabHost mTabHost;
        private ListView mHourListView;
        private ListView mDayListView;
        private HourWeatherAdapter mHourAdapter;
        private DayWeatherAdapter mDayAdapter;
        public PlaceholderFragment() {
        }

        private int dp2px(int dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    getResources().getDisplayMetrics());
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
            mTvCityWind = (TextView) rootView.findViewById(R.id.tv_wind);
            mTvCityTime = (TextView) rootView.findViewById(R.id.tv_time);
            mTabHost =(TabHost) rootView.findViewById(R.id.tabHost);
            mTabHost.setup();
            mTabHost.addTab(mTabHost.newTabSpec(TAB_HOUR).setIndicator("小时").setContent(R.id.layout_hour));
            mTabHost.addTab(mTabHost.newTabSpec(TAB_DAY).setIndicator("天").setContent(R.id.layout_day));
            TabWidget tabWidget = mTabHost.getTabWidget();
            for (int i = 0; i < tabWidget.getChildCount(); i++) {
                tabWidget.getChildAt(i).getLayoutParams().height = dp2px(40);
                TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
                tv.setGravity(BIND_AUTO_CREATE);
                tv.setTextSize(16);//设置字体的大小；
                tv.setTextColor(getResources().getColor(R.color.colorDiver));//设置字体的颜色；
            }
            mHourListView = (ListView) rootView.findViewById(R.id.hour_view);
            mDayListView = (ListView) rootView.findViewById(R.id.day_view);
            mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    if (tabId.equals(TAB_HOUR)) {

                    } else if (tabId.equals(TAB_DAY)) {

                    }
                }
            });
            /***********************初始化数据*************************/
            mWeatherInfo = WeatherManager.QueryCityWeather(getActivity(), mCityId);
            if (mWeatherInfo != null) {
                mTvCityName.setText(mWeatherInfo.getName());
                mTvCityTemp.setText(mWeatherInfo.getTemp() + "℃");
                mTvCityState.setText(mWeatherInfo.getState());
                if(mWeatherInfo.getTime() != null && !mWeatherInfo.getTime().equals("")) {
                    String[] times = mWeatherInfo.getTime().split(" ");
                    if(times.length > 1) {
                        mTvCityTime.setText("更新于" + times[1]);
                    }else {
                        mTvCityTime.setText("未更新");
                    }
                }else {
                    mTvCityTime.setText("未更新");
                }
                mTvCityWind.setText(mWeatherInfo.getWind());
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
        private void updateWeatherInfo() {
            /***********************初始化数据*************************/
            mWeatherInfo = WeatherManager.QueryCityWeather(getActivity(), mCityId);
            if (mWeatherInfo != null) {
                mTvCityName.setText(mWeatherInfo.getName());
                mTvCityTemp.setText(mWeatherInfo.getTemp() + "℃");
                mTvCityState.setText(mWeatherInfo.getState());
                mTvCityTime.setText("更新于" + mWeatherInfo.getTime().split(" ")[1]);
                mTvCityWind.setText(mWeatherInfo.getWind());
            }
            /***********************初始化小时预报数据*************************/
            List<HourForecastInfo> hourForecastInfoList = WeatherManager.QueryCityHourWeatherForecast(getActivity(), mCityId);
            mHourAdapter.clear();
            mHourAdapter.addAll(hourForecastInfoList);
            mHourAdapter.notifyDataSetChanged();
            /***********************初始化每天预报数据*************************/
            List<DayForecastInfo> dayForecastInfoList = WeatherManager.QueryCityDayWeatherForecast(getActivity(), mCityId);
            mDayAdapter.clear();
            mDayAdapter.addAll(dayForecastInfoList);
            mDayAdapter.notifyDataSetChanged();
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private int[] arrayIndex;
        private PlaceholderFragment currentFragment;

        public void setArrayIndex(int[] arrayIndex) {
            this.arrayIndex = arrayIndex;
        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public PlaceholderFragment getCurrentFragment() {
            return currentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (PlaceholderFragment) object;
            super.setPrimaryItem(container, position, object);
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
                        SectionsPagerAdapter adapter = (SectionsPagerAdapter) theActivity.mViewPager.getAdapter();
                        PlaceholderFragment fragment = adapter.getCurrentFragment();
                        fragment.updateWeatherInfo();
                        Toast.makeText(theActivity,"刷新成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
