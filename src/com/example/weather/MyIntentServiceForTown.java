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

public class MyIntentServiceForTown extends IntentService {
    VarTowns towns = new VarTowns();
    String UserTown = "";

    public MyIntentServiceForTown() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        UserTown = intent.getStringExtra("exTown2");

        String answer = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String r = "";
            for(int i = 0; i < UserTown.length(); i++) {
                if(UserTown.charAt(i) != ' ') {
                    r += UserTown.charAt(i);
                } else {
                    r += "%20";
                }
            }
            UserTown = r;
            String v = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + UserTown +
                    "&sensor=true&language=ru";

            HttpGet httpGet = new HttpGet(v);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            answer = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XMLParserForTown parser = new XMLParserForTown();
        try {
            parser.putAnswer(answer);

        } catch (Exception e) {
            e.printStackTrace();
        }
        towns = parser.getTowns();
        sendMessage();
    }

    private void sendMessage() {
        Intent intent = new Intent("my-event2");

        intent.putExtra("message2", towns);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
