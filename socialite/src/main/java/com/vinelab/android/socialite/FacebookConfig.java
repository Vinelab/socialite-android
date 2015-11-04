package com.vinelab.android.socialite;

import android.content.Context;

import com.facebook.FacebookSdk;

/**
 * Created by Nabil Souk on 11/4/2015.
 *
 * <p>
 *     Class handling the configuration of Facebook.
 * </p>
 */
public class FacebookConfig {
    /**
     * Initializes the Facebook SDK.
     */
    public static void initializeSDK(Context context) {
        FacebookSdk.sdkInitialize(context);
    }
}
