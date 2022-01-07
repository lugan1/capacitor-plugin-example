package com.softnet.speakerphone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.telecom.RemoteConference;
import android.telecom.RemoteConnection;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class PhoneCallReceiver extends BroadcastReceiver {

    private static String mLastState;
    private static String phoneNumber = "";
    private AudioManager audioManager;
    private int currentVolume;

    // 01089578425 , 01029883940, 0234462502


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        // API 29 부터 intent에서 수신전화번호 가져오는 방법이 deprecated 되었기 때문에 CallScreeningService 에서 수신번호를 가져온다.
        if(state.equals("onScreenCall")){
            phoneNumber= intent.getStringExtra("phoneNumber");
        }

        // 브로드 캐스트 리시버가 알수없는 원인으로 두번 중복호출 되기 때문에 temp 변수와 비교후 다를때만 아래쪽 코드 실행
        if(state.equals(mLastState)){
            return;
        } else {
            mLastState = state;
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            // API 28 이하 버전은 브로드캐스트 리시버의 intent 에서 수신전화번호를 가져올 수 있다.
            if(Build.VERSION.SDK_INT <= 28){
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            }
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            setSpeakerponeOn(context, true);
            Toast.makeText(context, "핸드폰번호 : "+phoneNumber, Toast.LENGTH_SHORT).show();
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            setSpeakerponeOn(context, false);
        }
    }


    //현재 오디오 라우팅을 가져와서 모드설정 후 , 스피커폰 ON 및 볼륨 설정 (전화 IDLE 시에 다시 볼륨 원상복귀)
    @SuppressLint("WrongConstant")
    private void setSpeakerponeOn(Context context, boolean flag){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_CURRENT);
        if(flag == true){
            audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.STREAM_VOICE_CALL);
        }
        else if(flag == false){
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currentVolume, AudioManager.STREAM_VOICE_CALL);
        }
    }


}