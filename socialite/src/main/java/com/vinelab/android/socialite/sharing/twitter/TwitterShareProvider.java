package com.vinelab.android.socialite.sharing.twitter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.vinelab.android.socialite.TwitterConfig;
import com.vinelab.android.socialite.sharing.listeners.SocialiteShareListener;

/**
 * Created by Nabil Souk on 11/3/2015.
 *
 * <p>
 *     Singleton providing share operations on Twitter.
 * </p>
 */
public class TwitterShareProvider {
    private static TwitterShareProvider shareProvider;
    private SocialiteShareListener shareListener;

    private TwitterShareProvider() {}

    public static TwitterShareProvider getInstance() {
        if(shareProvider == null)   shareProvider = new TwitterShareProvider();
        return shareProvider;
    }

    /**
     * Requests the Twitter app to open the Tweet Composer element in order to post a new tweet.
     * If the Twitter app is not installed, a web page requesting the user credentials first will open.
     * @param activity The activity requesting the share process.
     * @param tweet The tweet to be pre-filled.
     * @param listener The callback listener.
     */
    public void composeTweet(Activity activity, @NonNull String tweet, @Nullable SocialiteShareListener listener) {
        // set listener
        shareListener = listener;
        // create compose intent
        Intent intent = new TweetComposer.Builder(activity)
                .text(tweet)
                .createIntent();
        // start the intent
        activity.startActivityForResult(intent, TwitterConfig.COMPOSER_REQUEST_CODE);
    }

    /**
     * Called to check the response of the share request.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == TwitterConfig.COMPOSER_REQUEST_CODE && shareListener != null) {
            if(resultCode == Activity.RESULT_CANCELED)  shareListener.onCancel(TwitterConfig.provider);
            if(resultCode == Activity.RESULT_OK)    shareListener.onSuccess(TwitterConfig.provider, null);
        }
    }
}
