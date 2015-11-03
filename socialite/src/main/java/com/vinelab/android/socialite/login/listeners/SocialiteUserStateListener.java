package com.vinelab.android.socialite.login.listeners;

import com.vinelab.android.socialite.SocialiteCredentials;
import com.vinelab.android.socialite.SocialiteUtils.SOCIALITE_PROVIDER;

/**
 * Created by Nabil on 10/29/2015.
 *
 * <p>
 *     Interface definition for callbacks to be invoked when the user state is requested.
 * </p>
 */
public interface SocialiteUserStateListener {
    /**
     * Indicates that the user has already an active session (logged in previously).
     * @param provider The name of the provider executing the login.
     * @param credentials The credentials of the session created.
     */
    void onLoggedIn(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials);

    /**
     * Indicates that the user doesn't have any active session. He might not be logged in
     * previously or he has logged out the last time.
     * @param provider The name of the provider executing the login.
     */
    void onLoggedOut(SOCIALITE_PROVIDER provider);
}
