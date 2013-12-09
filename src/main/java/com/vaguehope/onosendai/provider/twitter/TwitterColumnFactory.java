package com.vaguehope.onosendai.provider.twitter;

import com.vaguehope.onosendai.config.Account;
import com.vaguehope.onosendai.config.AccountProvider;
import com.vaguehope.onosendai.config.Column;

public final class TwitterColumnFactory {

	private static final int DEFAULT_REFRESH_MINS = 30;

	private TwitterColumnFactory () {
		throw new AssertionError();
	}

	public static Column homeTimeline (final int id, final Account account) {
		checkAccount(account);
		return new Column(id, "Home Timeline", account.getId(), MainFeeds.TIMELINE.name(), DEFAULT_REFRESH_MINS, null, false);
	}

	public static Column sortByThread (final int id, final Account account) {
		checkAccount(account);
		return new Column(id, "Sort by Thread", account.getId(), MainFeeds.SORTBYTHREAD.name(), DEFAULT_REFRESH_MINS, null, false);
	}

	public static Column mentions (final int id, final Account account) {
		checkAccount(account);
		return new Column(id, "Mentions", account.getId(), MainFeeds.MENTIONS.name(), DEFAULT_REFRESH_MINS, null, false);
	}

	private static void checkAccount (final Account account) {
		if (account == null) throw new IllegalArgumentException("Account must not be null.");
		if (account.getProvider() != AccountProvider.TWITTER) throw new IllegalArgumentException("Account must be of type Twitter.");
	}

}
