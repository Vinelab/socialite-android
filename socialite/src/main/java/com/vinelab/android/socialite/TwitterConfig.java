package com.vinelab.android.socialite;

import android.content.Context;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nabil Souk on 11/3/2015.
 *
 * <p>
 *     Class handling the configuration of Twitter kits.
 * </p>
 */
public class TwitterConfig {
    public static SocialiteUtils.SOCIALITE_PROVIDER provider = SocialiteUtils.SOCIALITE_PROVIDER.TWITTER;
    public static int COMPOSER_REQUEST_CODE = 24;

    /**
     * Initializes the needed Twitter kits.
     * @param context The application context.
     * @param initComposer indicating whether to initialize the composer kit along or not.
     */
    public static void initializeKits(Context context, boolean initComposer, String consumerKey, String consumerSecret) {
        // initialize the SDK
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(consumerKey, consumerSecret);
        if(initComposer) {
            Fabric.with(context, new TwitterCore(authConfig), new TweetComposer());
        }
        else {
            Fabric.with(context, new TwitterCore(authConfig));
        }
    }
}
