package com.vinelab.android.socialite.sharing.facebook;

import com.vinelab.android.socialite.SocialiteShareExtras;

/**
 * Created by Nabil Souk on 11/4/2015.
 *
 * <p>
 *     Class holding extra data returned from Facebook after successfully
 *     sharing a content.
 * </p>
 */
public class FacebookShareExtras extends SocialiteShareExtras {
    private  String postId;

    public FacebookShareExtras(String postId) {
        super();
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }
}
