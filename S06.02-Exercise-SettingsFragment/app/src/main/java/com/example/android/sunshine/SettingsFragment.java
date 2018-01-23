package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/**
 * S06.02-Exercise-SettingsFragment
 *
 * @author Solange U. Gasengayire.
 */

// COMPLETED (4) Create SettingsFragment and extend PreferenceFragmentCompat
// COMPLETED (10) Implement OnSharedPreferenceChangeListener from SettingsFragment
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    // COMPLETED (5) Override onCreatePreferences
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Add the preference xml file using addPreferencesFromResource
        addPreferencesFromResource(R.xml.pref_visualizer);

        // COMPLETED (9) Set the preference summary on each preference that isn't a CheckBoxPreference
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        // Go through all of the preferences, and set up their preference summary.
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }

    // COMPLETED (8) Create a method called setPreferenceSummary that accepts a Preference and an Object
    // and sets the summary of the preference

    /**
     * Updates the summary for the provided preference
     * @param preference The preference to be updated
     * @param valueObj The value object that the preference is updated to
     */
    private void setPreferenceSummary(Preference preference, Object valueObj) {
        String value = valueObj.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, figure out the label of the selected value
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if (prefIndex >= 0) {
                // Set the summary to that label
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (preference instanceof EditTextPreference) {
            // For EditTextPreferences, set the summary to the value's simple string representation.
            preference.setSummary(value);
        }
    }

    // COMPLETED (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when they are changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Figure out which preference was changed
        Preference preference = findPreference(key);
        if (preference != null) {
            // Updates the summary for the preference
            if (!(preference instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(key, "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    // COMPLETED (12) Register SettingsFragment (this) as a SharedPreferenceChangedListener in onStart
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    // COMPLETED (13) Unregister SettingsFragment (this) as a SharedPreferenceChangedListener in onStop
    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
