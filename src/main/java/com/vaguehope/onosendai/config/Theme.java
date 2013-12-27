package com.vaguehope.onosendai.config;

import com.vaguehope.onosendai.util.Titleable;
public enum Theme implements Titleable {
	LIGHT("Light"),
	BLACK("Black");

	private final String theme;

	private Theme (final String theme) {
		this.theme = theme;
	}

	public String getTheme() {
		return this.theme;
	}

	@Override
	public String getUiTitle () {
		return this.theme;
	}

}