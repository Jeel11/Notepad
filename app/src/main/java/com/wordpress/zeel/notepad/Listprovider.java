package com.wordpress.zeel.notepad;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by zeel on 20-06-2016.
 */
public class Listprovider implements RemoteViewsService.RemoteViewsFactory {
    public  static ArrayList<String> result =new ArrayList<String>();
    private Context context;
    private int id;
    public Listprovider(Context context,Intent intent){
        this.context=context;
        this.id=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        populatedListView();
    }



    public void populatedListView(){
        NotesDb mDbHelper = new NotesDb(context);

        try {
            mDbHelper.open();
            //  mDbHelper.deleteEntry("notes");
            result=mDbHelper.getData();

            mDbHelper.close();
            // Toast.makeText(Main2Activity.this,"size "+ result.size(),Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews= new RemoteViews(context.getPackageName(),R.layout.list_row);
        remoteViews.setTextViewText(R.id.txt3,result.get(position));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Widget.LIST_ROW_NUMBER,position);
        remoteViews.setOnClickFillInIntent(R.id.txt3,fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
