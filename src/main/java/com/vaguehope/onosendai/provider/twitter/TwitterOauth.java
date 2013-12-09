package com.vaguehope.onosendai.provider.twitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.vaguehope.onosendai.util.IoHelper;
import com.vaguehope.onosendai.util.StringHelper;

public final class TwitterOauth {

	public static final String CALLBACK_URL = "https://api.twitter.com/oauth/authenticate";

	public static final String IEXTRA_AUTH_URL = "auth_url";
	public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
	public static final String IEXTRA_OAUTH_TOKEN = "oauth_token";

//	- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	private static final String RES_PATH = "/api_twitter";
	private static final String DEF_KEY = "BLVZ4gI3m3YqdyxkCGbgeA";
	private static final String DEF_SECRET = "wQBrJFwdW62V08mqyCbk6xncL6zA6LmtfzUuegtYLM";

	private static boolean read = false;
	private static String key;
	private static String secret;

	private TwitterOauth () {
		throw new AssertionError();
	}

	public static String getConsumerKey () {
		//read();
		//return key;
		return DEF_KEY;
	}

	public static String getConsumerSecret () {
		//read();
		//return secret;
		return DEF_SECRET;
	}

	private static void read () {
		if (read) return;
		final BufferedReader r = new BufferedReader(new InputStreamReader(TwitterOauth.class.getResourceAsStream(RES_PATH), Charset.forName("UTF-8")));
		try {
			key = r.readLine();
			secret = r.readLine();
			if (StringHelper.isEmpty(key) || StringHelper.isEmpty(secret)
					|| DEF_KEY.equals(key) || DEF_SECRET.equals(secret))
				throw new IllegalStateException("API keys are missing.");
			read = true;
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to read internal API resource.", e);
		}
		finally {
			IoHelper.closeQuietly(r);
		}
	}

}
