package com.baharapp.calendar.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baharapp.calendar.util.UpdateUtils;
import com.baharapp.calendar.util.Utils;

import java.lang.ref.WeakReference;

/**
 * The Calendar Service that updates widget time and clock and build/update
 * calendar notification.
 *
 * @author Ebrahim Byagowi <ebrahim@byagowi.com>
 */
public class ApplicationService extends Service {

    private static WeakReference<ApplicationService> instance;

    @Nullable
    public static ApplicationService getInstance() {
        return instance == null ? null : instance.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = new WeakReference<>(this);
        Log.d(ApplicationService.class.getName(), "start");
        UpdateUtils updateUtils = UpdateUtils.getInstance(getApplicationContext());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(new BroadcastReceivers(), intentFilter);

        Utils utils = Utils.getInstance(getBaseContext());
        utils.loadApp();
        updateUtils.update(true);

        return START_STICKY;
    }
}
