package com.aegislan.weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aegislan.weather.R;
import com.aegislan.weather.model.City;
import com.aegislan.weather.model.CityFinder;
import com.aegislan.weather.model.CityManager;
import com.aegislan.weather.provider.CityProvider;

public class AddCityActivity extends Activity {
    Button button;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        button = (Button) findViewById(R.id.bt_add);
        editText = (EditText) findViewById(R.id.et_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText.getText().toString().trim();
                if(!"".equals(name)) {
                    City city = CityFinder.QueryCity(AddCityActivity.this, name);
                    if(city != null) {
                        Intent intent = new Intent();
                        intent.putExtra("name",city.getName());
                        intent.putExtra("id",city.getId());
                        intent.putExtra("pinyin",city.getPinyin());
                        intent.putExtra("province",city.getProvince());
                        setResult(RESULT_OK,intent);
                        finish();
                    } else {
                        Toast.makeText(AddCityActivity.this,"无效的城市，请重新输入...",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddCityActivity.this,"请输入城市名",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
