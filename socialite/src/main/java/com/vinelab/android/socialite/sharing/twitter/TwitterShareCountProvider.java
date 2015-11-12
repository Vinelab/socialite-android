package com.vinelab.android.socialite.sharing.twitter;

import com.vinelab.android.socialite.DataAccess;
import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.sharing.SocialiteShareCount;
import com.vinelab.android.socialite.sharing.listeners.SocialiteShareCountListener;

import org.json.JSONObject;


/**
 * Created by Nabil Souk on 11/12/2015.
 *
 * <p>
 *     Class providing share counts of a link on Twitter.
 * </p>
 */
public class TwitterShareCountProvider {
    private static final String apiUrl = "http://cdn.api.twitter.com/1/urls/count.json?url=";

    /*public static void getShareCount(final String link, final SocialiteShareCountListener listener) {
        // check if link or listener null
        if(listener == null)    return;
        if(link == null || link.isEmpty()) {
            listener.onFailed(SocialiteUtils.SOCIALITE_PROVIDER.TWITTER, new SocialiteShareCount(0, link));
        }
        // request count
        SocialiteShareCount shareCount = null;
        try {
            final String request = apiUrl + link;
            final String response = DataAccess.sendGetRequest(request);
            if(response != null && !response.isEmpty()) {
                JSONObject jsonObject = new JSONObject(response);
                int count = jsonObject.getInt("count");
                String url = jsonObject.getString("url");
                shareCount = new SocialiteShareCount(count, url);
            }
        }
        catch (Exception e) {}

        if(shareCount == null)   listener.onFailed(SocialiteUtils.SOCIALITE_PROVIDER.TWITTER, new SocialiteShareCount(0, link));
        else    listener.onReceived(SocialiteUtils.SOCIALITE_PROVIDER.TWITTER, shareCount);
    }
*/
    public static SocialiteShareCount getShareCount(final String link) {
        SocialiteShareCount shareCount = null;
        // check if link or listener null
        if(link != null && !link.isEmpty()) {
            // request count
            try {
                final String request = apiUrl + link;
                final String response = DataAccess.sendGetRequest(request);
                if(response != null && !response.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(response);
                    int count = jsonObject.getInt("count");
                    String url = jsonObject.getString("url");
                    shareCount = new SocialiteShareCount(count, url);
                }
            }
            catch (Exception e) {}
        }

        return shareCount;
    }
}
