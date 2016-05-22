package com.vinelab.android.socialite.login.listeners;

import android.support.annotation.Nullable;

import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.login.SocialiteUserProfile;

/**
 * Created by Nabil on 5/22/2016.
 * <p>
 *     Interface definition for callbacks to be invoked when
 *     fetching a soocial user profile is triggered.
 * </p>
 */
public interface SocialiteProfileListener {

    /**
     * Indicates a successful operation.
     * @param provider The name of the provider executing the login.
     * @param profile The object containing the user profile.
     */
    void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteUserProfile profile);

    /**
     * Indicates that the login failed.
     * @param provider The name of the provider executing the login.
     * @param error The error message returned by the provider.
     */
    void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable String error);
}
