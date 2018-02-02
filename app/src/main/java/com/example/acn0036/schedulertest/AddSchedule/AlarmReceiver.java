package com.example.acn0036.schedulertest.AddSchedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ACN0036 on 2017-10-25.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mAlarmIntent = new Intent("com.example.acn0036.schedulertest.ALARM_START");
        throw new UnsupportedOperationException("NOT YET implemented");
    }
}
