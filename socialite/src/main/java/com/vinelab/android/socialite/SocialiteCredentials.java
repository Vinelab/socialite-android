package com.vinelab.android.socialite;

/**
 * Created by Nabil Souk on 10/29/2015.
 *
 * <p>
 *     Object holding the credentials returned after a successful login.
 * </p>
 */
public class SocialiteCredentials {
    private String userId;

    public SocialiteCredentials(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
