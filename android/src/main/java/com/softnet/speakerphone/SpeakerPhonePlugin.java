package com.softnet.speakerphone;

import android.Manifest;
import android.app.Instrumentation;
import android.app.NotificationManager;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name = "SpeakerPhone",
        permissions = {
                @Permission(
                        alias = "MODIFY_AUDIO_SETTINGS",
                        strings = {Manifest.permission.MODIFY_AUDIO_SETTINGS }
                ),
                @Permission(
                        alias = "READ_PHONE_STATE",
                        strings = {Manifest.permission.READ_PHONE_STATE}
                ),
                @Permission(
                        alias = "READ_PHONE_NUMBERS",
                        strings = {Manifest.permission.READ_PHONE_NUMBERS}
                )
        }
)
public class SpeakerPhonePlugin extends Plugin {
    private SpeakerPhone implementation = new SpeakerPhone();

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 서비스와 연결되었을 때 호출
            Log.v("CallTest","서비스 연결 성공");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊어졌을 때 호출
            Log.v("CallTest","서비스 연결 Disconnect");
        }
    };

    @Override
    public void load() {
        super.load();
    }

    @PermissionCallback
    public void permissionRequestResult(PluginCall call){
        if(Build.VERSION.SDK_INT <30){
            if ((getPermissionState("MODIFY_AUDIO_SETTINGS") == PermissionState.GRANTED)
                    && (getPermissionState("READ_PHONE_STATE") == PermissionState.GRANTED)){
                // 만약 오디오 설정 권한요청과, 폰 상태 읽어오는 권한 허락되었다면
            }
            else{ call.reject("권한요청 거부"); }
        }
        else if(Build.VERSION.SDK_INT >= 30){
            if ((getPermissionState("MODIFY_AUDIO_SETTINGS") == PermissionState.GRANTED)
                    && (getPermissionState("READ_PHONE_NUMBERS") == PermissionState.GRANTED)){
                // 만약 오디오 설정 권한요청과, 폰 상태 읽어오는 권한 허락되었다면
            }
            else{ call.reject("권한요청 거부"); }
        }
    }

    @PluginMethod
    public void requestPermissions(PluginCall call){
        if(Build.VERSION.SDK_INT < 30){
            if((getPermissionState("MODIFY_AUDIO_SETTINGS") != PermissionState.GRANTED)
                    || (getPermissionState("READ_PHONE_STATE") != PermissionState.GRANTED)){
                requestPermissionForAliases(new String[]{"MODIFY_AUDIO_SETTINGS", "READ_PHONE_STATE",}, call, "permissionRequestResult");
            }
        }
        else if(Build.VERSION.SDK_INT >= 30){
            if((getPermissionState("MODIFY_AUDIO_SETTINGS") != PermissionState.GRANTED)
                    || (getPermissionState("READ_PHONE_STATE") != PermissionState.GRANTED)
                    || (getPermissionState("READ_PHONE_NUMBERS") != PermissionState.GRANTED)){
                requestPermissionForAliases(new String[]{"MODIFY_AUDIO_SETTINGS", "READ_PHONE_STATE", "READ_PHONE_NUMBERS"}, call, "permissionRequestResult");
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @PluginMethod
    public void requestPhoneScreening(PluginCall call){
        if(Build.VERSION.SDK_INT >= 29){
            RoleManager roleManager = (RoleManager) getContext().getSystemService(Context.ROLE_SERVICE);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            startActivityForResult(call,intent,"RollRequestResult");
        }
    }

    @ActivityCallback
    public void RollRequestResult(PluginCall call, ActivityResult result){
        if(call == null){
            return;
        }
        Intent intent = new Intent(getContext(), CallScreeningService.class);
        getContext().bindService(intent,conn,Context.BIND_AUTO_CREATE);
    }
}
