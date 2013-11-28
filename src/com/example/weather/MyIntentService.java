package com.example.weather;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class MyIntentService extends IntentService {
    Info info = new Info();
    String town = "";

    public MyIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        town = intent.getStringExtra("exTown");
        String answer = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String v = "http://api.worldweatheronline.com/free/v1/weather.ashx?q=" + town +
                    "&format=xml&num_of_days=5&key=9379gushvyewdcqy66ya3ppm&extra=localObsTime";
            HttpGet httpGet = new HttpGet(v);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            answer = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLParser parser = new XMLParser();
        try {
            parser.putAnswer(answer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        info = parser.getInfo();


        sendMessage();
    }

    private void sendMessage() {
        Intent intent = new Intent("my-event");

        intent.putExtra("message", info);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
