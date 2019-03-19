package com.example.amankassahun;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Aman on 5/4/2018.
 */

public class Keeper extends Service {
    private NetworkChangeReceiver mNetworkChangeReceiver=null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){

        return super.onStartCommand(intent, flags,startId);
    }

    @Override
    public void onCreate(){
        super.onCreate();

        IntentFilter intentFilter= new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.setPriority(100);
        mNetworkChangeReceiver= new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver,intentFilter);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mNetworkChangeReceiver!=null){
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }
}
