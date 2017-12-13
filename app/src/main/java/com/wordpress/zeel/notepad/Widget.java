package com.wordpress.zeel.notepad;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by zeel on 18-06-2016.
 */
public class Widget extends AppWidgetProvider {


    public static final String ACTION_UPDATE = "com.wordpress.zeel.notepad.action.UPDATE";
    public static final String LIST_ROW_NUMBER="myPackage.LIST_ROW_NUMBER";
    public static int randomNo;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Random random = new Random();
        randomNo=random.nextInt(10000)+9999;
        final int n=appWidgetIds.length;

        for(int i=0;i<n;i++){
            int id=appWidgetIds[i];
            Intent intent=new Intent(context,Note.class);
            PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views=new RemoteViews(context.getPackageName(),R.layout.widget);

            views.setOnClickPendingIntent(R.id.button, pendingIntent);
            Intent intent1=new Intent(context,WidgetService.class);
            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intent1.setData(Uri.fromParts("content", String.valueOf(appWidgetIds[i] + randomNo), null));
            views.setRemoteAdapter(R.id.list1, intent1);
            views.setEmptyView(R.id.list1, R.id.emptyView);
            Intent intent2=new Intent(context,Widget.class);
            intent2.setAction(LIST_ROW_NUMBER);
            intent2.setData(Uri.parse(intent2.toUri(Intent.URI_INTENT_SCHEME)));
            intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            PendingIntent pi = PendingIntent.getBroadcast(context,0,intent2,0);
            views.setPendingIntentTemplate(R.id.list1, pi);
            appWidgetManager.updateAppWidget(id, views);
        }




    }

    @Override
    public void onReceive(Context context, Intent intent) {

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context.getPackageName(),getClass().getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(context,appWidgetManager,appWidgetIds);

            if (LIST_ROW_NUMBER.equals(intent.getAction())){
                int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
                AppWidgetManager appWidgetManager1 = AppWidgetManager.getInstance(context);
                int viewInt = intent.getIntExtra(LIST_ROW_NUMBER,0);
                Intent intent1 = new Intent(context,Main23Activity.class);
                intent1.setAction(LIST_ROW_NUMBER);
                intent1.putExtra("position",viewInt);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Toast.makeText(context, "widget is deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context) {
        Update.startupdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        Update.stopUpdate(context);
    }
}
