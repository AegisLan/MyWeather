package com.aegisLan.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aegisLan.weather.R;
import com.aegisLan.weather.adapter.SearchAdapter;
import com.aegisLan.weather.model.City;
import com.aegisLan.weather.model.CityManager;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity {
    private final static String[] hot_cities = {"北京", "上海", "广州", "深圳", "成都", "武汉", "重庆", "南京", "天津", "西安"};
    private TextView mProvince;
    private EditText editText;
    private Toolbar mToolbar;
    private ListView mListView;
    private List<String> mSearchOutList;
    private SearchAdapter adapter;
    private TextView mListViewLabel;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = editText.getText().toString();
            if (text.isEmpty()) {
                initHotCityList();
            } else {
                List<String> list = CityManager.SearchCity(AddCityActivity.this, text);
                initSearchOut(list);
            }

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initHotCityList() {
        mListViewLabel.setText(R.string.hot_city);
        mSearchOutList.clear();
        for (String str:hot_cities) {
            mSearchOutList.add(str);
        }
        adapter.notifyDataSetChanged();
    }

    private void initSearchOut(List<String> list) {
        mListViewLabel.setText(R.string.search_out);
        mSearchOutList.clear();
        for (String str:list) {
            mSearchOutList.add(str);
        }
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        mProvince = (TextView) findViewById(R.id.search_province);
        editText = (EditText) findViewById(R.id.et_name);
        editText.addTextChangedListener(textWatcher);
        mListViewLabel = (TextView) findViewById(R.id.tv_label);
        mListView = (ListView) findViewById(R.id.city_view);
        mProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddCityActivity.this,"按省会查询",Toast.LENGTH_SHORT).show();
            }
        });
        mSearchOutList = new ArrayList<>();
        adapter = new SearchAdapter(this, R.layout.layout_citylist, mSearchOutList);
        mListView.setAdapter(adapter);
        initHotCityList();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mSearchOutList.get(position);
                City city = CityManager.FindCity(AddCityActivity.this, name);
                if (city != null) {
                    Intent intent = new Intent();
                    intent.putExtra("name", city.getName());
                    intent.putExtra("id", city.getId());
                    intent.putExtra("pinyin", city.getPinyin());
                    intent.putExtra("province", city.getProvince());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        editText.requestFocus();
    }

    private void initToolbar() {
        mToolbar.inflateMenu(R.menu.menu_addcity);
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
}
