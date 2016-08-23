package com.vinelab.android.socialite.sharing;

/**
 * Created by Nabil SOuk on 11/12/2015.
 *
 * <p>
 *      Class holding the share count of a link.
 * </p>
 */
public class SocialiteShareCount {
    private final String link;
    private final int count;

    public SocialiteShareCount(String link, int count) {
        this.link = link;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getLink() {
        return link;
    }
}
