package com.by.alex.depositcalcupd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    CheckBoxPreference ut, rt;
    SharedPreferences mSettings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager prefMgr = getPreferenceManager();
        prefMgr.setSharedPreferencesName(MainActivity.APP_PREFERENCES);
        prefMgr.setSharedPreferencesMode(MODE_PRIVATE);

        addPreferencesFromResource(R.xml.prefrences);

        mSettings = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
        mSettings.registerOnSharedPreferenceChangeListener(this);

        ut = (CheckBoxPreference)findPreference("ukr_tax");
        rt = (CheckBoxPreference)findPreference("russian_tax");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key){
            case "russian_tax":
                if (rt.isChecked()) {
                    ut.setChecked(false);

                    SharedPreferences.Editor ed = mSettings.edit();
                    ed.putBoolean("ukr_tax", false); // your updated setting
                    ed.commit();
                }
                break;
            case "ukr_tax":
                if (ut.isChecked()) {
                    rt.setChecked(false);

                    SharedPreferences.Editor edt = mSettings.edit();
                    edt.putBoolean("russian_tax", false); // your updated setting
                    edt.commit();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
// Set up a listener whenever a key changes
        mSettings.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
// Unregister the listener whenever a key changes
        mSettings.unregisterOnSharedPreferenceChangeListener(this);
    }
}
