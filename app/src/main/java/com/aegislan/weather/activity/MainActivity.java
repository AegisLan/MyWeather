package com.aegislan.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.aegislan.weather.R;
import com.aegislan.weather.model.City;



public class MainActivity extends AppCompatActivity {
    private final static int ADD_CITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OnFloatingButtonClickListener());
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
                        //// TODO: 2016.1.16
                        Toast.makeText(this, city.toString().trim(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
