package com.example.spectresms;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    EditText SMS;
    EditText PHno;
    ImageButton setButton;
    TimePicker timePicker;
    int hour,minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SMS=(EditText) findViewById(R.id.msgEditText);
        PHno=(EditText) findViewById(R.id.PhNoEditText);
        setButton=(ImageButton) findViewById(R.id.imageButton);
        timePicker= (TimePicker) findViewById(R.id.timer);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int ahour, int aminute) {
              hour=ahour;
              minute=aminute;
            }
        });
        StartPermissionCheck();



    }

    public void StartPermissionCheck(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK, Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.FOREGROUND_SERVICE}, 102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 0:
                if (grantResults.length>0) {
                    ArrayList<String> ReRequest = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            ReRequest.add(permissions[i]);
                        }
                    }


                    if (ReRequest.size() > 0) {
                        Toast.makeText(this, "Give All Permissions", Toast.LENGTH_SHORT).show();
                        String[] Permissons = new String[ReRequest.size()];
                        for (int i = 0; i < ReRequest.size(); i++) {
                            Permissons[i] = ReRequest.get(i);
                            ActivityCompat.requestPermissions(this, Permissons, 0);
                        }
                    }
                }
                else {
                    Toast.makeText(this,"Give All The Permissions",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SET_ALARM, Manifest.permission.WAKE_LOCK, Manifest.permission.SEND_SMS}, 102);
                }
        }
    }

    public void SCHEDULE(View v) {
        if (SMS.getText().toString().matches("") || PHno.getText().toString().matches("")) {
            Toast.makeText(this, "Fill the required credentials", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Message will be sent", Toast.LENGTH_SHORT).show();

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Date date = new Date();

            Calendar alarm_cal = Calendar.getInstance();
            Calendar now_cal = Calendar.getInstance();

            //alarm_cal.setTime(date);
            //now_cal.setTime(date);

            alarm_cal.set(Calendar.HOUR_OF_DAY, hour);
            alarm_cal.set(Calendar.MINUTE, minute);
            alarm_cal.set(Calendar.SECOND, 0);

            if (alarm_cal.before(now_cal)) {
                alarm_cal.add(Calendar.DATE, 1);
            }

            Intent intent = new Intent(MainActivity.this, broadcastReceiver.class);
            intent.putExtra("Message", SMS.getText().toString());
            intent.putExtra("PhNo", PHno.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 24444, intent, 0);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_cal.getTimeInMillis(), pendingIntent);


        }
    }



}