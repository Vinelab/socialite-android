package com.vinelab.android.socialite.sharing;

/**
 * Created by Nabil SOuk on 11/12/2015.
 *
 * <p>
 *      Class holding the share count of a link.
 * </p>
 */
public class SocialiteShareCount {
    private final int count;
    private final String link;

    public SocialiteShareCount(int count, String link) {
        this.count = count;
        this.link = link;
    }

    public int getCount() {
        return count;
    }

    public String getLink() {
        return link;
    }
}
