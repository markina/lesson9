package com.example.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.*;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;

import java.util.List;

public class ActivityEdit extends Activity {

    //DB db;
    EditText etTown;
    long spinnerPosition;

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            VarTowns towns= (VarTowns)intent.getSerializableExtra("message2");
            if(towns == null) {
                Toast.makeText(ActivityEdit.this, "Отсутствует доступ к интернету", Toast.LENGTH_LONG).show();
            } else if(towns.towns.elementAt(0).Name.equals("bad")) {
                Toast.makeText(ActivityEdit.this, "Некорректный город", Toast.LENGTH_LONG).show();
            } else {
                writeToTv(towns);
            }
        }
    };


    EditText etEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);


        etEdit = (EditText) findViewById(R.id.etEdit);

        etEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String userTown = etEdit.getText().toString();
                    if(userTown.equals("")) {
                        Toast.makeText(ActivityEdit.this, "Введите город", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(ActivityEdit.this, MyIntentServiceForTown.class);
                        intent.putExtra("exTown2", userTown);
                        startService(intent);
                    }

                    return true;
                }
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        Button btnOkEdit = (Button) findViewById(R.id.btnOkEdit);
        View.OnClickListener oclBtnOkEdit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userTown = etEdit.getText().toString();
                if(userTown.equals("")) {
                    Toast.makeText(ActivityEdit.this, "Введите город", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(ActivityEdit.this, MyIntentServiceForTown.class);
                    intent.putExtra("exTown2", userTown);
                    startService(intent);
                }
            }
        };
        btnOkEdit.setOnClickListener(oclBtnOkEdit);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver2,
                new IntentFilter("my-event2"));

    }

    private void writeToTv(VarTowns towns) {
        ListView lv = (ListView) findViewById(R.id.lvEdit);
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, towns.getNames());
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //nextWord(position);

            }
        });
    }


}
