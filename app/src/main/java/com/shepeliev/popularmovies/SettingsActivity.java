package com.shepeliev.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    addPreferencesFromResource(R.xml.preferences);

    updateSummaries();
  }

  private void updateSummaries() {
    ListPreference listPreference =
        (ListPreference) findPreference(getString(R.string.pref_sort_key));
    listPreference.setSummary(listPreference.getEntry());
  }
}
