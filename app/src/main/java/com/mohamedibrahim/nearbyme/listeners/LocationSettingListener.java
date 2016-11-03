package com.mohamedibrahim.nearbyme.listeners;

import android.content.Intent;

/**
 * Created by Mohamed Ibrahim on 11/1/2016.
 **/

public interface LocationSettingListener {
    void onRequestResult(int requestCode, int resultCode, Intent Date);
}
