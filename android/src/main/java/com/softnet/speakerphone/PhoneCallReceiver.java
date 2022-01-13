package com.softnet.speakerphone;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;

public class PhoneCallReceiver extends BroadcastReceiver {

    private static String mLastState;
    private static String phoneNumber = "";
    private static AudioManager audioManager;
    private static int currentVolume;

/*    private static HashMap regNumber = new HashMap<String, String>(){
        {
            put("이후기","01029883940");
            put("소프트넷","0234462502");
            put("노은석대리님","01089578425");
        }
    };*/

    Set<String> numberList;

    public PhoneCallReceiver() {
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String state = "";
        state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        Toast.makeText(context,"브로드캐스트 리시버 호출 ",Toast.LENGTH_SHORT).show();

        Iterator<String> iterator = read_CenterNumber(context).iterator();
        String number;

        while (iterator.hasNext()){
            number = iterator.next();
            Log.v("CallTest","저장된 번호 리스트 : "+number);
            if(number.equals("0234462502")){
                Toast.makeText(context,"읽어온 센터번호 : "+number,Toast.LENGTH_LONG).show();
            }
        }
        // 브로드 캐스트 리시버가 알수없는 원인으로 두번 중복호출 되기 때문에 temp 변수와 비교후 다를때만 아래쪽 코드 실행
        // Call_LOG 권한을 추가했을때에 2번 실행되는걸로 보아, 전화기록이 핸드폰에 저장되었을때에 한번 더 호출되는 것으로 추정 (Call Log : API 28 에 추가된 권한)
        if(state.equals(mLastState)){

            //안드로이드 28 버전 이상의 핸드폰은 Call_log의 액션 (전화번호가 기록되는 액션 에서 수신전화를 가져온다), 28이상부터 CALL_LOG 권한 추가됨
            if(Build.VERSION.SDK_INT > 27 && state.equals(TelephonyManager.EXTRA_STATE_RINGING)){

                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                numberList = read_CenterNumber(context);
                Log.v("CallTest","전화리스트에 포함되어 있는지 테스트 : "+numberList.contains(phoneNumber));
                Log.v("CallTest", "phone : "+phoneNumber+" regi : "+numberList);
            }
            return;
        } else {
            mLastState = state;
        }

        //안드로이드 27 버전이하에서는 수신전화의 phone_state_changed 인텐트에서 수신전화번호를 가져올수 있다.
        if(Build.VERSION.SDK_INT <= 27 && state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            numberList = read_CenterNumber(context);
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            if(read_CenterNumber(context).contains(phoneNumber)){
                audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                setSpeakerponeOn(audioManager, true);
            }
        }

        if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            if(read_CenterNumber(context).contains(phoneNumber)){
                phoneNumber="";
                //outGoing 상태는 offHook 부터 시작
            }
        }
    }


    //현재 오디오 라우팅을 가져와서 모드설정 후 , 스피커폰 ON 및 볼륨 설정 (전화 IDLE 시에 다시 볼륨 원상복귀)
    @SuppressLint("WrongConstant")
    private static void setSpeakerponeOn(AudioManager audioManager, boolean enable){
        audioManager.setMode(AudioManager.MODE_CURRENT);
        if(enable == true){
            audioManager.setSpeakerphoneOn(true);

            //원인은 모르겠지만 30이상의 API 에서만 스피커폰 볼륨조절이 가능함
            if(Build.VERSION.SDK_INT >= 30){
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)/2, AudioManager.STREAM_VOICE_CALL);
                // 통화 볼륨 중간으로 설정
            }
            else {
                currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                Log.v("CallTest","현재 볼륨 : "+currentVolume+" 최대 볼륨 : "+audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)/2, AudioManager.STREAM_VOICE_CALL);
                Log.v("CallTest","볼륨설정 후 볼륨 : "+audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
            }
        }
        else if(enable == false){
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currentVolume, AudioManager.STREAM_VOICE_CALL);
            // 통화볼륨 원복
            audioManager.setMode(AudioManager.MODE_NORMAL);
            // 통화 스트림모드 일반모드로 원복
        }
    }

    private static class CheckSpeakerphone extends AsyncTask<Object, Object, Integer>{

        @Override
        protected Integer doInBackground(Object...params) {
            audioManager = (AudioManager) params[0];
            Context context = (Context) params[1] ;
            try {
                while (!isCancelled()) {
                    Log.v("CallTest","running... isCancelled() : "+isCancelled()+"    spekaerphone : "+audioManager.isSpeakerphoneOn());
                    Log.v("CallTest","현재 볼륨 : "+currentVolume+" 최대 볼륨 : "+audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
/*                    if (audioManager.isSpeakerphoneOn() == false) {
                        setSpeakerponeOn(audioManager, true);
                    }*/
                    Thread.sleep(1000);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        }
    }

    private Set<String> read_CenterNumber(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("callCenter_Info",Context.MODE_PRIVATE);
        Set<String> regiNumber = sharedPreferences.getStringSet("tel",null);
        return regiNumber;
    }

}