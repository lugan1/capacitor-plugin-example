package com.softnet.speakerphone;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

@CapacitorPlugin(
        name = "SpeakerPhone",
        permissions = {
                @Permission(
                        alias = "speaker",
                        strings = {Manifest.permission.MODIFY_AUDIO_SETTINGS }
                ),

                @Permission(
                        alias = "readPhoneState",
                        strings = {Manifest.permission.READ_PHONE_STATE}
                )
        }
)
public class SpeakerPhonePlugin extends Plugin {
    private boolean speakerphoneFlag = false;

    private SpeakerPhone implementation = new SpeakerPhone();

    @Override
    public void load() {
        super.load();
    }

    @PluginMethod
    public void setSpeakerphoneOn(PluginCall call){
        if(getPermissionState("speaker") != PermissionState.GRANTED){
            requestPermissionForAliases(new String[]{"speaker", "readPhoneState"}, call, "permissionRequestResult");
        }

        if(getPermissionState("readPhoneState") != PermissionState.GRANTED){
            requestPermissionForAlias("readPhoneState", call, "permissionRequestResult");
        }

        speakerphoneFlag = call.getBoolean("value");
        if(speakerphoneFlag == true){
            Intent intent = new Intent(getContext(), CallingService.class);
            getContext().startService(intent);
        }

        if (speakerphoneFlag == false){
            Intent intent = new Intent(getContext(), CallingService.class);
            getContext().stopService(intent);
        }

    }

    @PermissionCallback
    public void permissionRequestResult(PluginCall call){
        if ((getPermissionState("speaker") == PermissionState.GRANTED) && (getPermissionState("readPhoneState") == PermissionState.GRANTED)) {
            // 만약 오디오 설정 권한요청과, 폰 상태 읽어오는 권한 허락되었다면
        }
        else{
            call.reject("권한요청 거부");
        }
    }



}
