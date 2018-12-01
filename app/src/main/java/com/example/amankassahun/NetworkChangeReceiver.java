package com.example.amankassahun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Aman on 5/23/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static boolean isUpdated = false;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo activeNetwork = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.isConnected() && !isUpdated) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        Log.d("aziza", "disconcoNnectedwfi");
                        Intent servicIntent= BlogService.newIntent(context);
                        context.getApplicationContext().startService(servicIntent);
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        Log.d("aziza", "disconcoNnectedMob");
                        Intent servicIntent= BlogService.newIntent(context);
                        context.getApplicationContext().startService(servicIntent);
                    }

                    isUpdated = true;
                } else {
                    isUpdated = false;
                }
            }
        }





    }
}
