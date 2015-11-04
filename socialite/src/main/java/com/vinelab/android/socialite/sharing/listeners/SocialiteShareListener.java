package com.vinelab.android.socialite.sharing.listeners;

import android.support.annotation.Nullable;

import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.SocialiteShareExtras;

/**
 * Created by Nabil Souk on 11/2/2015.
 *
 * <p>
 *     Interface definition for callbacks to be invoked when a share operation is triggered.
 * </p>
 */
public interface SocialiteShareListener {
    /**
     * Indicates a successful share.
     * @param provider The name of the provider executing the operation.
     * @param extras Any extra data sent back from the provider.
     */
    void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable SocialiteShareExtras extras);

    /**
     * Indicates that the user cancelled the operation.
     * @param provider The name of the provider executing the operation.
     */
    void onCancel(SocialiteUtils.SOCIALITE_PROVIDER provider);

    /**
     * Indicates that the operation failed.
     * @param provider The name of the provider executing the login.
     * @param error The error message returned by the provider.
     */
    void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable String error);
}
