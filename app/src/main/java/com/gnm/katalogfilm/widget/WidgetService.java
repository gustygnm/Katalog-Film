package com.gnm.katalogfilm.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new com.gnm.katalogfilm.widget.RemoteViewsFactory(this.getApplicationContext(), intent);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
