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
    private static int resConsumerKey = R.string.twitter_consumer_key;
    private static int resConsumerSecret = R.string.twitter_consumer_secret;

    /**
     * Initializes the needed Twitter kits.
     * @param context The application context.
     * @param initComposer indicating whether to initialize the composer kit along or not.
     */
    public static void initializeKits(Context context, boolean initComposer) {
        // get consumer credentials from xml
        String consumerKey = context.getResources().getString(resConsumerKey);
        String consumerSecret = context.getResources().getString(resConsumerSecret);
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
