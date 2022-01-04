package com.softnet.speakerphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        
       String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

       System.out.println("OnReceive 호출");
       Toast.makeText(context.getApplicationContext(), "OnReceive 호출", Toast.LENGTH_SHORT).show();
       
       if(TelephonyManager.EXTRA_STATE_RINGING.equals(state)){
           String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
           System.out.println("벨소리 : "+phoneNumber);
           Toast.makeText(context.getApplicationContext(), "벨소리", Toast.LENGTH_SHORT).show();
       }

       if(TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)){
           String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
           System.out.println("전화 OFFHOOK : "+phoneNumber);
           Toast.makeText(context.getApplicationContext(), "전화 OFFHOOK", Toast.LENGTH_SHORT).show();
       }
    }
}