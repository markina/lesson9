package com.example.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MyActivity extends Activity {
    final String town = "";

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Info info = (Info)intent.getSerializableExtra("message");
            if(info.vecL == null) {
                Toast.makeText(MyActivity.this, "Problem with internet", Toast.LENGTH_LONG).show();
            } else {
                writeActivityInfo(info);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            //  refresh
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Spinner spinnerTown = (Spinner) findViewById(R.id.spTown);
        final String[] townesRu = {"Москва", "Санкт-Петербург", "Омск", "Казань", "Владивосток"};
        final String[] townesEn = {"Moscow", "59.921621,30.467361", "Omsk", "Kazan", "Vladivostok"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, townesRu);
        spinnerTown.setAdapter(adapter);
        spinnerTown.setPrompt("Город");
        spinnerTown.setSelection(0);
        spinnerTown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                Intent intent = new Intent(MyActivity.this, MyIntentService.class);
                intent.putExtra("exTown", townesEn[position]);
                startService(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //To change body of implemented methods use File | Settings | File Templates.
            }



        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));


    }

    private void writeActivityInfo(Info info) {

        TextView myTemp = (TextView) findViewById(R.id.tvTemperature);
        TextView myMessage = (TextView) findViewById(R.id.tvMessage);
        TextView myHum = (TextView) findViewById(R.id.tvHum);
        TextView myMm = (TextView) findViewById(R.id.tvMmHg);
        TextView mySp = (TextView) findViewById(R.id.tvSp);
        TextView myLWind = (TextView) findViewById(R.id.tvLWindVector);
        TextView myViewVec = (TextView) findViewById(R.id.tvViewWindVector);
        TextView mydate = (TextView) findViewById(R.id.tvConstDate);
        TextView myTime = (TextView) findViewById(R.id.tvConstTime);
        TextView tvConstWindVector = (TextView) findViewById(R.id.tvConstWindVelocity);
        TextView tvConstMmH = (TextView) findViewById(R.id.tvConstMmHg);
        TextView tvConstHum = (TextView) findViewById(R.id.tvConstHumidity);
        TextView tvConstToday = (TextView) findViewById(R.id.tvConstToday);

        TextView tvConstFor = (TextView) findViewById(R.id.tvConstForPeople);
        tvConstFor.setText("Прогноз на следующие 3 дня:");
        TextView tvConstTimeL = (TextView) findViewById(R.id.tvConstTimeL);
        tvConstTimeL.setText(info.timeL);

        tvConstToday.setText("Сегодня");
        tvConstHum.setText("Влажность");
        tvConstMmH.setText("мм рт. ст.");
        tvConstWindVector.setText("м/с");


        String degree = "" + (char)186 + " C";
        if(info.temp > 0) {
            myTemp.setText("+" + info.temp + degree);
        } else {
            myTemp.setText(info.temp + degree);
        }






        Integer clear = R.raw.clear;
        Integer cloudy = R.raw.cloudy;
        Integer cloudy_partly = R.raw.cloudy_partly;
        Integer fair = R.raw.fair;
        Integer fair_and_rain = R.raw.fair_and_rain;
        Integer fair_and_thundershowers = R.raw.fair_and_thundershowers;
        Integer foggy = R.raw.foggy;
        Integer hot = R.raw.hot;
        Integer night_clear = R.raw.night_clear;
        Integer night_cloudy = R.raw.night_cloudy;
        Integer night_fair = R.raw.night_fair;
        Integer night_fair_and_rain = R.raw.night_fair_and_rain;
        Integer night_snow = R.raw.night_snow;
        Integer rain = R.raw.rain;
        Integer rain_hail = R.raw.rain_hail;
        Integer rain_heavy = R.raw.rain_heavy;
        Integer snow = R.raw.snow;
        Integer snow_cloudy = R.raw.snow_cloudy;
        Integer snow_fair = R.raw.snow_fair;
        Integer snow_rain = R.raw.snow_rain;
        Integer thundershowers = R.raw.thundershowers;

        HashMap<String, ForMap > map = new HashMap<>();
        map.put("Moderate or heavy snow in area with thunder", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Patchy light snow in area with thunder", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Moderate or heavy rain in area with thunder", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Patchy light rain in area with thunder", new ForMap("Гроза", new Pair<Integer, Integer>(R.raw.thundershowers, R.raw.night_fair_and_rain)));
        map.put("Moderate or heavy showers of ice pellets", new ForMap("Град", new Pair<Integer, Integer>(R.raw.rain_hail, R.raw.rain_hail)));
        map.put("Light showers of ice pellets", new ForMap("Град", new Pair<Integer, Integer>(R.raw.rain_hail, R.raw.rain_hail)));
        map.put("Moderate or heavy snow showers", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Light snow showers", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Moderate or heavy sleet showers", new ForMap("Снег с дождем", new Pair<Integer, Integer>(R.raw.snow_rain, R.raw.snow_rain)));
        map.put("Light sleet showers", new ForMap("Снег с дождем", new Pair<Integer, Integer>(R.raw.snow_rain, R.raw.snow_rain)));
        map.put("Torrential rain shower", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Moderate or heavy rain shower", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Light rain shower", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Ice pellets", new ForMap("Град", new Pair<Integer, Integer>(R.raw.rain_hail, R.raw.rain_hail)));
        map.put("Heavy snow", new ForMap("Сильный снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Patchy heavy snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Moderate snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Patchy moderate snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Light snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Patchy light snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Moderate or heavy sleet", new ForMap("Снег с дождем", new Pair<Integer, Integer>(R.raw.snow_rain, R.raw.snow_rain)));
        map.put("Light sleet", new ForMap("Снег с дождем", new Pair<Integer, Integer>(R.raw.snow_rain, R.raw.snow_rain)));
        map.put("Moderate or Heavy freezing rain", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Light freezing rain", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Heavy rain", new ForMap("Сильный дождь", new Pair<Integer, Integer>(R.raw.rain_heavy, R.raw.night_fair_and_rain)));
        map.put("Heavy rain at times", new ForMap("Сильный дождь", new Pair<Integer, Integer>(R.raw.rain_heavy, R.raw.night_fair_and_rain)));
        map.put("Moderate rain", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Moderate rain at times", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Light rain", new ForMap("Небольшой дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Patchy light rain", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Heavy freezing drizzle", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Freezing drizzle", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Light drizzle", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Patchy light drizzle", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Freezing fog", new ForMap("Пасмурно", new Pair < Integer, Integer > (R.raw.foggy, R.raw.foggy)));
        map.put("Fog", new ForMap("Пасмурно", new Pair < Integer, Integer > (R.raw.foggy, R.raw.foggy)));
        map.put("Blizzard", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Blowing snow", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Thundery outbreaks in nearby", new ForMap("Гроза", new Pair<Integer, Integer>(R.raw.thundershowers, R.raw.thundershowers)));
        map.put("Patchy freezing drizzle nearby", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Patchy sleet nearby", new ForMap("Снег с дождем", new Pair<Integer, Integer>(R.raw.snow_rain, R.raw.snow_rain)));
        map.put("Patchy snow nearby", new ForMap("Снег", new Pair<Integer, Integer>(R.raw.snow, R.raw.night_snow)));
        map.put("Patch rain nearby", new ForMap("Дождь", new Pair<Integer, Integer>(R.raw.rain, R.raw.night_fair_and_rain)));
        map.put("Mist", new ForMap("Пасмурно", new Pair<Integer, Integer>(R.raw.foggy, R.raw.foggy)));
        map.put("Overcast", new ForMap("Облачно", new Pair<Integer, Integer>(R.raw.cloudy, R.raw.night_cloudy)));
        map.put("Cloudy", new ForMap("Облачно", new Pair<Integer, Integer>(R.raw.cloudy, R.raw.night_cloudy)));
        map.put("Partly Cloudy", new ForMap("Облачно с проснениями", new Pair<Integer, Integer>(R.raw.fair, R.raw.night_cloudy)));
        map.put("Clear", new ForMap("Ясно", new Pair<Integer, Integer>(R.raw.clear, R.raw.night_clear)));
        map.put("Sunny", new ForMap("Ясно", new Pair<Integer, Integer>(R.raw.clear, R.raw.night_clear)));

        ImageView imvMain = (ImageView) findViewById(R.id.imvMain);
        ForMap t = map.get(info.mess.trim());

        myMessage.setText(t.mess);

        if(info.isNight) {
            imvMain.setImageResource(t.pair.second);
        } else {
            imvMain.setImageResource(t.pair.first);
        }


        ImageView imageView1 = (ImageView) findViewById(R.id.iv1Main1);
        t = map.get(info.mess1.trim());
        imageView1.setImageResource(t.pair.first);
        TextView temp1 = (TextView) findViewById(R.id.temp1);
        temp1.setText(((Integer)info.minTemp1).toString() + degree + "..." +((Integer)info.maxTemp1).toString() + degree);
        TextView mess1 = (TextView) findViewById(R.id.tvMsg1);
        mess1.setText(t.mess);

        ImageView imageView2 = (ImageView) findViewById(R.id.iv1Main2);
        t = map.get(info.mess2.trim());
        imageView2.setImageResource(t.pair.first);
        TextView temp2 = (TextView) findViewById(R.id.temp2);
        temp2.setText(((Integer)info.minTemp2).toString() + degree + "..." +((Integer)info.maxTemp2).toString() + degree);
        TextView mess2 = (TextView) findViewById(R.id.tvMsg2);
        mess2.setText(t.mess);

        ImageView imageView3 = (ImageView) findViewById(R.id.iv1Main3);
        t = map.get(info.mess3.trim());
        imageView3.setImageResource(t.pair.first);
        TextView temp3 = (TextView) findViewById(R.id.temp3);
        temp3.setText(((Integer)info.minTemp3).toString() + degree + "..." +((Integer)info.maxTemp3).toString() + degree);
        TextView mess3 = (TextView) findViewById(R.id.tvMsg3);
        mess3.setText(t.mess);


        myHum.setText(info.hum + "%");
        myMm.setText(info.mm + "");
        mySp.setText(info.sp + "");
        String vec = info.vecL;

        if(vec.length() == 3) {
            vec = "" + vec.charAt(1) + vec.charAt(2);
        }
        switch (vec) {
            case "N":
                myLWind.setText("С");
                myViewVec.setText("↓");
                break;
            case "W":
                myLWind.setText("З");
                myViewVec.setText("→");
                break;
            case "S":
                myLWind.setText("Ю");
                myViewVec.setText("↑");
                break;
            case "E":
                myLWind.setText("В");
                myViewVec.setText("←");
                break;
            case "NW":
                myLWind.setText("СЗ");
                myViewVec.setText("↘");
                break;
            case "NE":
                myLWind.setText("СВ");
                myViewVec.setText("↙");
                break;
            case "SW":
                myLWind.setText("ЮЗ");
                myViewVec.setText("↗");
                break;
            case "SE":
                myLWind.setText("ЮВ");
                myViewVec.setText("↖");
                break;
        }

        String q = (info.day < 10 ? "0" : "") + info.day + "."
                + (info.month < 10 ? "0" : "") +
                info.month + "." +
                info.year;
        mydate.setText(q);

        String e = (info.hours < 10 ? "0" : "") + info.hours + ":" + (info.minutes < 10 ? "0" : "") + info.minutes;
        myTime.setText(e);
    }





}
