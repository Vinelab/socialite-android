package com.vinelab.android.socialite.sharing.listeners;

import com.vinelab.android.socialite.SocialiteUtils;
import com.vinelab.android.socialite.sharing.SocialiteShareCount;

/**
 * Created by Nabil on 11/12/2015.
 */
public interface SocialiteShareCountListener {
    void onReceived(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteShareCount count);

    void onFailed(SocialiteUtils.SOCIALITE_PROVIDER provider, SocialiteShareCount count);
}
