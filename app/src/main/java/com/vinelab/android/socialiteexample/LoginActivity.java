package com.vinelab.android.socialiteexample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.vinelab.android.socialite.SocialiteCredentials;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.login.SocialiteUserProfile;
import com.vinelab.android.socialite.login.facebook.FacebookLoginProvider;
import com.vinelab.android.socialite.login.listeners.SocialiteLoginListener;
import com.vinelab.android.socialite.login.listeners.SocialiteProfileListener;
import com.vinelab.android.socialite.login.listeners.SocialiteUserStateListener;
import com.vinelab.android.socialite.login.twitter.TwitterLoginProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LoginActivity extends Activity implements View.OnClickListener{
    ProgressDialog pdLoader;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // buttons click events
        findViewById(R.id.btnLoginFacebook).setOnClickListener(this);
        findViewById(R.id.btnLoginTwitter).setOnClickListener(this);

        // progress loader
        pdLoader = new ProgressDialog(this);
        pdLoader.setMessage("Loading...");
        pdLoader.setCancelable(false);
        pdLoader.setIndeterminate(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginFacebook:
                loginViaFacebook();
                break;
            case R.id.btnLoginTwitter:
                loginViaTwitter();
                break;
        }
    }

    // FACEBOOK //

    void loginViaFacebook() {
        // check if user is already logged in
        FacebookLoginProvider.getInstance().isUserLoggedIn(new SocialiteUserStateListener() {
            @Override
            public void onLoggedIn(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
                loginListener.onSuccess(provider, credentials);
            }

            @Override
            public void onLoggedOut(SocialiteUtils.SOCIALITE_PROVIDER provider) {
                // show loader
                displayLoader();
                // login
                FacebookLoginProvider.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"), loginListener);
            }
        });
    }

    // TWITTER //

    void loginViaTwitter() {
        // check if user is already logged in
        TwitterLoginProvider.getInstance().isUserLoggedIn(new SocialiteUserStateListener() {
            @Override
            public void onLoggedIn(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
                loginListener.onSuccess(provider, credentials);
            }

            @Override
            public void onLoggedOut(SocialiteUtils.SOCIALITE_PROVIDER provider) {
                // show loader
                displayLoader();
                // login
                TwitterLoginProvider.getInstance().login(LoginActivity.this, loginListener);
            }
        });
    }

    // UTILS //

    SocialiteLoginListener loginListener = new SocialiteLoginListener() {
        @Override
        public void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
            // hide loader
            dismissLoader();
            // show result
            Toast.makeText(getApplicationContext(), provider.toString() + " login success: & userId=" + credentials.getUserId(), Toast.LENGTH_SHORT).show();
            /*// check if fb
            if (provider == SocialiteUtils.SOCIALITE_PROVIDER.FACEBOOK) {
                FacebookLoginProvider.getInstance().fetchSessionUser(new SocialiteProfileListener() {
                    @Override
                    public void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteUserProfile profile) {
                        return;
                    }

                    @Override
                    public void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable String error) {
                        return;
                    }
                });
            }*/
        }

        @Override
        public void onCancel(SocialiteUtils.SOCIALITE_PROVIDER provider) {
            // hide loader
            dismissLoader();
            // show result
            Toast.makeText(getApplicationContext(), provider.toString() + " login cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, String error) {
            // hide loader
            dismissLoader();
            // show result
            Toast.makeText(getApplicationContext(), provider.toString() + " login failed: error=" + (error != null ? error : "unknown"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookLoginProvider.getInstance().onActivityResult(requestCode, resultCode, data);
        TwitterLoginProvider.getInstance().onActivityResult(requestCode, resultCode, data);
    }

    void displayLoader() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                pdLoader.show();
            }
        });
    }

    void dismissLoader() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                pdLoader.dismiss();
            }
        });
    }
}
