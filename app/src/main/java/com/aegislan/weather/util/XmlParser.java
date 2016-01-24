package com.aegisLan.weather.util;

import android.util.Log;

import com.aegisLan.weather.model.City;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AegisLan on 2016.1.16.
 */
public class XmlParser {
    private final static String TAG = "XmlParser";

    public static List<City> getCityList(InputStream inputStream) throws Exception {
        Log.d(TAG, "startParser");
        List<City> list = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG: {
                    if (nodeName.equals("d")) {
                        int attri_num = parser.getAttributeCount();
                        if (attri_num != 4) {
                            continue;
                        }
                        String province = "";
                        String pinyin = "";
                        String name = "";
                        int id = 0;
                        for (int i = 0; i < attri_num; i++) {
                            String attrName = parser.getAttributeName(i);
                            if (attrName.equals("d1")) {
                                id = Integer.parseInt(parser.getAttributeValue(i));
                            } else if (attrName.equals("d2")) {
                                name = parser.getAttributeValue(i);
                            } else if (attrName.equals("d3")) {
                                pinyin = parser.getAttributeValue(i);
                            } else if (attrName.equals("d4")) {
                                province = parser.getAttributeValue(i);
                            }
                        }
                        City city = new City(id, name, pinyin, province);
                        list.add(city);
                    }
                    break;
                }
            }
            try {
                eventType = parser.next();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "endParser");
        return list;
    }
}
