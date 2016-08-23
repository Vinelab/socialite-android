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
        int shareCount = 0;
        int commentCount = 0;
        String shareLink = link;
        // check if link or listener null
        if(link != null && !link.isEmpty()) {
            // request count
            try {
                final String request = apiUrl + link;
                final String response = DataAccess.sendGetRequest(request);
                if(response != null && !response.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(response);
                    // get count from share object
                    if (!jsonObject.isNull("share")) {
                        JSONObject jsonShare = jsonObject.getJSONObject("share");
                        if (!jsonShare.isNull("share_count"))   shareCount = jsonShare.getInt("share_count");
                        if (!jsonShare.isNull("comment_count"))   commentCount = jsonShare.getInt("comment_count");
                    }
                    // get url
                    if (!jsonObject.isNull("id")) {
                        shareLink = jsonObject.getString("id");
                    }
                }
            }
            catch (Exception e) {}
        }
        return new FacebookShareCount(shareLink, shareCount, commentCount);
    }
}
