package com.baharapp.calendar.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.baharapp.calendar.Constants;
import com.baharapp.calendar.R;
import com.baharapp.calendar.util.Utils;
import com.baharapp.calendar.view.preferences.AthanNumericDialog;
import com.baharapp.calendar.view.preferences.AthanNumericPreference;
import com.baharapp.calendar.view.preferences.AthanVolumeDialog;
import com.baharapp.calendar.view.preferences.AthanVolumePreference;
import com.baharapp.calendar.view.preferences.GPSLocationDialog;
import com.baharapp.calendar.view.preferences.GPSLocationPreference;
import com.baharapp.calendar.view.preferences.LocationPreference;
import com.baharapp.calendar.view.preferences.LocationPreferenceDialog;
import com.baharapp.calendar.view.preferences.PrayerSelectDialog;
import com.baharapp.calendar.view.preferences.PrayerSelectPreference;
import com.baharapp.calendar.view.preferences.ShapedListDialog;
import com.baharapp.calendar.view.preferences.ShapedListPreference;

/**
 * Preference activity
 *
 * @author ebraminio
 */
public class ApplicationPreferenceFragment extends PreferenceFragmentCompat {
    private Preference categoryAthan;
    private Utils utils;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.settings), "");

        addPreferencesFromResource(R.xml.preferences);

        categoryAthan = findPreference(Constants.PREF_KEY_ATHAN);
        updateAthanPreferencesState();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(preferenceUpdateReceiver,
                new IntentFilter(Constants.LOCAL_INTENT_UPDATE_PREFERENCE));
    }

    private BroadcastReceiver preferenceUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateAthanPreferencesState();
        }
    };

    @Override
    public void onDestroyView() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(preferenceUpdateReceiver);
        super.onDestroyView();
    }

    public void updateAthanPreferencesState() {
        boolean locationEmpty = utils.getCoordinate() == null;
        categoryAthan.setEnabled(!locationEmpty);
        if (locationEmpty) {
            categoryAthan.setSummary(R.string.athan_disabled_summary);
        } else {
            categoryAthan.setSummary("");
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;
        if (preference instanceof PrayerSelectPreference) {
            fragment = new PrayerSelectDialog();
        } else if (preference instanceof AthanVolumePreference) {
            fragment = new AthanVolumeDialog();
        } else if (preference instanceof LocationPreference) {
            fragment = new LocationPreferenceDialog();
        } else if (preference instanceof AthanNumericPreference) {
            fragment = new AthanNumericDialog();
        } else if (preference instanceof GPSLocationPreference) {
            fragment = new GPSLocationDialog();
        } else if (preference instanceof ShapedListPreference) {
            fragment = new ShapedListDialog();
        } else {
            super.onDisplayPreferenceDialog(preference);
        }

        if (fragment != null) {
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            fragment.setArguments(bundle);
            fragment.setTargetFragment(this, 0);
            fragment.show(getFragmentManager(), fragment.getClass().getName());        }
    }
}
