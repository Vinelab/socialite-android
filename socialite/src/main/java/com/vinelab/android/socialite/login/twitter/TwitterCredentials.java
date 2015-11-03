package com.vinelab.android.socialite.login.twitter;

import com.vinelab.android.socialite.SocialiteCredentials;

/**
 * Created by Nabil Souk on 10/29/2015.
 *
 * <p>
 *     Object holding the credentials of a Twitter active session.
 * </p>
 */
public class TwitterCredentials extends SocialiteCredentials {
    private String token;
    private String secret;

    public TwitterCredentials(String userId, String token, String secret) {
        super(userId);
        this.token = token;
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }
}
