package com.vaguehope.onosendai.ui.pref;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.vaguehope.onosendai.config.Theme;
import com.vaguehope.onosendai.config.Prefs;
import com.vaguehope.onosendai.util.DialogHelper;
import com.vaguehope.onosendai.util.DialogHelper.Listener;

public class ThemesPrefFragment extends PreferenceFragment {

	private Prefs prefs;

	@Override
	public void onCreate (final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setPreferenceScreen(getPreferenceManager().createPreferenceScreen(getActivity()));
		this.prefs = new Prefs(getPreferenceManager());
		refreshThemesList();
	}

	protected void refreshThemesList() {
		getPreferenceScreen().removeAll();

		final Preference pref = new Preference(getActivity());
		pref.setTitle("Changes Themes");
		pref.setSummary("Select and change the ideal theme");
		pref.setOnPreferenceClickListener(new AddThemeClickListener(this));
		getPreferenceScreen().addPreference(pref);
	}

	protected void promptThemeTypes () {
		DialogHelper.askItem(getActivity(), "Themes",
				new Theme[] { Theme.LIGHT, Theme.BLACK },
				new Listener<Theme>() {
					@Override
					public void onAnswer (final Theme theme) {
						promptChangeTheme(theme);
					}
				});
	}

	protected void promptChangeTheme (final Theme theme) {
		switch (theme) {
			case LIGHT:
				this.prefs.setTheme(Theme.LIGHT);
				DialogHelper.alert(getActivity(), "Click Ok and Change to the LIGHT theme ");
				break;
			case BLACK:
				this.prefs.setTheme(Theme.BLACK);
				DialogHelper.alert(getActivity(), "Click Ok and Change to the BLACK theme");
				break;
			default:
				DialogHelper.alert(getActivity(), "Do not know the selected theme");
		}
	}

	private static class AddThemeClickListener implements OnPreferenceClickListener {

		private final ThemesPrefFragment themesPrefFragment;

		public AddThemeClickListener (final ThemesPrefFragment themesPrefFragment) {
			this.themesPrefFragment = themesPrefFragment;
		}

		@Override
		public boolean onPreferenceClick (final Preference preference) {
			this.themesPrefFragment.promptThemeTypes();
			return true;
		}
	}
}