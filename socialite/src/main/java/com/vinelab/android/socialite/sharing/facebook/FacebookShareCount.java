package com.vinelab.android.socialite.sharing.facebook;

import com.vinelab.android.socialite.sharing.SocialiteShareCount;

/**
 * Created by Nabil on 8/23/2016.
 *
 * <p>
 *     Class holding the share counts, returned by Facebook,
 *     for a given object.
 * </p>
 */
public class FacebookShareCount extends SocialiteShareCount {
    final int commentCount;

    public FacebookShareCount(String link, int shareCount, int commentCount) {
        super(link, shareCount);
        this.commentCount = commentCount;
    }

    public int getCommentCount() {
        return commentCount;
    }
}
