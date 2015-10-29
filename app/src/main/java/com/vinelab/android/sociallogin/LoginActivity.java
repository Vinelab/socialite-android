package com.vinelab.android.sociallogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.vinelab.android.sociallogin.socialite.SocialiteCredentials;
import com.vinelab.android.sociallogin.socialite.SocialiteUtils.SOCIALITE_PROVIDER;
import com.vinelab.android.sociallogin.socialite.facebook.FacebookLoginProvider;
import com.vinelab.android.sociallogin.socialite.listeners.SocialiteLoginListener;
import com.vinelab.android.sociallogin.socialite.listeners.SocialiteUserStateListener;
import com.vinelab.android.sociallogin.socialite.twitter.TwitterLoginProvider;

public class LoginActivity extends Activity {
    ProgressDialog pdLoader;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init fb sdk
        FacebookLoginProvider.getInstance().initialize(getApplicationContext());
        // init twitter sdk
        TwitterLoginProvider.getInstance().initialize(getApplicationContext());

        // buttons click events
        findViewById(R.id.btnLoginFacebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViaFacebook();
            }
        });
        findViewById(R.id.btnLoginTwitter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViaTwitter();
            }
        });

        // progress loader
        pdLoader = new ProgressDialog(this);
        pdLoader.setMessage("Loading...");
        pdLoader.setCancelable(false);
        pdLoader.setIndeterminate(true);
    }

    // FACEBOOK //

    void loginViaFacebook() {
        // check if user is already logged in
        FacebookLoginProvider.getInstance().isUserLoggedIn(new SocialiteUserStateListener() {
            @Override
            public void onLoggedIn(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
                loginListener.onSuccess(provider, credentials);
            }

            @Override
            public void onLoggedOut(SOCIALITE_PROVIDER provider) {
                // show loader
                displayLoader();
                // login
                FacebookLoginProvider.getInstance().logInWithReadPermissions(LoginActivity.this, null, loginListener);
            }
        });
    }

    // TWITTER //

    void loginViaTwitter() {
        // check if user is already logged in
        TwitterLoginProvider.getInstance().isUserLoggedIn(new SocialiteUserStateListener() {
            @Override
            public void onLoggedIn(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
                loginListener.onSuccess(provider, credentials);
            }

            @Override
            public void onLoggedOut(SOCIALITE_PROVIDER provider) {
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
        public void onSuccess(SOCIALITE_PROVIDER provider, SocialiteCredentials credentials) {
            // hide loader
            dismissLoader();
            // show result
            Toast.makeText(getApplicationContext(), provider.toString() + " login success: & userId=" + credentials.getUserId(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SOCIALITE_PROVIDER provider) {
            // hide loader
            dismissLoader();
            // show result
            Toast.makeText(getApplicationContext(), provider.toString() + " login cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SOCIALITE_PROVIDER provider, String error) {
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
