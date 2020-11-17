package com.hadyan.githubuser.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.hadyan.githubuser.AlarmReceiver;
import com.hadyan.githubuser.R;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    private String REMINDER;

    private SwitchPreference reminderPref;

    private AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        alarmReceiver = new AlarmReceiver();

        init();
        setSummaries();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(REMINDER)) {
            Boolean isActive = sharedPreferences.getBoolean(REMINDER, false);

            reminderPref.setChecked(sharedPreferences.getBoolean(REMINDER, false));

            if (isActive) {
                String repeatTime = "09:00";
                String repeatMessage = "Don't forget to check your favourite github's users";
                alarmReceiver.setRepeatingAlarm(getActivity(), AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage);
            } else {
                alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_REPEATING);
            }
        }
    }

    private void init(){
        REMINDER = getResources().getString(R.string.key_reminder);

        reminderPref = (SwitchPreference) findPreference(REMINDER);

    }

    private void setSummaries(){
        SharedPreferences sh = getPreferenceManager().getSharedPreferences();
        reminderPref.setChecked(sh.getBoolean(REMINDER, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
