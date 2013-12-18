package com.vaguehope.onosendai.ui;

import com.vaguehope.onosendai.R;
import com.vaguehope.onosendai.config.Theme;

public class ThemeManager {

	public static int getTheme(Theme theme) {
		switch (theme) {
			case LIGHT:
				return R.style.LightTheme;
			case BLACK:
				return R.style.BlackTheme;
			default:
				return R.style.BlackTheme;
		}
	}
}