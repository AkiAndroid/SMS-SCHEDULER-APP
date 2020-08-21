package com.example.spectresms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class broadcastReceiver extends BroadcastReceiver {
    String Msg,PhNo;
    @Override
    public void onReceive(Context context, Intent intent) {


     Msg=intent.getStringExtra("Message");
     PhNo=intent.getStringExtra("PhNo");

     SmsManager smsManager= SmsManager.getDefault();

     try{
         smsManager.sendTextMessage(PhNo,null,Msg,null,null);

     }
     catch (Exception e){
         Toast.makeText(context, "Message could not be sent", Toast.LENGTH_SHORT).show();

     }


    }
}
