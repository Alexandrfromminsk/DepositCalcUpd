package com.by.alex.depositcalcupd;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(MainActivity.APP_PREFERENCES);
        prefMgr.setSharedPreferencesMode(MODE_PRIVATE);

        addPreferencesFromResource(R.xml.prefrences);
    }
}
