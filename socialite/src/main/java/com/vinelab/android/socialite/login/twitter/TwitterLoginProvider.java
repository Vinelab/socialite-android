package com.vinelab.android.socialite.login.twitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vinelab.android.socialite.R;
import com.vinelab.android.socialite.TwitterConfig;
import com.vinelab.android.socialite.login.listeners.SocialiteLoginListener;
import com.vinelab.android.socialite.login.listeners.SocialiteUserStateListener;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nabil Souk on 10/28/2015.
 *
 * <p>
 *     Class providing Twitter login functionality.
 * </p>
 */
public class TwitterLoginProvider {
    private static TwitterLoginProvider twitterLoginProvider;
    private TwitterAuthClient twitterAuthClient;
    private SocialiteLoginListener loginListener;

    private TwitterLoginProvider() {
        initialize();
    }

    public static TwitterLoginProvider getInstance() {
        if(twitterLoginProvider == null)    twitterLoginProvider = new TwitterLoginProvider();
        return twitterLoginProvider;
    }

    /**
     * Initializes the tools needed.
     */
    private void initialize() {
        // auth client
        twitterAuthClient = new TwitterAuthClient();
    }

    /**
     * Checks if the user has an active session (still logged in).
     * @param loginStateListener Callback listener for the user state.
     */
    public void isUserLoggedIn(SocialiteUserStateListener loginStateListener) {
        if(loginStateListener != null) {
            // check if the user already has an active session
            TwitterSession twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
            if(twitterSession != null && twitterSession.getAuthToken() != null) {
                long userId = twitterSession.getUserId();
                String token = twitterSession.getAuthToken().token;
                String secret = twitterSession.getAuthToken().secret;
                // broadcast result
                TwitterCredentials credentials = new TwitterCredentials(String.valueOf(userId), token, secret);
                loginStateListener.onLoggedIn(TwitterConfig.provider, credentials);
            }
            else {
                loginStateListener.onLoggedOut(TwitterConfig.provider);
            }
        }
    }

    /**
     * Triggers the login process of the SDK.
     * @param activity The activity which is starting the login process.
     * @param listener The callback listener.
     */
    public void login(Activity activity, SocialiteLoginListener listener) {
        // set listener
        loginListener = listener;
        // trigger login
        twitterAuthClient.authorize(activity, twitterSessionCallback);
    }

    /**
     * Called to forward the login results for the TwitterAuthClient.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(twitterAuthClient != null)   twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login callbacks listener.
     */
    Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            if(result != null) {
                // check result
                TwitterAuthToken authToken = result.data.getAuthToken();
                if(authToken != null) {
                    long userId = result.data.getUserId();
                    String token = authToken.secret;
                    String secret = authToken.secret;
                    // broadcast result
                    broadcastLoginSuccess(token, secret, userId);
                    return;
                }
            }
            // if something wen wrong
            broadcastLoginError(null);
        }

        @Override
        public void failure(TwitterException e) {
            broadcastLoginError(e != null? e.getMessage(): null);
        }
    };

    // provider callbacks //

    /**
     * Broadcasts a login success flag along with the current session data.
     * @param token The auth token of the current active session.
     * @param secret The auth secret of the current active session.
     * @param userId The Twitter ID of the logged user.
     */
    private void broadcastLoginSuccess(String token, String secret, long userId) {
        if(loginListener != null) {
            TwitterCredentials credentials = new TwitterCredentials(String.valueOf(userId), token, secret);
            loginListener.onSuccess(TwitterConfig.provider, credentials);
        }
    }

    /**
     * Broadcasts a login error flag.
     * @param error The error message returned by the SDK.
     */
    private void broadcastLoginError(@Nullable String error) {
        if(loginListener != null)   loginListener.onError(TwitterConfig.provider, error);
    }

    /**
     * Broadcasts a login cancelled flag.
     */
    private void broadcastLoginCancel() {
        if(loginListener != null)   loginListener.onCancel(TwitterConfig.provider);
    }
}
