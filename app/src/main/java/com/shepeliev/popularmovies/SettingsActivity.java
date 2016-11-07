package com.shepeliev.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

  private static final Preference.OnPreferenceChangeListener sPreferenceChangeListener =
      new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
          String stringValue = newValue.toString();
          if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);
            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
          } else {
            preference.setSummary(stringValue);
          }

          return true;
        }
      };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    addPreferencesFromResource(R.xml.preferences);
    bindValueToSummary(findPreference(getString(R.string.pref_sort_key)));
  }

  private void bindValueToSummary(Preference preference) {
    preference.setOnPreferenceChangeListener(sPreferenceChangeListener);
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    String value = preferences.getString(preference.getKey(), "");
    sPreferenceChangeListener.onPreferenceChange(preference, value);
  }
}
