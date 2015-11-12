package com.vinelab.android.socialiteexample;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.sharing.SocialiteShareCount;
import com.vinelab.android.socialite.sharing.facebook.FacebookShareCountProvider;
import com.vinelab.android.socialite.sharing.listeners.SocialiteShareCountListener;
import com.vinelab.android.socialite.sharing.twitter.TwitterShareCountProvider;

public class ShareCountActivity extends Activity {
    Handler handler = new Handler();
    final String link = "http://vinelab.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_count);

//        TwitterShareCountProvider.getShareCount(link, shareCountListener);
        requestCounts();
    }

    void requestCounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get twitter count
                SocialiteShareCount countTwitter = TwitterShareCountProvider.getShareCount(link);
                // get Facebook count
                SocialiteShareCount countFacebook = FacebookShareCountProvider.getShareCount(link);
                // display counts
                displayToastMessage(countTwitter == null? "Twitter failed": "Twitter count for " + countTwitter.getLink() + " is " + countTwitter.getCount());
                displayToastMessage(countFacebook == null? "Facebook failed": "Facebook count for " + countFacebook.getLink() + " is " + countFacebook.getCount());
            }
        }).start();
    }

    /*SocialiteShareCountListener shareCountListener = new SocialiteShareCountListener() {
        @Override
        public void onReceived(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteShareCount count) {
            displayToastMessage("onReceived: " + provider.toString() + " count for " + count.getLink() + " is " +  count.getCount());
        }

        @Override
        public void onFailed(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteShareCount count) {
            displayToastMessage("onFailed: " + provider.toString() + " count for " + count.getLink());
        }
    };*/

    void displayToastMessage(final String message) {
        if(message != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
