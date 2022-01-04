package com.softnet.speakerphone;


import android.app.Service;
import android.content.Intent;

import android.media.AudioManager;

import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallingService extends Service {
    private TelephonyManager telephonyManager;
    private AudioManager audioManager;

    public CallingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("서비스 - 온 크리에이트");

        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        audioManager = (AudioManager) getApplicationContext().getSystemService(AUDIO_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("서비스 - 온 디스트로이");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener(){

        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            System.out.println("OnCallStateChanged 호출 : "+state);
            System.out.println("Phone Number : "+phoneNumber);

            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //통화 중이 아닐때
                    System.out.println("통화중이 아님");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("통화중일때");
                    if(audioManager.isSpeakerphoneOn() == false){
                        audioManager.setSpeakerphoneOn(true);
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    System.out.println("전화벨이 울릴때");
                    break;
            }
        }
    };
}