package com.softnet.speakerphone;

import android.Manifest;
import android.app.Instrumentation;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
                        alias = "READ_CALL_LOG",
                        strings = {Manifest.permission.READ_CALL_LOG}
                )
        }
)
public class SpeakerPhonePlugin extends Plugin {
    private SpeakerPhone implementation = new SpeakerPhone();

    @Override
    public void load() {
        super.load();
    }

    @PermissionCallback
    public void permissionRequestResult(PluginCall call){
        if ((getPermissionState("MODIFY_AUDIO_SETTINGS") == PermissionState.GRANTED)
                && (getPermissionState("READ_PHONE_STATE") == PermissionState.GRANTED)
                && (getPermissionState("READ_CALL_LOG") == PermissionState.GRANTED)){
            // 만약 오디오 설정 권한요청과, 폰 상태 읽어오는 권한 되었다면
            JSObject jsObject = new JSObject();
            jsObject.put("result", "권한 요청 승인 완료");
            call.resolve(jsObject);
        }
        else{ call.reject("권한요청 거부"); }
    }

    @PluginMethod
    public void requestPermissions(PluginCall call){
        if((getPermissionState("MODIFY_AUDIO_SETTINGS") != PermissionState.GRANTED)
                || (getPermissionState("READ_PHONE_STATE") != PermissionState.GRANTED)
                || (getPermissionState("READ_CALL_LOG") != PermissionState.GRANTED)){
            requestPermissionForAliases(new String[]{"MODIFY_AUDIO_SETTINGS", "READ_PHONE_STATE", "READ_CALL_LOG"}, call, "permissionRequestResult");
        }
        else {
            JSObject jsObject = new JSObject();
            jsObject.put("result","권한 요청이 이미 모두 grated 상태");
            call.resolve(jsObject);
        }
    }

    @PluginMethod
    public void setRegNumber(PluginCall call) throws JSONException {
        JSObject jsObject = new JSObject();
        List<String> numberList = call.getArray("number").toList();

        Set<String> regNumber = new HashSet<>((ArrayList<String>) numberList);
        regist_CenterNumber(getContext(), regNumber);

        jsObject.put("result", "저장완료");
        call.resolve(jsObject);
    }

    private void regist_CenterNumber(Context context, Set<String> regNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences("callCenter_Info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("tel",regNumber);
        editor.commit();
    }
}
