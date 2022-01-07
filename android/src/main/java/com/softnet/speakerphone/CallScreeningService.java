package com.softnet.speakerphone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallScreeningService extends android.telecom.CallScreeningService {

    @Override
    public void onScreenCall(@NonNull Call.Details callDetails) {
        String phoneNumber = callDetails.getHandle().getSchemeSpecificPart();
        Log.v("CallTest","Phone Number : "+phoneNumber);

        Intent intent = new Intent(this,PhoneCallReceiver.class);
        intent.putExtra("state","onScreenCall");
        intent.putExtra("phoneNumber",phoneNumber);
        sendBroadcast(intent);
        return;
    }
}