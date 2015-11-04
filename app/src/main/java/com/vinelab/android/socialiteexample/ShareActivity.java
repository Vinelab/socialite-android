package com.vinelab.android.socialiteexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.vinelab.android.socialite.SocialiteShareExtras;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.sharing.email.EmailShareProvider;
import com.vinelab.android.socialite.sharing.facebook.FacebookShareProvider;
import com.vinelab.android.socialite.sharing.listeners.SocialiteShareListener;
import com.vinelab.android.socialite.sharing.twitter.TwitterShareProvider;
import com.vinelab.android.socialite.sharing.whatsapp.WhatsappShareProvider;

public class ShareActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // buttons click events
        findViewById(R.id.btnShareFeed).setOnClickListener(this);
        findViewById(R.id.btnShareMessenger).setOnClickListener(this);
        findViewById(R.id.btnShareWhatsapp).setOnClickListener(this);
        findViewById(R.id.btnShareEmail).setOnClickListener(this);
        findViewById(R.id.btnShareTweet).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShareFeed:
                postOnFacebookFeed();
                break;
            case R.id.btnShareMessenger:
                shareViaFacebookMessenger();
                break;
            case R.id.btnShareTweet:
                shareViaTwitter();
                break;
            case R.id.btnShareWhatsapp:
                shareViaWhatsapp();
                break;
            case R.id.btnShareEmail:
                shareViaEmail();
                break;
        }
    }

    // SHARING //

    String contentUrl = "http://www.techradar.com/reviews/phones/mobile-phones/htc-one-a9-1307203/review";
    String contentTitle = "HTC One A9 review";
    String contentDescription = "The iPhone with the brains of an Android";
    String imageUrl = "http://cdn.mos.techradar.com/art/mobile_phones/HTC/HTC%20One%20A9/HTC%20One%20A9%20hands%20on/One%20A9%20hands%20on/HCToneA9review%20(1)-1200-80.jpg";

    void postOnFacebookFeed() {
        FacebookShareProvider.getInstance().postOnMeFeed(ShareActivity.this, contentUrl, contentTitle, imageUrl, contentDescription, shareListener);
    }

    void shareViaFacebookMessenger() {
        FacebookShareProvider.getInstance().sendMessage(ShareActivity.this, contentUrl, contentTitle, imageUrl, contentDescription, shareListener);
    }

    void shareViaWhatsapp() {
        String message = "N S is sharing content on Whatsapp!";
        if(!WhatsappShareProvider.shareMessage(ShareActivity.this, message)) {
            Toast.makeText(getApplicationContext(), "Whatsapp no installed", Toast.LENGTH_SHORT).show();
        }
    }

    void shareViaEmail() {
        EmailShareProvider.shareMessage(ShareActivity.this, null, contentTitle, contentDescription + " " + contentUrl);
    }

    void shareViaTwitter() {
        TwitterShareProvider.getInstance().composeTweet(ShareActivity.this, contentTitle + " " + contentUrl, shareListener);
    }

    SocialiteShareListener shareListener = new SocialiteShareListener() {
        @Override
        public void onSuccess(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteShareExtras extras) {
            Toast.makeText(getApplicationContext(), provider.toString() + " share success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SocialiteUtils.SOCIALITE_PROVIDER provider) {
            Toast.makeText(getApplicationContext(), provider.toString() + " share cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SocialiteUtils.SOCIALITE_PROVIDER provider, @Nullable String error) {
            Toast.makeText(getApplicationContext(), provider.toString() + " share failed: error=" + (error != null ? error : "unknown"), Toast.LENGTH_SHORT).show();
        }
    };

    // UTILS //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookShareProvider.getInstance().onActivityResult(requestCode, resultCode, data);
        TwitterShareProvider.getInstance().onActivityResult(requestCode, resultCode, data);
    }
}
