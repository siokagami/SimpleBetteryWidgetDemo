package com.siokagami.simplebetterywidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

/**
 * Created by SiO鏡 on 2016/2/22.
 */
public class SimpleBatteryService extends Service {
    private static int currentBatteryLevel;
    private static int currentBatteryStatus;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private BroadcastReceiver batteryReceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            currentBatteryLevel=intent.getIntExtra("level", 0);
            currentBatteryStatus=intent.getIntExtra("status", 0);
        }

    };
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        AppWidgetManager manager= AppWidgetManager.getInstance(this);
        RemoteViews views=new RemoteViews(getPackageName(),R.layout.activity_simple_battery_start);

        //设置widget显示电池电量大小
        views.setTextViewText(R.id.battery_status,currentBatteryLevel+"%");

        ComponentName simpleBatteryWidget=new ComponentName(this,SimpleBatteryStart.class);

        long now=System.currentTimeMillis();
        long pause=1000;

        Intent alarmIntent=new Intent();
        alarmIntent=intent;

        PendingIntent pendingIntent= PendingIntent.getService(this, 0, alarmIntent, 0);
        AlarmManager alarm=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, now + pause, pendingIntent);

        manager.updateAppWidget(simpleBatteryWidget, views);
    }
}
