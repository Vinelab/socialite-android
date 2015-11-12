package com.vinelab.android.socialite.sharing.facebook;

import com.vinelab.android.socialite.DataAccess;
import com.vinelab.android.socialite.sharing.SocialiteShareCount;

import org.json.JSONObject;

/**
 * Created by Nabil Souk on 11/12/2015.
 *
 * <p>
 *     Class providing share counts of a link on Facebook.
 * </p>
 */
public class FacebookShareCountProvider {
    private static final String apiUrl = "http://graph.facebook.com/?id=";

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
                    int count = jsonObject.getInt("shares");
                    String url = jsonObject.getString("id");
                    shareCount = new SocialiteShareCount(count, url);
                }
            }
            catch (Exception e) {}
        }

        return shareCount;
    }
}
