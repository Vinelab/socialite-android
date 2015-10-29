package com.vinelab.android.sociallogin.socialite.facebook;

import com.vinelab.android.sociallogin.socialite.SocialiteCredentials;

/**
 * Created by Nabil Souk on 10/29/2015.
 *
 * <p>
 *     Object holding the credentials of a Facebook active session.
 * </p>
 */
public class FacebookCredentials extends SocialiteCredentials {
    private String token;

    public FacebookCredentials(String userId, String token) {
        super(userId);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
