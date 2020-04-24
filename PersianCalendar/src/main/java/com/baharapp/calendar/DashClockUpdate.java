package com.baharapp.calendar;

import com.baharapp.calendar.util.UpdateUtils;
import com.google.android.apps.dashclock.api.DashClockExtension;

public class DashClockUpdate extends DashClockExtension {

    @Override
    protected void onUpdateData(int reason) {
        setUpdateWhenScreenOn(true);
        UpdateUtils updateUtils = UpdateUtils.getInstance(getApplicationContext());
        updateUtils.update(false);
        publishUpdate(updateUtils.getExtensionData());
    }

}
