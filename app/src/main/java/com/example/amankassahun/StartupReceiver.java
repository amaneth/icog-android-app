package com.example.amankassahun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Aman on 5/9/2018.
 */

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED")
                || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON")
                || intent.getAction().equals("com.htc.intent.action.QUICKBOOT_POWERON"))){
            Toast.makeText(context, "FIRED BOOT COMPLETE" , Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Received broadcast intent: " + intent.getAction());

        boolean isOn = QueryPreferences.isAlarmOn(context);
        PollService.setServiceAlarm(context, isOn);
    }
}
