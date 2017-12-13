package com.wordpress.zeel.notepad;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by zeel on 20-06-2016.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int id = Integer.valueOf(intent.getData().getSchemeSpecificPart())-Widget.randomNo;
        return new Listprovider(this.getApplicationContext(),intent);
    }
}
