package com.vinelab.android.socialiteexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vinelab.android.socialite.FacebookConfig;
import com.vinelab.android.socialite.TwitterConfig;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init fb sdk
        FacebookConfig.initializeSDK(getApplicationContext());
        // init twitter sdk
        String consumerKey = getResources().getString(R.string.twitter_consumer_key);
        String consumerSecret = getResources().getString(R.string.twitter_consumer_secret);
        TwitterConfig.initializeKits(getApplicationContext(), true, consumerKey, consumerSecret);
        // set click events
        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnSharing).setOnClickListener(this);
        findViewById(R.id.btnShareCount).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                openLogin();
                break;
            case R.id.btnSharing:
                openSharing();
                break;
            case R.id.btnShareCount:
                openShareCount();
                break;
        }
    }

    void openLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    void openSharing() {
        startActivity(new Intent(getApplicationContext(), ShareActivity.class));
    }

    void openShareCount() {
        startActivity(new Intent(getApplicationContext(), ShareCountActivity.class));
    }
}
