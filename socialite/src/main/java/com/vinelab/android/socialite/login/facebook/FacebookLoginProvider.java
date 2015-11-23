package com.vinelab.android.socialite.login.facebook;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.login.listeners.SocialiteLoginListener;
import com.vinelab.android.socialite.login.listeners.SocialiteUserStateListener;

import java.util.Collection;

/**
 * Created by Nabil Souk on 10/28/2015.
 *
 * <p>
 *     Class providing Facebook login functionality.
 * </p>
 */
public class FacebookLoginProvider {
    private static FacebookLoginProvider facebookLoginProvider;
    private CallbackManager callbackManager;
    private SocialiteLoginListener loginListener;
    private final SocialiteUtils.SOCIALITE_PROVIDER provider = SocialiteUtils.SOCIALITE_PROVIDER.FACEBOOK;

    private FacebookLoginProvider() {
        initialize();
    }

    public static FacebookLoginProvider getInstance() {
        if(facebookLoginProvider == null)   facebookLoginProvider = new FacebookLoginProvider();
        return facebookLoginProvider;
    }

    /**
     * Initializes the tools needed.
     */
    private void initialize() {
        // Create the callback manager to handle the login responses
        callbackManager = CallbackManager.Factory.create();
        // register for login callbacks from LoginManager
        LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
    }

    /**
     * Called to forward the login results for the CallbackManager.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(callbackManager != null) callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login callbacks listener.
     */
    private FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // check the result of login
            if(loginResult != null) {
                // get the access token object
                AccessToken accessToken = loginResult.getAccessToken();
                if(accessToken != null) {
                    // get the login token and user ID values
                    String token = accessToken.getToken();
                    String userId = accessToken.getUserId();
                    // broadcast results
                    broadcastLoginSuccess(token, userId);
                }
                else {
                    // broadcast unknown error
                    broadcastLoginError(null);
                }
            }
            else {
                // broadcast unknown error
                broadcastLoginError(null);
            }
        }

        @Override
        public void onCancel() {
            // broadcast login cancelled
            broadcastLoginCancel();
        }

        @Override
        public void onError(FacebookException error) {
            // broadcast the error message
            broadcastLoginError(error != null? error.getMessage(): null);
        }
    };

    /**
     * Checks if the user has an active session (still logged in).
     * @param stateListener Callback listener for the user state.
     */
    public void isUserLoggedIn(SocialiteUserStateListener stateListener) {
        // check if listener is null
        if(stateListener == null)  return;
        // get current access token
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        // check if not null
        if(accessToken != null) { // broadcast user logged in
            FacebookCredentials credentials = new FacebookCredentials(accessToken.getUserId(), accessToken.getToken());
            stateListener.onLoggedIn(provider, credentials);
        }
        else { // broadcast user not logged in
            stateListener.onLoggedOut(provider);
        }
    }
    /**
     * Checks if the user has an active session (still logged in).
     */
    public boolean isUserLoggedIn() {
        return (AccessToken.getCurrentAccessToken() != null);
    }

    /**
     * Logs the user in with the requested read permissions.
     * @param activity The activity which is starting the login process.
     * @param permissions The requested permissions.
     * @param listener The callback listener.
     */
    public void logInWithReadPermissions(Activity activity, Collection<String> permissions, SocialiteLoginListener listener) {
        // set the callback listener
        loginListener = listener;
        // trigger the login
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions);
    }

    /**
     * Logs the user in with the requested publish permissions.
     * @param activity The activity which is starting the login process.
     * @param permissions The requested permissions.
     * @param listener The callback listener.
     */
    public void loginWithPublishPermissions(Activity activity, Collection<String> permissions, SocialiteLoginListener listener) {
        // set the callback listener
        loginListener = listener;
        // trigger the login
        LoginManager.getInstance().logInWithPublishPermissions(activity, permissions);
    }

    /**
     * Logs the user in with the requested read permissions.
     * @param fragment The fragment which is starting the login process.
     * @param permissions The requested permissions.
     * @param listener The callback listener.
     */
    public void logInWithReadPermissions(Fragment fragment, Collection<String> permissions, SocialiteLoginListener listener) {
        // set the callback listener
        loginListener = listener;
        // trigger the login
        LoginManager.getInstance().logInWithReadPermissions(fragment, permissions);
    }

    /**
     * Logs the user in with the requested publish permissions.
     * @param fragment The fragment which is starting the login process.
     * @param permissions The requested permissions.
     * @param listener The callback listener.
     */
    public void loginWithPublishPermissions(Fragment fragment, Collection<String> permissions, SocialiteLoginListener listener) {
        // set the callback listener
        loginListener = listener;
        // trigger the login
        LoginManager.getInstance().logInWithPublishPermissions(fragment, permissions);
    }

    /**
     * Logs out the user from any previous active sessions.
     */
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    // provider callbacks //

    /**
     * Broadcasts a login success flag along with the current session credentials.
     * @param token The token of the current active session.
     * @param userId The Facebook ID of the logged user.
     */
    private void broadcastLoginSuccess(String token, String userId) {
        if(loginListener != null) {
            // create credentials
            FacebookCredentials credentials = new FacebookCredentials(userId, token);
            loginListener.onSuccess(provider, credentials);
        }
    }

    /**
     * Broadcasts a login error flag.
     * @param error The error message returned by the SDK.
     */
    private void broadcastLoginError(@Nullable String error) {
        if(loginListener != null)   loginListener.onError(provider, error);
    }

    /**
     * Broadcasts a login cancelled flag.
     */
    private void broadcastLoginCancel() {
        if(loginListener != null)   loginListener.onCancel(provider);
    }
}
