package com.mohamedibrahim.nearbyme.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Mohamed Ibrahim
 * on 5/5/2017.
 */

public class WidgetService  extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this);
    }
}
