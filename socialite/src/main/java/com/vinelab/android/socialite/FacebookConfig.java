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
    public static SocialiteUtils.SOCIALITE_PROVIDER provider = SocialiteUtils.SOCIALITE_PROVIDER.FACEBOOK;
    public static int SHARE_REQUEST_CODE = 23;
    /**
     * Initializes the Facebook SDK.
     */
    public static void initializeSDK(Context context) {
        FacebookSdk.sdkInitialize(context);
    }
}
