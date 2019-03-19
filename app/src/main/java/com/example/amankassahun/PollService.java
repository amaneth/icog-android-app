package com.example.amankassahun;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aman on 5/9/2018.
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    public static final String ACTION_SHOW_NOTIFICATION =
            "com.bignerdranch.android.Notificationgallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE =
            "com.bignerdranch.android.Notificationgallery.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";
    private static final String latestUrl="http://www.icog-labs.com/news/";
    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        QueryPreferences.setAlarmOn(context, isOn);
    }
    public static boolean isServiceAlarmOn(Context context) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }
    public PollService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        String lastResultId = QueryPreferences.getLastResultId(this);
        List<NotificationGallery> items;
            items = new WebsiteFitcher().fetchRecentPhotos(latestUrl,getApplicationContext());

        if (items.size() == 0) {
            return;
        }
        String resultId = items.get(0).getId();

        if (!resultId.equals(lastResultId)) {
            Intent i = NotifyActivity.newIntent(this,latestUrl);
           //Uri uri= Uri.parse("http://www.uog.edu.et/wp-content/uploads/2013/03/biniam.mp3");
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
            String url= items.get(0).getUrl();
            byte[] bitmapBytes = new byte[0];
            try {
                bitmapBytes = new WebsiteFitcher().getUrlBytes(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Notification notification = new NotificationCompat.Builder(this,"M_CH_ID")
                    .setTicker(items.get(0).getTitle())
                    .setSmallIcon(R.drawable.action_notification)
                    .setLargeIcon(bitmap)
                    .setContentTitle(items.get(0).getTitle())
                    .setContentText(items.get(0).getContent())
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();

            showBackgroundNotification(0, notification);
        }
        QueryPreferences.setLastResultId(this, resultId);
    }
    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);

 sendOrderedBroadcast(i,PERM_PRIVATE, null, null,
             Activity.RESULT_OK, null, null);
    }
}
