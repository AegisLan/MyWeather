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
import android.widget.TextView;

import com.aegisLan.weather.R;
import com.aegisLan.weather.model.WeatherInfo;
import com.aegisLan.weather.model.WeatherInfoRequest;
import com.aegisLan.weather.model.WeatherManager;


public class CityActivity extends BaseActivity {
    private int[] mArrayCityId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
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
        mToolbar.inflateMenu(R.menu.menu_city);
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
        private WeatherInfo mWeatherInfo;

        private int mCityId;
        private TextView mTvCityName;
        private TextView mTvCityTemp;
        private TextView mTvCityState;

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
            mWeatherInfo = WeatherManager.QueryCityWeather(getActivity(), mCityId);
            View rootView = inflater.inflate(R.layout.fragment_city, container, false);
            mTvCityName = (TextView) rootView.findViewById(R.id.tv_city_name);
            mTvCityTemp = (TextView) rootView.findViewById(R.id.tv_temp);
            mTvCityState = (TextView) rootView.findViewById(R.id.tv_state);
            if (mWeatherInfo != null) {
                mTvCityName.setText(mWeatherInfo.getName());
                mTvCityTemp.setText("当前气温：" + mWeatherInfo.getTemp() + "℃");
                mTvCityState.setText(mWeatherInfo.getState());
            }
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

    public final static int WEATHERUPDATE = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEATHERUPDATE:
                    int position = mViewPager.getCurrentItem();
                    int cityId = mArrayCityId[position];
                    if (cityId == msg.arg1) {
                        // TODO: 2016.1.23  刷新界面
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class OnFloatingButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int position = mViewPager.getCurrentItem();
            int cityId = mArrayCityId[position];
            WeatherInfoRequest.RefreshWeatherInfo(WEATHERUPDATE, handler, new int[]{cityId});
        }
    }
}
