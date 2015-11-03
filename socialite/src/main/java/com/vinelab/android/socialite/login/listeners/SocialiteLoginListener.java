package com.vinelab.android.socialite.login.listeners;

import android.support.annotation.Nullable;

import com.vinelab.android.socialite.SocialiteCredentials;
import com.vinelab.android.socialite.SocialiteUtils.SOCIALITE_PROVIDER;

/**
 * Created by Nabil Souk on 10/29/2015.
 *
 * <p>
 *     Interface definition for callbacks to be invoked when a login is triggered.
 * </p>
 */
public interface SocialiteLoginListener {
    /**
     * Indicates a successful login.
     * @param provider The name of the provider executing the login.
     * @param credentials The credentials of the session created.
     */
    void onSuccess(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials);

    /**
     * Indicates that the user cancelled the login.
     * @param provider The name of the provider executing the login.
     */
    void onCancel(SOCIALITE_PROVIDER provider);

    /**
     * Indicates that the login failed.
     * @param provider The name of the provider executing the login.
     * @param error The error message returned by the provider.
     */
    void onError(SOCIALITE_PROVIDER provider, @Nullable String error);
}
