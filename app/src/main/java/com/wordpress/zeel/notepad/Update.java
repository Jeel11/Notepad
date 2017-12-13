package com.wordpress.zeel.notepad;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by zeel on 25-06-2016.
 */
public class Update {

    public  static void startupdate(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,Widget.class);
        intent.setAction(Widget.ACTION_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC,System.currentTimeMillis(),120000,pendingIntent);
    }

    public static void stopUpdate(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context,Widget.class);
        intent.setAction(Widget.ACTION_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        alarmManager.cancel(pendingIntent);
    }
}
