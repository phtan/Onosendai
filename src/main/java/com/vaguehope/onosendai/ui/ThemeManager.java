package com.vaguehope.onosendai.ui;

import com.vaguehope.onosendai.R;
public class ThemeManager {

	public static int THEME = 0;
	public static int getTheme() {
		switch (ThemeManager.THEME) {
			case 0:
				return R.style.LightTheme;
			case 1:
				return R.style.BlackTheme;
			default:
				return R.style.BlackTheme;
		}
	}
}